package com.sneakers.inventory_service.dto.mapper;


import com.sneakers.inventory_service.dto.AvailableItem;
import com.sneakers.inventory_service.dto.InvItemRequest;
import com.sneakers.inventory_service.dto.InvItemResponse;
import com.sneakers.inventory_service.model.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryMapper {

    public InvItemResponse mapToDto(InventoryItem inventoryItem){
        return InvItemResponse.builder()
                .itemId(inventoryItem.getItemId())
                .itemCode(inventoryItem.getItemCode())
                .itemName(inventoryItem.getItemName())
                .itemPrice(inventoryItem.getItemPrice())
                .itemQuantity(inventoryItem.getItemQuantity())
                .build();
    }

    public InventoryItem mapToItem(InvItemRequest inventoryItem) {
        return InventoryItem.builder()
                .itemCode(UUID.randomUUID().toString())
                .itemName(inventoryItem.getItemName())
                .itemPrice(inventoryItem.getItemPrice())
                .itemQuantity(inventoryItem.getItemQuantity())
                .build();
    }

    public AvailableItem mapToAvailableItem(InventoryItem inventoryItem) {

        return AvailableItem.builder()
                .itemId(inventoryItem.getItemId())
                .itemCode(inventoryItem.getItemCode())
                .itemName(inventoryItem.getItemName())
                .itemPrice(inventoryItem.getItemPrice())
                .isInStock(inventoryItem.getItemQuantity()>0)
                .build();

    }
}
