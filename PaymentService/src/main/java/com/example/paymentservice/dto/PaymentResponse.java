package com.example.paymentservice.dto;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.enums.PaymentMethodType;
import com.example.paymentservice.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long amountKRW;
    private PaymentMethodType paymentMethodType;
    private String paymentData;
    private PaymentStatus paymentStatus;
    private Long referenceCode;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUserId();
        this.orderId = payment.getOrderId();
        this.amountKRW = payment.getAmountKRW();
        this.paymentMethodType = payment.getPaymentMethodType();
        this.paymentData = payment.getPaymentData();
        this.paymentStatus = payment.getPaymentStatus();
        this.referenceCode = payment.getReferenceCode();
    }
}
