package com.example.orderservice.dto;

public record ProcessPaymentDTO(Long orderId, Long userId, Long amountKRW, Long paymentMethodId) {
}
