package com.example.product_module.controller;

import com.example.product_module.model.request.IdRequest;
import com.example.product_module.model.request.ProductCreateRequest;
import com.example.product_module.model.response.PersonalOfferResponse;
import com.example.product_module.model.response.ProductListResponse;
import com.example.product_module.model.response.ProductResponse;
import com.example.product_module.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductCreateRequest productCreateRequest
            ) {
        return ResponseEntity.ok(productService.createProduct(productCreateRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        ResponseEntity.ok();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<ProductListResponse> getProductList() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @PostMapping("/twoProductsPO")
    public ResponseEntity<PersonalOfferResponse> getPersonalOffer(
            @RequestBody IdRequest productIdList
    ) {
        return ResponseEntity.ok(productService.getPersonalOffer(productIdList));
    }

    @GetMapping("/poForNoOrders")
    public PersonalOfferResponse getPOForNoOrders() {
        return productService.getPOForNoOrders();
    }
}
