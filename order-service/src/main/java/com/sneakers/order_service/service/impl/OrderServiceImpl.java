package com.sneakers.order_service.service.impl;
import com.sneakers.order_service.dto.OrderRequest;
import com.sneakers.order_service.dto.OrderResponse;
import com.sneakers.order_service.dto.mapper.OrderMapper;
import com.sneakers.order_service.external.client.InventoryFeignClient;
import com.sneakers.order_service.external.dto.AvailableItem;
import com.sneakers.order_service.model.Order;
import com.sneakers.order_service.model.OrderItem;
import com.sneakers.order_service.repository.OrderRepo;
import com.sneakers.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public String submitOrder(OrderRequest orderRequest) {
        // Retrieve the list of order items from the order request and map them using the orderMapper
        List<OrderItem> orderItemsList = new ArrayList<>(orderRequest.getOrderItems()
                .stream()
                .map(orderMapper::mapItemsDto)
                .toList());

        // Create a new Order object with a unique order number and the list of order items
        Order order = Order.builder()
                .orderNum(UUID.randomUUID().toString())
                .orderItems(orderItemsList)
                .build();

        // Set the order for each order item in the list
        for (OrderItem orderItem : orderItemsList) {
            orderItem.setOrder(order);
        }

        // Extract the search IDs from the order items to check for item availability
        List<Long> orderItemsSearchIds = orderItemsList.stream()
                .map(OrderItem::getSearchId)
                .toList();

        // Retrieve the available items from the inventory service based on the search IDs
        List<AvailableItem> availableItems = inventoryFeignClient.checkItems(orderItemsSearchIds);

        if(availableItems.getFirst().getItemId()==-1L){
            return "The inventory service is currently unavailable. Please try again later";
        }
        if(availableItems.getFirst().getItemId()==-2L){
            return "Timeout";
        }



        // Filter the available items to only include those that are in stock
        List<AvailableItem> inStockItems = availableItems.stream()
                .filter(AvailableItem::getIsInStock)
                .toList();

        // Check if no items are in stock, return a message indicating all items are out of stock
        if (inStockItems.isEmpty()) {
            return "Unfortunately, All Items Are Out Of Stock";
        }

        // Update each order item with the corresponding item name and price if available
        for (OrderItem orderItem : orderItemsList) {
            Optional<AvailableItem> matchingItem = availableItems.stream()
                    .filter(item -> item.getItemId().equals(orderItem.getSearchId()))
                    .findFirst();

            matchingItem.ifPresent(item -> {
                orderItem.setItemName(item.getItemName());
                orderItem.setItemPrice(item.getItemPrice());
            });
        }

        // Retrieve the IDs of the items that are in stock
        List<Long> inStockItemIds = inStockItems.stream()
                .map(AvailableItem::getItemId)
                .toList();

        // Remove order items that are not in stock from the orderItemsList
        orderItemsList.removeIf(orderItem -> !inStockItemIds.contains(orderItem.getSearchId()));

        // Save the order in the database
        orderRepo.save(order);

        // Provide appropriate success messages based on the order status
        if (orderItemsList.size() < orderItemsSearchIds.size()) {
            return "Only Available Items with IDs: " + inStockItemIds + " are Found In Stock. The Order Placed Successfully with available items.";
        }

        return "Order Placed Successfully!";
    }

    @Override
    public List<OrderResponse> viewAllOrders() {
        return orderRepo.findAll().stream().map(orderMapper::mapToDto).toList();
    }

    @Override
    public void deleteOrder(Long orderId) {
        Optional<Order> order=orderRepo.findById(orderId);
        order.ifPresent(orderRepo::delete);
    }


}
