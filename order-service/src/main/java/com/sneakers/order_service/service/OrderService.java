package com.sneakers.order_service.service;

import com.sneakers.order_service.dto.OrderRequest;
import com.sneakers.order_service.dto.OrderResponse;
import com.sneakers.order_service.model.Order;

import java.util.List;


public interface OrderService {
    public String submitOrder(OrderRequest orderRequest);
    public List<OrderResponse> viewAllOrders();
    public void deleteOrder(Long orderId);
}
