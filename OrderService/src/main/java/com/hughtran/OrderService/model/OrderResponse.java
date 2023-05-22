package com.hughtran.OrderService.model;

import com.hughtran.OrderService.external.response.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;

    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class ProductDetails {
        private String productName;
        private long productId;
        private long quantity;
        private long price;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class PaymentDetails {
        private long paymentId;
        private String paymentMode;
        private String paymentStatus;
        private Instant paymentDate;
    }
}




