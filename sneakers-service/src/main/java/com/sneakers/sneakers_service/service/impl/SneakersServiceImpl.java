package com.sneakers.sneakers_service.service.impl;

import com.sneakers.sneakers_service.dto.SneakersRequest;
import com.sneakers.sneakers_service.dto.SneakersResponse;
import com.sneakers.sneakers_service.dto.mapper.SneakersMapper;
import com.sneakers.sneakers_service.model.Sneakers;
import com.sneakers.sneakers_service.repository.SneakersRepo;
import com.sneakers.sneakers_service.service.SneakersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SneakersServiceImpl implements SneakersService {

    private final SneakersRepo productRepo;
    private final SneakersMapper productMapper;

    @Override
    public List<SneakersResponse> viewAllSneakers() {
        return productRepo.findAll().stream().map(productMapper::mapToDto).toList();

    }

    @Override
    public void addProduct(SneakersRequest productRequest) {

        Sneakers product = productMapper.mapToProduct(productRequest);

        productRepo.save(product);

    }

    @Override
    public void deleteProduct(String productModel) {

        Optional<Sneakers> product = productRepo.findByTitle(productModel);
        product.ifPresent(productRepo::delete);
    }

    @Override
    public void updateProduct(SneakersRequest productRequest, String productModel) {
        Optional<Sneakers> product = productRepo.findByTitle(productModel);
        if (product.isPresent()) {
            Sneakers pro = product.get();
            pro.setCategory(productRequest.getCategory());
            pro.setTitle(productRequest.getTitle());
            pro.setPrice(productRequest.getPrice());
            pro.setListPrice(productRequest.getListPrice());
            pro.setVendor(productRequest.getVendor());
            pro.setDiscount(productRequest.getDiscount());
            productRepo.save(pro);
        }

    }

    @Override
    public SneakersResponse searchItemByName(String productModel) {
        Optional<Sneakers> sneakers = productRepo.findByTitle(productModel);

        return sneakers.map(productMapper::mapToDto).orElse(null);

    }

    @Override
    public List<SneakersResponse> searchItemsByCategory(String category) {
        Optional<List<Sneakers>> sneakersList = productRepo.findByCategoryIn(category);

        return sneakersList.map(sneakers -> sneakers.stream().map(productMapper::mapToDto)
                .toList()).orElse(null);

    }

    @Override
    public List<SneakersResponse> getScrapedSneakers() {

        RestClient restClient = RestClient.create();

        return Arrays.stream(Objects.requireNonNull(restClient.get()
                .uri("http://localhost:8000/sneakers/sneakers-info/").retrieve()
                .body(SneakersResponse[].class))).toList();


    }

    @Override
    public List<SneakersResponse> getScrapedSneakersByCategory(String category, int pages) {

        RestClient restClient = RestClient.create();

        return Arrays.stream(Objects.requireNonNull(restClient.get()
                .uri("http://localhost:8000/sneakers/find-by-category/?category={category}&pages={pages}",
                        category, pages)
                .retrieve()
                .body(SneakersResponse[].class))).toList();

    }

    @Override
    public List<SneakersResponse> getScrapedSneakersByModelName(String model) {
        RestClient restClient = RestClient.create();

        return Arrays.stream(Objects.requireNonNull(restClient.get()
                .uri("http://localhost:8000/sneakers/find-by-model-name/{model}/", model)
                .retrieve()
                .body(SneakersResponse[].class))).toList();
    }

    @Override
    @Transactional
    public void getAndSaveScrapedSneakers() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<SneakersResponse[]> responseEntity = restTemplate.exchange(
                "http://localhost:8000/sneakers/sneakers-info/",
                HttpMethod.GET, null, SneakersResponse[].class);

        List<Sneakers> sneakersResponses = Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                .map(productMapper::mapResponseToProduct)
                .toList();
        productRepo.saveAll(sneakersResponses);


    }

}
