package com.example.product.module.service;

import com.example.product.module.exception.ProductNotFound;
import com.example.product.module.mapper.ProductMapper;
import com.example.product.module.model.ProductEntity;
import com.example.product.module.model.request.IdRequest;
import com.example.product.module.model.request.PersonalOfferListRequest;
import com.example.product.module.model.request.ProductCreateRequest;
import com.example.product.module.model.response.*;
import com.example.product.module.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
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
        if (productIdList.getProductIdList().isEmpty())
            return getPOForNoOrders();

        List<ProductEntity> productEntityList = findAll();

        List<ProductEntity> productEntities = productEntityList.stream()
                .filter(product -> !productIdList.getProductIdList().contains(product.getId()))
                .toList();

        return twoMaxCountProducts(productEntities);
    }

    public PersonalOfferResponse getPOForNoOrders() {
        return twoMaxCountProducts(findAll());
    }

    @Transactional
    public void updateProductCount(ProductCountDto productCountDto) {
        ProductEntity product = findById(productCountDto.getProductId());

        Long currentCount = product.getCount();
        Long updateCount = productCountDto.getCount();

        if (Boolean.TRUE.equals(productCountDto.getDeleteProduct())) {
            currentCount -= updateCount;
        } else {
            currentCount += updateCount;
        }

        product.setCount(currentCount);
        productRepository.save(product);
    }

    public PersonalOfferListResponse getPersonalOfferList(PersonalOfferListRequest personalOfferListRequest) {
        List<ProductEntity> productEntityList = findAll();
        Map<Long, ProductEntity> productIdEmailMap = new HashMap<>();
        productEntityList.forEach(productEntity -> productIdEmailMap.put(productEntity.getId(), productEntity));

        List<PersonalOfferForEmployee> personalOfferForEmployeeList = personalOfferListRequest.getPersonalOfferDataList().stream()
                .map(personalOfferData -> {
                    List<ProductEntity> personalOfferList = new ArrayList<>();
                    personalOfferData.getProductIdList()
                            .forEach(productId ->
                                    personalOfferList.add(productIdEmailMap.get(productId)));

                    PersonalOfferResponse personalOfferResponse = PersonalOfferResponse.builder().personalOfferList(personalOfferList).build();
                    return PersonalOfferForEmployee.builder().employeeId(personalOfferData.getEmployeeId()).personalOfferResponse(personalOfferResponse).build();
                }).toList();

        return PersonalOfferListResponse.builder().personalOfferForEmployeeList(personalOfferForEmployeeList).build();
    }


    private ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFound::new);
    }

    private List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

    private PersonalOfferResponse twoMaxCountProducts(List<ProductEntity> productEntityList) {
        if (productEntityList.isEmpty()) {
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
