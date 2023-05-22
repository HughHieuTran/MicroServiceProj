package com.hughtran.PaymentService.service;

import com.hughtran.PaymentService.model.PaymentRequest;
import com.hughtran.PaymentService.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse getPaymentDetailsByOrderId(long orderId);

    long doPayment(PaymentRequest paymentRequest);
}
