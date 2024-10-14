package com.example.product_module.service;

import com.example.product_module.exception.ProductNotFound;
import com.example.product_module.mapper.ProductMapper;
import com.example.product_module.model.ProductEntity;
import com.example.product_module.model.request.IdRequest;
import com.example.product_module.model.request.ProductCreateRequest;
import com.example.product_module.model.response.PersonalOfferResponse;
import com.example.product_module.model.response.ProductListResponse;
import com.example.product_module.model.response.ProductResponse;
import com.example.product_module.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        ProductEntity productEntity = productRepository.save(
                ProductEntity.builder()
                        .name(productCreateRequest.getName())
                        .description(productCreateRequest.getDescription())
                        .count(productCreateRequest.getCount())
                        .currentPrice(productCreateRequest.getCurrentPrice())
                        .build()
        );

        return productMapper.toProductResponse(productEntity);
    }

    public void deleteProduct(Long id) {
        ProductEntity product = findById(id);
        productRepository.delete(product);
    }

    public ProductResponse findProductById(Long id) {
        ProductEntity product = findById(id);
        return productMapper.toProductResponse(product);
    }

    public ProductListResponse findAllProducts() {
        List<ProductEntity> productEntityList = findAll();

        List<ProductResponse> productResponseList = productEntityList.stream()
                .map(productMapper::toProductResponse)
                .toList();

        return ProductListResponse.builder()
                .productResponseList(productResponseList)
                .build();
    }

    public PersonalOfferResponse getPersonalOffer(IdRequest productIdList) {
        List<ProductEntity> productEntityList = findAll();

        List<ProductEntity> productEntities = productEntityList.stream()
                .filter(product -> !productIdList.getProductIdList().contains(product.getId()))
                .toList();

        return twoMaxCountProducts(productEntities);
    }

    public PersonalOfferResponse getPOForNoOrders() {
        return twoMaxCountProducts(findAll());
    }

    private ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFound::new);
    }

    private List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

    private PersonalOfferResponse twoMaxCountProducts(List<ProductEntity> productEntityList) {
        if(productEntityList.isEmpty()) {
            return PersonalOfferResponse.builder().build();
        }

        List<ProductEntity> personalOffer = productEntityList.stream()
                .sorted(Comparator.comparingLong(ProductEntity::getCount).reversed())
                .limit(2)
                .map(product -> {
                            product.setCurrentPrice(product.getCurrentPrice() * 0.7);
                            return product;
                        }
                ).toList();

        return PersonalOfferResponse.builder()
                .personalOfferList(personalOffer)
                .build();
    }
}
