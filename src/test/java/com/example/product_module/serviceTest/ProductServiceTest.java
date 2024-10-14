package com.example.product_module.serviceTest;

import com.example.product_module.exception.ProductNotFound;
import com.example.product_module.mapper.ProductMapper;
import com.example.product_module.model.ProductEntity;
import com.example.product_module.model.request.ProductCreateRequest;
import com.example.product_module.model.response.ProductListResponse;
import com.example.product_module.model.response.ProductResponse;
import com.example.product_module.repository.ProductRepository;
import com.example.product_module.service.ProductService;
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
    void shouldGetCount() {
        Optional<ProductEntity> optionalProduct = Optional.ofNullable(product1);
        assert product1 != null;
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);

        ProductResponse actualProductRecord = productService.findProductById(product1.getId());

        assertEquals(product1.getCount(), actualProductRecord.getCount());
    }

    @Test
    void shouldGetName() {
        Optional<ProductEntity> optionalProduct = Optional.ofNullable(product1);
        assert product1 != null;
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);

        ProductResponse actualProductRecord = productService.findProductById(product1.getId());

        assertEquals(product1.getName(), actualProductRecord.getName());
    }

    @Test
    void shouldGetCurrentPrice() {
        Optional<ProductEntity> optionalProduct = Optional.ofNullable(product1);
        assert product1 != null;
        Mockito.when(mockProductRepository.findById(product1.getId())).thenReturn(optionalProduct);

        ProductResponse actualProductRecord = productService.findProductById(product1.getId());

        assertEquals(product1.getCurrentPrice(), actualProductRecord.getCurrentPrice());
    }
}
