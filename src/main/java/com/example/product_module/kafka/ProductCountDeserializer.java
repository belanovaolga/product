package com.example.product_module.kafka;

import com.example.product_module.model.response.ProductCountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class ProductCountDeserializer implements Deserializer<ProductCountDto> {
    private final ObjectMapper objectMapper;

    public ProductCountDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ProductCountDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), ProductCountDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to ProductCountDto");
        }
    }
}
