package com.example.product.module.mapper;

import com.example.product.module.model.response.ProductResponse;
import com.example.product.module.model.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(ProductEntity productEntity) {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .count(productEntity.getCount())
                .currentPrice(productEntity.getCurrentPrice())
                .build();
    }
}
