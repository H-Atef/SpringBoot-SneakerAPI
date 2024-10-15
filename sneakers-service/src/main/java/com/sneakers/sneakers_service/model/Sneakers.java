package com.sneakers.sneakers_service.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "product")
public class Sneakers {
    @Id
    private String proId;
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
