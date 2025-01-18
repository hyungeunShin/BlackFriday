package com.example.orderservice.dto;

public record ProcessDeliveryDTO(Long orderId, String productName, Long productCount, String address) {
}
