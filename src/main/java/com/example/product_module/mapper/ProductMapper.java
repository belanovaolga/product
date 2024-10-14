package com.example.product_module.mapper;

import com.example.product_module.model.ProductEntity;
import com.example.product_module.model.response.ProductResponse;
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
