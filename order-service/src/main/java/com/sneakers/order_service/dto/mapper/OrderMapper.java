package com.sneakers.order_service.dto.mapper;

import com.sneakers.order_service.dto.OrderItemDto;
import com.sneakers.order_service.dto.OrderRequest;
import com.sneakers.order_service.dto.OrderResponse;
import com.sneakers.order_service.model.Order;
import com.sneakers.order_service.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {
    public OrderItem mapItemsDto(OrderItemDto orderItemsDto) {

        return OrderItem.builder()
                .searchId(orderItemsDto.getSearchId())
                .itemCode(UUID.randomUUID().toString())
                .build();
    }

    public OrderResponse mapToDto (Order order) {

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNum(order.getOrderNum())
                .orderItems(order.getOrderItems().stream().map(this::mapItemsToDto).toList())
                .build();
    }

    private OrderItemDto mapItemsToDto(OrderItem orderItem){
        return OrderItemDto.builder()
                .itemCode(orderItem.getItemCode())
                .searchId(orderItem.getSearchId())
                .itemName(orderItem.getItemName())
                .itemPrice(orderItem.getItemPrice())
                .build();

    }
}
