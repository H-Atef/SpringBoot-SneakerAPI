package com.sneakers.sneakers_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("product_id")
    private String productId;

    private String title;
    private String link;
    private BigDecimal price;

    @JsonProperty("list_price")
    private BigDecimal listPrice;

    private Integer quantity;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("image_link")
    private String imageLink;

    private String vendor;
    private BigDecimal discount;
    private String category;
}
