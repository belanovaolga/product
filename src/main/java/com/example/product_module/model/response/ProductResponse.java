package com.example.product_module.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long count;
    private Double currentPrice;
}
