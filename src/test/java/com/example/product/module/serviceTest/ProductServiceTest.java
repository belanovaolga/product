package com.example.product.module.serviceTest;

import com.example.product.module.model.request.IdRequest;
import com.example.product.module.model.request.ProductCreateRequest;
import com.example.product.module.model.response.ProductCountDto;
import com.example.product.module.model.response.ProductResponse;
import com.example.product.module.exception.ProductNotFound;
import com.example.product.module.mapper.ProductMapper;
import com.example.product.module.model.ProductEntity;
import com.example.product.module.model.response.PersonalOfferResponse;
import com.example.product.module.model.response.ProductListResponse;
import com.example.product.module.repository.ProductRepository;
import com.example.product.module.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {
    private final ProductService productService;
    private final ProductRepository mockProductRepository;
    private final ProductMapper productMapper;
    private final ProductEntity product1;
    private final ProductEntity product2;
    private final ProductEntity productNew;

    public ProductServiceTest() {
        mockProductRepository = Mockito.mock(ProductRepository.class);
        productMapper = new ProductMapper();
        productService = new ProductService(mockProductRepository, productMapper);

        product1 = ProductEntity.builder().id(1L).name("lemon").description("yellow, sour").count(65L).currentPrice(87.5).build();
        product2 = ProductEntity.builder().id(2L).name("kiwi").description("like potatoes").count(43L).currentPrice(118.74).build();
        productNew = ProductEntity.builder().id(3L).name("apple").description("gold apple, sweet").count(86L).currentPrice(98.57).build();
    }

    @Test
    void shouldCreateProduct() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("apple", "gold apple, sweet", 86L, 98.57);
        Mockito.when(mockProductRepository.save(
                ProductEntity.builder()
                .name(productCreateRequest.getName())
                .description(productCreateRequest.getDescription())
                .count(productCreateRequest.getCount())
                .currentPrice(productCreateRequest.getCurrentPrice())
                .build())
        ).thenReturn(productNew);
        ProductResponse productResponse = productMapper.toProductResponse(productNew);

        ProductResponse actualProductRecord = productService.createProduct(productCreateRequest);

        assertEquals(productResponse, actualProductRecord);
    }

    @Test
    void shouldDeleteProduct() {
        Optional<ProductEntity> optionalProduct = Optional.ofNullable(product1);
        assert product1 != null;
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);
        Mockito.doNothing().when(mockProductRepository).delete(product1);
        productService.deleteProduct(product1.getId());

        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFound.class, () -> {
            productService.findProductById(product1.getId());
        });
    }

    @Test
    void shouldFindProductById() {
        Optional<ProductEntity> optionalProduct = Optional.ofNullable(product1);
        assert product1 != null;
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);
        ProductResponse productResponse = productMapper.toProductResponse(product1);

        ProductResponse actualProductRecord = productService.findProductById(product1.getId());

        assertEquals(productResponse, actualProductRecord);
    }

    @Test
    void shouldFindProductById_whenProductNotFound() {
        Mockito.when(mockProductRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFound.class, () -> {
            productService.findProductById(20L);
        });
    }

    @Test
    void shouldFindAllProducts() {
        List<ProductEntity> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        List<ProductResponse> expectedProductResponseList = productList.stream().map(productMapper::toProductResponse).toList();

        Mockito.when(mockProductRepository.findAll()).thenReturn(productList);

        ProductListResponse actualProductListResponse = productService.findAllProducts();

        assertEquals(expectedProductResponseList, actualProductListResponse.getProductResponseList());
    }

    @Test
    void shouldGetPersonalOffer_whenOneOrder() {
        List<ProductEntity> productResponseList = new ArrayList<>();
        productResponseList.add(product1);
        PersonalOfferResponse expectedPersonalOffer = PersonalOfferResponse.builder().personalOfferList(productResponseList).build();

        List<Long> productIdList = new ArrayList<>();
        productIdList.add(product2.getId());
        productIdList.add(productNew.getId());
        IdRequest idRequest = IdRequest.builder().productIdList(productIdList).build();
        List<ProductEntity> allProductEntityList = new ArrayList<>();
        allProductEntityList.add(product1);
        allProductEntityList.add(product2);
        allProductEntityList.add(productNew);
        Mockito.when(mockProductRepository.findAll()).thenReturn(allProductEntityList);

        PersonalOfferResponse actualPersonalOffer = productService.getPersonalOffer(idRequest);

        assertEquals(expectedPersonalOffer, actualPersonalOffer);
    }

    @Test
    void shouldGetPersonalOffer_whenTwoOrders() {
        List<ProductEntity> productResponseList = new ArrayList<>();
        productResponseList.add(product1);
        productResponseList.add(product2);
        PersonalOfferResponse expectedPersonalOffer = PersonalOfferResponse.builder().personalOfferList(productResponseList).build();

        List<Long> productIdList = new ArrayList<>();
        productIdList.add(productNew.getId());
        IdRequest idRequest = IdRequest.builder().productIdList(productIdList).build();
        List<ProductEntity> allProductEntityList = new ArrayList<>();
        allProductEntityList.add(product1);
        allProductEntityList.add(product2);
        allProductEntityList.add(productNew);
        Mockito.when(mockProductRepository.findAll()).thenReturn(allProductEntityList);

        PersonalOfferResponse actualPersonalOffer = productService.getPersonalOffer(idRequest);

        assertEquals(expectedPersonalOffer, actualPersonalOffer);
    }

    @Test
    void shouldGetPOForNoOrders() {
        List<ProductEntity> productResponseList = new ArrayList<>();
        productResponseList.add(productNew);
        productResponseList.add(product1);
        PersonalOfferResponse expectedPersonalOffer = PersonalOfferResponse.builder().personalOfferList(productResponseList).build();

        List<ProductEntity> allProductEntityList = new ArrayList<>();
        allProductEntityList.add(product1);
        allProductEntityList.add(product2);
        allProductEntityList.add(productNew);
        Mockito.when(mockProductRepository.findAll()).thenReturn(allProductEntityList);

        PersonalOfferResponse actualPersonalOffer = productService.getPOForNoOrders();

        assertEquals(expectedPersonalOffer, actualPersonalOffer);
    }


    @Test
    void shouldUpdateProductCount_whenDeleteProduct() {
        ProductCountDto productCountDto = ProductCountDto.builder().productId(product1.getId()).count(2L).deleteProduct(true).build();

        ProductEntity updatedProduct = ProductEntity.builder().id(1L).name("lemon").description("yellow, sour").count(63L).currentPrice(87.5).build();
        Optional<ProductEntity> optionalProduct = Optional.of(product1);
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);
        Mockito.when(mockProductRepository.save(updatedProduct)).thenReturn(updatedProduct);

        productService.updateProductCount(productCountDto);

        assertEquals(product1.getCount(), updatedProduct.getCount());
    }

    @Test
    void shouldUpdateProductCount_whenNotDeleteProduct() {
        ProductCountDto productCountDto = ProductCountDto.builder().productId(product1.getId()).count(2L).deleteProduct(false).build();

        ProductEntity updatedProduct = ProductEntity.builder().id(1L).name("lemon").description("yellow, sour").count(67L).currentPrice(87.5).build();
        Optional<ProductEntity> optionalProduct = Optional.of(product1);
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);
        Mockito.when(mockProductRepository.save(updatedProduct)).thenReturn(updatedProduct);

        productService.updateProductCount(productCountDto);

        assertEquals(product1.getCount(), updatedProduct.getCount());
    }
}
