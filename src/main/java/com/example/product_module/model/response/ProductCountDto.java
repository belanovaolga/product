package com.example.product_module.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCountDto {
    private Long productId;
    private Long count;
    private Boolean deleteProduct;
}
