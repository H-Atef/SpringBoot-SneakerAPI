package com.sneakers.inventory_service.service.impl;

import com.sneakers.inventory_service.dto.AvailableItem;
import com.sneakers.inventory_service.dto.InvItemRequest;
import com.sneakers.inventory_service.dto.InvItemResponse;
import com.sneakers.inventory_service.dto.mapper.InventoryMapper;
import com.sneakers.inventory_service.external.dto.SneakersResponse;
import com.sneakers.inventory_service.model.InventoryItem;
import com.sneakers.inventory_service.repository.InventoryRepo;
import com.sneakers.inventory_service.service.InventoryService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;
    private final InventoryMapper inventoryMapper;
    private final WebClient.Builder webClientBuilder;

    @Override
    public List<InvItemResponse> viewInventoryItems() {
        return inventoryRepo.findAll().stream().map(inventoryMapper::mapToDto).toList();
    }

    @Override
    public void addNewItem(InvItemRequest invItemRequest) {

        inventoryRepo.save(inventoryMapper.mapToItem(invItemRequest));

    }

    @Override
    public void deleteItem(Long itemId) {

        Optional<InventoryItem> item = inventoryRepo.findById(itemId);
        item.ifPresent(inventoryRepo::delete);
    }

    @Override
    public void updateItem(Long itemId, InvItemRequest invItemRequest) {

        Optional<InventoryItem> item = inventoryRepo.findById(itemId);

        invItemRequest.setItemId(itemId);

        if (item.isPresent()) {
            item.get().setItemId(invItemRequest.getItemId());
            item.get().setItemName(invItemRequest.getItemName());
            item.get().setItemPrice(invItemRequest.getItemPrice());
            item.get().setItemQuantity(invItemRequest.getItemQuantity());
            inventoryRepo.save(item.get());
        }

    }

    @Override
    public boolean checkItemInStock(Long itemId) {
        Optional<InventoryItem> item = inventoryRepo.findById(itemId);

        return item.isPresent() && item.get().getItemQuantity() > 0;
    }


    @Override
    @Bulkhead(name = "itemsCheck", fallbackMethod = "fallbackMethod", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "itemsCheck", fallbackMethod = "fallbackMethod")
    public CompletableFuture<List<AvailableItem>> checkItemsInStock(List<Long> itemIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                return checkItems(itemIds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public CompletableFuture<ResponseEntity<String>> fallbackMethod(List<Long> itemIds, Exception e) {
        return CompletableFuture.supplyAsync(() -> {
            List<AvailableItem> availableItems = inventoryRepo.findByItemIdIn(itemIds)
                    .stream().map(inventoryMapper::mapToAvailableItem).toList();
            handleItemsQuantities(itemIds, availableItems, 1);
            String errorMessage = "Timeout occurred while processing the request for itemIds: " + itemIds;
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorMessage);
        });
    }


    @Transactional
    private List<AvailableItem> checkItems(List<Long> itemIds) throws InterruptedException {


        List<AvailableItem> availableItems = inventoryRepo.findByItemIdIn(itemIds)
                .stream().map(inventoryMapper::mapToAvailableItem).toList();

        handleItemsQuantities(itemIds, availableItems, -1);

        return availableItems;
    }


    @Transactional
    private void handleItemsQuantities(List<Long> itemIds, List<AvailableItem> availableItems, int amount) {


        if (!availableItems.isEmpty()) {
            List<InventoryItem> updatedInventoryItems = inventoryRepo.findByItemIdIn(itemIds).stream().toList();

            for (InventoryItem inventoryItem : updatedInventoryItems) {
                if (inventoryItem != null) {
                    if (inventoryItem.getItemQuantity() > 0) {

                        inventoryItem.setItemQuantity(inventoryItem.getItemQuantity() + amount);

                    }

                }
            }

            inventoryRepo.saveAll(updatedInventoryItems);


        }


    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name="sneakers",fallbackMethod = "sneakersFallbackMethod")
    @Retry(name = "sneakers")
    @TimeLimiter(name="sneakers",fallbackMethod = "sneakersFallbackTimeoutMethod")
    public CompletableFuture<List<SneakersResponse>> getItemsDetailsByCategory(String category) {
        SneakersResponse[] sneakersResponses = webClientBuilder.build().get()
                .uri("http://sneakers-service/api/v1/sneakers/find-items-by-category/{product-category}"
                        , category)
                .retrieve()
                .bodyToMono(SneakersResponse[].class).block();

        if (sneakersResponses != null) {
            return CompletableFuture.supplyAsync(()->Arrays.stream(sneakersResponses).toList());
        }
        return CompletableFuture.supplyAsync(List::of);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name="sneakers2",fallbackMethod = "sneakersFallbackMethod")
    @Retry(name = "sneakers2")
    @TimeLimiter(name="sneakers2",fallbackMethod = "sneakersFallbackTimeoutMethod")
    public CompletableFuture<SneakersResponse> getItemDetailsByName(String model) {
       SneakersResponse sneakersResponse= webClientBuilder.build().get()
                .uri("http://sneakers-service/api/v1/sneakers/search-model-by-name/{product-model}", model)
                .retrieve()
                .bodyToMono(SneakersResponse.class).block();
        return CompletableFuture.supplyAsync(()->sneakersResponse);


    }

    public CompletableFuture<ResponseEntity<String>> sneakersFallbackMethod(Exception e) {
        return CompletableFuture.supplyAsync(() -> {
            String errorMessage = "The sneakers service is currently unavailable. Please try again later";
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
        });
    }

    public CompletableFuture<ResponseEntity<String>> sneakersFallbackTimeoutMethod(Exception e) {
        return CompletableFuture.supplyAsync(() -> {
            String errorMessage = "Timeout occurred while processing the request";
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorMessage);
        });
    }



}
