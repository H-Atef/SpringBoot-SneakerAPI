package com.sneakers.inventory_service.controller;


import com.sneakers.inventory_service.dto.AvailableItem;
import com.sneakers.inventory_service.dto.InvItemRequest;
import com.sneakers.inventory_service.dto.InvItemResponse;
import com.sneakers.inventory_service.external.dto.SneakersResponse;
import com.sneakers.inventory_service.service.InventoryService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/view-inventory-items")
    public List<InvItemResponse> viewAllItems(){
        return inventoryService.viewInventoryItems();
    }

    @PostMapping("/add-item")
    public ResponseEntity<String> addNewItem(@RequestBody InvItemRequest invItemRequest){

        inventoryService.addNewItem(invItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Item Added Successfully!");
    }

    @DeleteMapping("/delete-item/{item-id}")
    public ResponseEntity<String> deleteItem(@PathVariable("item-id") Long itemId){

        inventoryService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item Deleted Successfully!");

    }


    @PutMapping("/update-item/{item-id}")
    public ResponseEntity<String> updateItem(@PathVariable("item-id") Long itemId,
                                             @RequestBody InvItemRequest invItemRequest){

        inventoryService.updateItem(itemId,invItemRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Item Updated Successfully!");

    }

    @GetMapping("/check-item/{item-id}")
    public ResponseEntity<String>checkItem(@PathVariable("item-id") Long itemId){
        boolean itemFound=inventoryService.checkItemInStock(itemId);
        if(itemFound){
            return ResponseEntity.ok("Item Found");
        }
        return ResponseEntity.badRequest().body("Item Not Found !!");
    }

    @GetMapping("/check-items")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<List<AvailableItem>> checkItems(@RequestParam(required = false) List<Long>itemIds){
        return inventoryService.checkItemsInStock(itemIds);
    }



    @GetMapping("/item-details/{item-name}")
    public CompletableFuture<SneakersResponse> viewItemDetails(@PathVariable("item-name") String itemName){
        return inventoryService.getItemDetailsByName(itemName);
    }

    @GetMapping("/items-details/{category}")
    public CompletableFuture<List<SneakersResponse>> viewItemsDetails(@PathVariable("category") String category){
        return inventoryService.getItemsDetailsByCategory(category);
    }


}
