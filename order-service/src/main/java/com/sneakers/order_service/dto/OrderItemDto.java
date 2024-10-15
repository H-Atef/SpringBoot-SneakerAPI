package com.sneakers.order_service.dto;


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
public class OrderItemDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long searchId;
    private String itemName;
    private BigDecimal itemPrice;
    private String itemCode;
}
