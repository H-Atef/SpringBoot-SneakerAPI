package com.sneakers.inventory_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SneakersResponse {
    private String productId;
    private String title;
    private String link;
    private BigDecimal price;
    private BigDecimal listPrice;
    private Integer quantity;
    private String productCode;
    private String imageLink;
    private String vendor;
    private BigDecimal discount;
    private String category;
}