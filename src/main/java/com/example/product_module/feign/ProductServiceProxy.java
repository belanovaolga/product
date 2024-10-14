package com.example.product_module.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="order-service", url="localhost:8080")
public class ProductServiceProxy {
}
