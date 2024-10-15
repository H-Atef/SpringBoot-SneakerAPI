package com.sneakers.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "s_order")
public class Order {
    @Id
    @SequenceGenerator(name="order_seq",sequenceName ="order_seq" ,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "order_seq")
    private Long orderId;
    private String orderNum;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<OrderItem> orderItems;


}
