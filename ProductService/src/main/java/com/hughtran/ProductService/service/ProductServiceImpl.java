package com.hughtran.ProductService.service;

import com.hughtran.ProductService.entity.Product;
import com.hughtran.ProductService.model.ProductRequest;
import com.hughtran.ProductService.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("adding product");
        Product product = Product.builder()
                .price(productRequest.getPrice())
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .build();
        return productRepository.save(product).getProductId();

    }
}
