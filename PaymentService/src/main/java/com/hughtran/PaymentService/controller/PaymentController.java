package com.hughtran.PaymentService.controller;

import com.hughtran.PaymentService.model.PaymentRequest;
import com.hughtran.PaymentService.model.PaymentResponse;
import com.hughtran.PaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(
                paymentService.doPayment(paymentRequest), HttpStatus.OK
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId) {
        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderId(orderId);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}
