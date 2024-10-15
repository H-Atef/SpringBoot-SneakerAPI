package com.sneakers.sneakers_service.service;


import com.sneakers.sneakers_service.dto.SneakersRequest;
import com.sneakers.sneakers_service.dto.SneakersResponse;

import java.util.List;

public interface SneakersService {
    public List<SneakersResponse> viewAllSneakers();
    public void addProduct(SneakersRequest productRequest);
    public void deleteProduct(String productModel);
    public void updateProduct(SneakersRequest productRequest, String productModel);
    public SneakersResponse searchItemByName(String productModel);
    public List<SneakersResponse> searchItemsByCategory(String category);
    public List<SneakersResponse> getScrapedSneakers();
    public List<SneakersResponse> getScrapedSneakersByCategory(String category,int pages);
    public List<SneakersResponse> getScrapedSneakersByModelName(String model);
    public void getAndSaveScrapedSneakers();

}
