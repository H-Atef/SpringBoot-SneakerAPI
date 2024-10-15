package com.sneakers.order_service.controller;


import com.sneakers.order_service.dto.OrderRequest;
import com.sneakers.order_service.dto.OrderResponse;
import com.sneakers.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderServ;


    @GetMapping("/view-order")
    List<OrderResponse> viewAllOrders() {
        return orderServ.viewAllOrders();
    }

    @PostMapping("/add-order")
    ResponseEntity<String> submitOrder(@RequestBody OrderRequest orderRequest) {

        String result = orderServ.submitOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/delete-order/{order-id}")
    ResponseEntity<String> deleteOrder(@PathVariable("order-id") Long orderId) {
        orderServ.deleteOrder(orderId);
        return ResponseEntity.ok("The Order Deleted Successfully!");
    }

}
