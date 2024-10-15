package com.sneakers.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvItemResponse {
    private Long itemId;
    private String itemName;
    private String itemCode;
    private BigDecimal itemPrice;
    private Integer itemQuantity;
}
