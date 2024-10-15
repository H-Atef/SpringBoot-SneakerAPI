package com.sneakers.order_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableItem {
    private Long itemId;
    private String itemName;
    private String itemCode;
    private BigDecimal itemPrice;
    private Boolean isInStock;
}
