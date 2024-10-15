package com.sneakers.order_service.external.client;

import com.sneakers.order_service.external.dto.AvailableItem;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@FeignClient(value = "INVENTORY-SERVICE")
public interface InventoryFeignClient {

    @GetMapping("/api/v1/inventory/check-items")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    public List<AvailableItem> checkItems(@RequestParam(required = false) List<Long> itemIds);


    default List<AvailableItem> fallbackMethod(List<Long> itemIds, Throwable throwable) {
        System.out.println(throwable.toString());
        AvailableItem item = new AvailableItem();


        if(throwable.toString().contains("[408]")) {item.setItemId(-2L);}
        if(!throwable.toString().contains("[408]")) {item.setItemId(-1L);}

        return List.of(item);
    }





}
