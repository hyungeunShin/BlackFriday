package com.example.orderservice.dto;

import java.util.Map;

public record StartOrderResponseDTO(Long orderId, Map<String, Object> paymentMethod, Map<String, Object> address) {
}
