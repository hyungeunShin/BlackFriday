package com.example.deliveryservice.dto;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.enums.DeliveryStatus;

public record DeliveryResponse(
        Long id, Long orderId, String productName, Long productCount,
        String address, Long referenceCode, DeliveryStatus status) {
    public DeliveryResponse(Delivery delivery) {
        this(delivery.getId(), delivery.getOrderId(), delivery.getProductName(), delivery.getProductCount(),
                delivery.getAddress(), delivery.getReferenceCode(), delivery.getStatus());
    }
}
