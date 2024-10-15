package com.sneakers.sneakers_service.controller;

import com.sneakers.sneakers_service.dto.SneakersRequest;
import com.sneakers.sneakers_service.dto.SneakersResponse;
import com.sneakers.sneakers_service.service.SneakersService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sneakers")
public class SneakersController {

    private final SneakersService productService;

    @GetMapping
    List<SneakersResponse> viewAllSneakers() {
        return productService.viewAllSneakers();
    }

    @PostMapping("/add-sneakers")
    ResponseEntity<String> addNewSneakers(@RequestBody SneakersRequest productRequest) {

        productService.addProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("New Sneakers Added Successfully!");
    }

    @DeleteMapping("/delete-sneakers/{product-model}")
    ResponseEntity<String> deleteSneakers(@PathVariable("product-model") String productModel) {

        productService.deleteProduct(productModel);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Sneakers Deleted Successfully!");
    }

    @PutMapping("/update-sneakers/{product-model}")
    ResponseEntity<String> UpdateSneakersInfo(@RequestBody SneakersRequest productRequest,
                                              @PathVariable("product-model") String productModel) {

        productService.updateProduct(productRequest, productModel);

        return ResponseEntity.ok("Sneakers Updated Successfully!");
    }


    @GetMapping("/search-model-by-name/{product-model}")
    SneakersResponse searchByModelName(@PathVariable("product-model") String productModel) {
        return productService.searchItemByName(productModel);

    }

    @GetMapping("/find-items-by-category/{product-category}")
    List<SneakersResponse> searchByCategory(@PathVariable("product-category") String productCategory) {
        return productService.searchItemsByCategory(productCategory);

    }


    @GetMapping("/get-scraped-sneakers")
    @CircuitBreaker(name = "sneakers1", fallbackMethod = "getFallbackMethod")
    List<SneakersResponse> getsScrapedSneakers() {
        return productService.getScrapedSneakers();
    }


    @GetMapping("/get-and-save-scraped-sneakers")
    @CircuitBreaker(name = "sneakers2", fallbackMethod = "getAndScrapeFallbackMethod")
    ResponseEntity<String> getAndSaveScrapedSneakers() {
        productService.getAndSaveScrapedSneakers();
        return ResponseEntity.status(HttpStatus.CREATED).body("All data retrieved and saved successfully!");
    }


    @GetMapping("/get-sneakers-info-by-category")
    @CircuitBreaker(name = "sneakers3", fallbackMethod = "findByCategoryFallbackMethod")
    List<SneakersResponse> getScrapedSneakersByCategory(
            @RequestParam(required = false, defaultValue = "Sneakers") String category,
            @RequestParam(required = false, defaultValue = "1") int pages) {

        return productService.getScrapedSneakersByCategory(category, pages);
    }


    @GetMapping("/get-sneakers-info-by-model-name/{model}")
    @CircuitBreaker(name = "sneakers4", fallbackMethod = "findByModelFallbackMethod")
    List<SneakersResponse> getScrapedSneakersByModelName(@PathVariable String model) {
        return productService.getScrapedSneakersByModelName(model);
    }


    // Fallback method for CallNotPermittedException
    public List<SneakersResponse> getFallbackMethod(Exception e) {
        return List.of();
    }

    public ResponseEntity<String> getAndScrapeFallbackMethod(Exception e) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service is not available at the moment, please try again later");


    }

    public List<SneakersResponse> findByCategoryFallbackMethod(Exception e) {
        return List.of();
    }

    public List<SneakersResponse> findByModelFallbackMethod(Exception e) {
        return List.of();
    }

}
