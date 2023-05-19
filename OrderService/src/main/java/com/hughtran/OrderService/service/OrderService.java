package com.hughtran.OrderService.service;

import com.hughtran.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
