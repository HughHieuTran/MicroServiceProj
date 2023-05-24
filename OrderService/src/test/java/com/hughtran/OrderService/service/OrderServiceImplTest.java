package com.hughtran.OrderService.service;

import com.hughtran.OrderService.entity.Order;
import com.hughtran.OrderService.exception.CustomException;
import com.hughtran.OrderService.external.client.PaymentService;
import com.hughtran.OrderService.external.client.ProductService;
import com.hughtran.OrderService.external.response.PaymentResponse;
import com.hughtran.OrderService.model.OrderRequest;
import com.hughtran.OrderService.model.OrderResponse;
import com.hughtran.OrderService.model.PaymentMode;
import com.hughtran.OrderService.model.ProductResponse;
import com.hughtran.OrderService.repository.OrderRepository;
import com.hughtran.OrderService.request.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get order success scenario")
    @Test
    void test_When_Order_Success() {
        Order order = getMockOrder();
        when(orderRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                        ProductResponse.class
                )
        ).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/order/" + order.getId()
                        , PaymentResponse.class
                )
        ).thenReturn(getMockPaymentResponse());


        OrderResponse orderResponse = orderService.getOrderDetails(1);

        verify(orderRepository, times(1))
                .findById(ArgumentMatchers.anyLong());
        verify(restTemplate, times(1))
                .getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                        ProductResponse.class
                );
        verify(restTemplate, times(1))
                .getForObject(
                        "http://PAYMENT-SERVICE/payment/order/" + order.getId()
                        , PaymentResponse.class
                );

        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(orderResponse.getOrderId(), order.getId());

    }

    @DisplayName("Get order failed scenario")
    @Test
    void test_when_order_not_found() {
        when(orderRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        CustomException customException = Assertions.assertThrows(
                CustomException.class,
                () -> orderService.getOrderDetails(1)
        );
        assertEquals("ORDER_NOT_FOUND", customException.getErrorCode());
        assertEquals(404, customException.getStatus());

        verify(orderRepository, times(1))
                .findById(ArgumentMatchers.anyLong());

    }

    @DisplayName("place order success scenario")
    @Test
    void test_when_place_order_success() {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);
        assertEquals(orderId, order.getId());
    }

    @DisplayName("place order fail scenario")
    @Test
    void test_when_place_order_fail() {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = orderService.placeOrder(orderRequest);
        assertEquals(orderId, order.getId());

    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .productId(1)
                .totalAmount(10)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .orderId(1)
                .paymentMode(String.valueOf(PaymentMode.CASH))
                .paymentDate(Instant.now())
                .amount(10)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productName("apple")
                .price(100)
                .productId(1)
                .quantity(10)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .id(1)
                .productId(1)
                .quantity(1)
                .amount(10)
                .orderDate(Instant.now())
                .orderStatus("PLACED")
                .build();
    }


}