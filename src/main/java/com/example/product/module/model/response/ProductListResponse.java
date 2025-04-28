package com.example.product.module.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListResponse {
    private List<ProductResponse> productResponseList;
}
