package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentMethodResponse;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.ProcessPaymentDTO;
import com.example.paymentservice.dto.RegisterPaymentMethodDTO;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/registerMethod")
    public PaymentMethodResponse registerPaymentMethod(@RequestBody RegisterPaymentMethodDTO dto) {
        return new PaymentMethodResponse(paymentService.registerPaymentMethod(dto.userId(), dto.paymentMethodType(), dto.creditCardNumber()));
    }

    @PostMapping("/process")
    public PaymentResponse processPayment(@RequestBody ProcessPaymentDTO dto) {
        return new PaymentResponse(paymentService.processPayment(dto.userId(), dto.orderId(), dto.amountKRW(), dto.paymentMethodId()));
    }

    @GetMapping("/{userId}/paymentMethod")
    public List<PaymentMethodResponse> getPaymentMethod(@PathVariable("userId") Long userId) {
        return paymentService.getPaymentMethod(userId).stream().map(PaymentMethodResponse::new).toList();
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@PathVariable("paymentId") Long paymentId) {
        return new PaymentResponse(paymentService.getPayment(paymentId));
    }
}
