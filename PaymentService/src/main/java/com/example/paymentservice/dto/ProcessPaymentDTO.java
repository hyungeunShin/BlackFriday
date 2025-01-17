package com.example.paymentservice.dto;

public record ProcessPaymentDTO(Long userId, Long orderId, Long amountKRW, Long paymentMethodId) {
}
