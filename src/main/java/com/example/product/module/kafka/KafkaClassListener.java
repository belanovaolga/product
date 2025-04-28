package com.example.product.module.kafka;

import com.example.product.module.model.response.ProductCountDto;
import com.example.product.module.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaClassListener {
    private final ProductService productService;

    @KafkaListener(topics = "product-count-topic",
    containerFactory = "productCountKafkaListenerContainerFactory")
    void listener(ProductCountDto productCountDto) {
        productService.updateProductCount(productCountDto);
    }
}
