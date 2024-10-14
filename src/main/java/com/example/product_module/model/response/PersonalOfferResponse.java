package com.example.product_module.model.response;

import com.example.product_module.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalOfferResponse {
    List<ProductEntity> personalOfferList;
}
