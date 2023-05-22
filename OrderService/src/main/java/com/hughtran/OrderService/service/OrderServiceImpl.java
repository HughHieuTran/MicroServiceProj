package com.hughtran.OrderService.service;

import com.hughtran.OrderService.entity.Order;
import com.hughtran.OrderService.exception.CustomException;
import com.hughtran.OrderService.external.client.PaymentService;
import com.hughtran.OrderService.external.client.ProductService;
import com.hughtran.OrderService.external.response.PaymentResponse;
import com.hughtran.OrderService.model.OrderRequest;
import com.hughtran.OrderService.model.OrderResponse;
import com.hughtran.OrderService.model.ProductResponse;
import com.hughtran.OrderService.repository.OrderRepository;
import com.hughtran.OrderService.request.PaymentRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order
                .builder()
                .productId(orderRequest.getProductId())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .build();

        orderRepository.save(order);

        log.info("calling payment service to complete the payment");

        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .orderId(order.getId())
                        .paymentMode(orderRequest.getPaymentMode())
                        .amount(orderRequest.getTotalAmount())
                        .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("payment done");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("error when paying");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);

        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get details for order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("order not found for: " + orderId, "ORDER_NOT_FOUND", 404)
                );

        log.info("invoking product service to fetch the product {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        );

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .build();

        log.info("Get payment details for id: {}", orderId);

        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId()
                , PaymentResponse.class
        );

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();


        OrderResponse orderResponse = OrderResponse.builder()
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();



        return orderResponse;
    }
}
