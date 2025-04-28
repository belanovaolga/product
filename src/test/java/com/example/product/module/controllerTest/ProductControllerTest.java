package com.example.product.module.controllerTest;

import com.example.product.module.model.request.IdRequest;
import com.example.product.module.model.request.ProductCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void shouldCreateProduct() {
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder().name("apple").description("gold apple").count(25L).currentPrice(104.99).build();

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8000/product")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequest)))
                .andExpect(status().isOk())
//      нужно МЕНЯТЬ ЦИФРУ при каждом последующем запуске теста
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.name").value("apple"))
                .andExpect(jsonPath("$.description").value("gold apple"))
                .andExpect(jsonPath("$.count").value(25))
                .andExpect(jsonPath("$.currentPrice").value(104.99));
    }

    @Test
    @SneakyThrows
    void shouldDeleteProduct() {
//      нужно МЕНЯТЬ ЦИФРУ при каждом последующем запуске теста
        Long productId = 5L;

        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8000/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldGetProductById() {
//      нужно МЕНЯТЬ ЦИФРУ при каждом последующем запуске теста
        Long productId = 5L;

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8000/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
//      нужно МЕНЯТЬ ЦИФРУ при каждом последующем запуске теста
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("apple"))
                .andExpect(jsonPath("$.description").value("gold apple"))
                .andExpect(jsonPath("$.count").value(25))
                .andExpect(jsonPath("$.currentPrice").value(104.99));
    }

    @Test
    @SneakyThrows
    void shouldGetProductById_whenProductNotFound() {
        Long productId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8000/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldGetProductList() {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8000/product/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }


    @Test
    @SneakyThrows
    void shouldGetPersonalOffer() {
        List idList = new ArrayList();
        idList.add(4L);
        idList.add(5L);
        idList.add(6L);
        IdRequest idRequest = IdRequest.builder().productIdList(idList).build();

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8000/product/twoProductsPO")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldGetPOForNoOrders() {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8000/product/poForNoOrders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}
