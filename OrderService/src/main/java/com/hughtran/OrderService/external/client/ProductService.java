package com.hughtran.OrderService.external.client;

import com.hughtran.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE/product")
@CircuitBreaker(name = "external",fallbackMethod = "fallback")
public interface ProductService {
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId, @RequestParam long quantity
    );
    default void fallback(Exception e){
        throw new CustomException("product service is not active","UNAVAILABLE",500);
    }
}
