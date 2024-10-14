package com.example.product_module.controller;

import com.example.product_module.exception.ProductNotFound;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(ProductNotFound.class)
    public ResponseStatusException productNotFound(ProductNotFound productNotFound) {
        return new ResponseStatusException(HttpStatusCode.valueOf(404), "The product does not exist");
    }
}
