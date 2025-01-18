package com.example.orderservice.dto;

import com.example.orderservice.entity.ProductOrder;
import com.example.orderservice.enums.OrderStatus;

public record ProductOrderResponse(
        Long id, Long userId, Long productId, Long count,
        OrderStatus orderStatus, Long paymentId, Long deliveryId, String deliveryAddress
) {
    public ProductOrderResponse(ProductOrder order) {
        this(order.getId(), order.getUserId(), order.getProductId(), order.getCount(), order.getOrderStatus(), order.getPaymentId(), order.getDeliveryId(), order.getDeliveryAddress());
    }
}
