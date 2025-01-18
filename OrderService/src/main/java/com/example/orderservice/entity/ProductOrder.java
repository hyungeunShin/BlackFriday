package com.example.orderservice.entity;

import com.example.orderservice.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    private Long count;

    private OrderStatus orderStatus;

    private Long paymentId;

    private Long deliveryId;

    private String deliveryAddress;

    public ProductOrder(Long userId, Long productId, Long count, OrderStatus orderStatus, Long paymentId, Long deliveryId, String deliveryAddress) {
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        this.orderStatus = orderStatus;
        this.paymentId = paymentId;
        this.deliveryId = deliveryId;
        this.deliveryAddress = deliveryAddress;
    }

//    public void updateStatus(Long paymentId, Long deliveryId, OrderStatus orderStatus) {
//        this.paymentId = paymentId;
//        this.deliveryId = deliveryId;
//        this.orderStatus = orderStatus;
//    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updatePaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void updateDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void updateDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
