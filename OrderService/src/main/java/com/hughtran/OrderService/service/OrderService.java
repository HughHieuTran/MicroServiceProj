package com.hughtran.OrderService.service;

import com.hughtran.OrderService.model.OrderRequest;
import com.hughtran.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
