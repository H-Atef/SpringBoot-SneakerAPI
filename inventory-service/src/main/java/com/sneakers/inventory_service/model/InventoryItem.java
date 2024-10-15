package com.sneakers.inventory_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "s_inventory")
public class InventoryItem {
    @Id
    @SequenceGenerator(name = "inv_seq",sequenceName = "inv_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "inv_seq")
    private Long itemId;
    private String itemName;
    private String itemCode;
    private BigDecimal itemPrice;
    private Integer itemQuantity;
}
