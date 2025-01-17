package com.example.deliveryservice.dto;

public record ProcessDeliveryDTO(Long orderId, String productName, Long productCount, String address) {
}
