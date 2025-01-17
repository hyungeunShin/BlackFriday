package com.example.paymentservice.dto;

import com.example.paymentservice.enums.PaymentMethodType;

public record RegisterPaymentMethodDTO(Long userId, PaymentMethodType paymentMethodType, String creditCardNumber) {
}
