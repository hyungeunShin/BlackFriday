package com.example.paymentservice.dto;

import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.enums.PaymentMethodType;

public record PaymentMethodResponse(Long id, Long userId, PaymentMethodType paymentMethodType, String creditCardNumber) {
    public PaymentMethodResponse(PaymentMethod paymentMethod) {
        this(paymentMethod.getId(), paymentMethod.getUserId(), paymentMethod.getPaymentMethodType(), paymentMethod.getCreditCardNumber());
    }
}
