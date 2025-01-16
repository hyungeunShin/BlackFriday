package com.example.deliveryservice.entity;

import com.example.deliveryservice.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "idx_orderId", columnList = "orderId")})
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String productName;

    private Long productCount;

    private String address;

    private Long referenceCode;

    private DeliveryStatus status;

    public Delivery(Long orderId, String productName, Long productCount, String address, Long referenceCode, DeliveryStatus status) {
        this.orderId = orderId;
        this.productName = productName;
        this.productCount = productCount;
        this.address = address;
        this.referenceCode = referenceCode;
        this.status = status;
    }

    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }
}
