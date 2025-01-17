package com.example.paymentservice.dto;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.enums.PaymentMethodType;
import com.example.paymentservice.enums.PaymentStatus;

public record PaymentResponse(
        Long id,
        Long userId,
        Long orderId,
        Long amountKRW,
        PaymentMethodType paymentMethodType,
        String paymentData,
        PaymentStatus paymentStatus,
        Long referenceCode
) {
    public PaymentResponse(Payment payment) {
        this(payment.getId(), payment.getUserId(), payment.getOrderId(), payment.getAmountKRW(), payment.getPaymentMethodType(), payment.getPaymentData(), payment.getPaymentStatus(), payment.getReferenceCode());
    }
}
