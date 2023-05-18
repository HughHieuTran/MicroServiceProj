package com.hughtran.ProductService.service;


import com.hughtran.ProductService.entity.Product;
import com.hughtran.ProductService.model.ProductRequest;
import com.hughtran.ProductService.model.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ResponseEntity<Product> getAllProducts();

    ProductResponse getProductById(Long id);
}
