package com.sneakers.inventory_service.service;

import com.sneakers.inventory_service.dto.AvailableItem;
import com.sneakers.inventory_service.dto.InvItemRequest;
import com.sneakers.inventory_service.dto.InvItemResponse;
import com.sneakers.inventory_service.external.dto.SneakersResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InventoryService {
    public List<InvItemResponse> viewInventoryItems();
    public void addNewItem(InvItemRequest invItemRequest);
    public void deleteItem(Long itemId);
    public void updateItem(Long itemId,InvItemRequest invItemRequest);
    public boolean checkItemInStock(Long itemId);
    public CompletableFuture<List<AvailableItem>> checkItemsInStock(List<Long> itemIds);
    public CompletableFuture<List<SneakersResponse>>getItemsDetailsByCategory(String category);
    public CompletableFuture<SneakersResponse> getItemDetailsByName(String model);
}
