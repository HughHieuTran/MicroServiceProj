package com.hughtran.OrderService.request;

import com.hughtran.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private long amount;
    private long orderId;
    private String referenceNumber;

    private PaymentMode paymentMode;
}
