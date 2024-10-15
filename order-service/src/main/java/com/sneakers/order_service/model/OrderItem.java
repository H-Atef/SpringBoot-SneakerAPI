package com.sneakers.order_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "s_order_items")
public class OrderItem {
    @Id
    @SequenceGenerator(name="items_seq",sequenceName ="items_seq" ,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "items_seq")
    private Long itemId;
    private Long searchId;
    private String itemName;
    private BigDecimal itemPrice;
    private String itemCode;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;


}
