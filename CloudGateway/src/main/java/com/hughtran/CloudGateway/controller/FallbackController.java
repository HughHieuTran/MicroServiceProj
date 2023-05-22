package com.hughtran.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallback(){
        return "Order service is down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallback(){
        return "product service is down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallback(){
        return "payment service is down";
    }

}
