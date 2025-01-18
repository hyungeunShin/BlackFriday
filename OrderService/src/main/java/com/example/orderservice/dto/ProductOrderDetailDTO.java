package com.example.orderservice.dto;

import com.example.orderservice.enums.OrderStatus;

public record ProductOrderDetailDTO(
        Long id, Long userId, Long productId, Long paymentId,
        Long deliveryId, OrderStatus orderStatus, String paymentStatus, String deliveryStatus
) {
}
