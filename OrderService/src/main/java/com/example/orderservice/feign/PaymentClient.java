package com.example.orderservice.feign;

import com.example.orderservice.dto.ProcessPaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "paymentClient", url = "http://payment-service:8080")
public interface PaymentClient {
    @GetMapping(value = "/payments/users/{userId}/paymentMethod/first-method")
    Map<String, Object> getPaymentMethod(@PathVariable("userId") Long userId);

    @PostMapping(value = "/payments/process")
    Map<String, Object> processPayment(@RequestBody ProcessPaymentDTO dto);

    @GetMapping(value = "/payments/{paymentId}")
    Map<String, Object> getPayment(@PathVariable("paymentId") Long paymentId);
}
