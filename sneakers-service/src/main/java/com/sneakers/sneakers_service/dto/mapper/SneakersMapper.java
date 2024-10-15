package com.sneakers.sneakers_service.dto.mapper;

import com.sneakers.sneakers_service.dto.SneakersRequest;
import com.sneakers.sneakers_service.dto.SneakersResponse;
import com.sneakers.sneakers_service.model.Sneakers;
import org.springframework.stereotype.Component;

@Component
public class SneakersMapper {

    public Sneakers mapToProduct(SneakersRequest productRequest) {
        return Sneakers.builder()
                .productId(productRequest.getProductId())
                .title(productRequest.getTitle())
                .price(productRequest.getPrice())
                .listPrice(productRequest.getListPrice())
                .category(productRequest.getCategory())
                .link(productRequest.getLink())
                .imageLink(productRequest.getImageLink())
                .vendor(productRequest.getVendor())
                .quantity(productRequest.getQuantity())
                .productCode(productRequest.getProductCode())
                .discount(productRequest.getDiscount())
                .build();

    }

    public SneakersResponse mapToDto(Sneakers product) {
        return SneakersResponse.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .price(product.getPrice())
                .listPrice(product.getListPrice())
                .category(product.getCategory())
                .link(product.getLink())
                .imageLink(product.getImageLink())
                .vendor(product.getVendor())
                .quantity(product.getQuantity())
                .productCode(product.getProductCode())
                .discount(product.getDiscount())
                .build();

    }

    public Sneakers mapResponseToProduct(SneakersResponse sneakersResponse) {
        return Sneakers.builder()
                .productId(sneakersResponse.getProductId())
                .title(sneakersResponse.getTitle())
                .price(sneakersResponse.getPrice())
                .listPrice(sneakersResponse.getListPrice())
                .category(sneakersResponse.getCategory())
                .link(sneakersResponse.getLink())
                .imageLink(sneakersResponse.getImageLink())
                .vendor(sneakersResponse.getVendor())
                .quantity(sneakersResponse.getQuantity())
                .productCode(sneakersResponse.getProductCode())
                .discount(sneakersResponse.getDiscount())
                .build();
    }
}
