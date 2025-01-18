package com.example.orderservice.dto;

public record FinishOrderDTO(Long orderId, Long paymentMethodId, Long addressId) {
}
