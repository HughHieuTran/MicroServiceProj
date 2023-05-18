package com.hughtran.ProductService.service;

import com.hughtran.ProductService.ProductServiceApplication;
import com.hughtran.ProductService.entity.Product;
import com.hughtran.ProductService.exception.ProductServiceCustomException;
import com.hughtran.ProductService.model.ProductRequest;
import com.hughtran.ProductService.model.ProductResponse;
import com.hughtran.ProductService.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

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
        productRepository.save(product);
        return product.getProductId();
    }

    @Override
    public ResponseEntity<Product> getAllProducts() {
        return null;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("product not found with id","PRODUCT_NOT_FOUND"  ));

        ProductResponse productResponse = new ProductResponse();

        BeanUtils.copyProperties(product,productResponse);

        return productResponse;
    }
}
