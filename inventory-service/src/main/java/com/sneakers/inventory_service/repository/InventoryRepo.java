package com.sneakers.inventory_service.repository;

import com.sneakers.inventory_service.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<InventoryItem,Long> {
    List<InventoryItem>findByItemIdIn(List<Long> itemIds);
}
