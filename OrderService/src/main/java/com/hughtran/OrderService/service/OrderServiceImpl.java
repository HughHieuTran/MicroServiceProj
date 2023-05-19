package com.hughtran.OrderService.service;

import com.hughtran.OrderService.entity.Order;
import com.hughtran.OrderService.external.client.ProductService;
import com.hughtran.OrderService.model.OrderRequest;
import com.hughtran.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements  OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        Order order = Order
                .builder()
                .productId(orderRequest.getProductId())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);
        return order.getId();
    }
}
