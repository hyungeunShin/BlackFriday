package com.example.paymentservice.dto;

import com.example.paymentservice.enums.PaymentMethodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPaymentMethodDTO {
    private Long userId;
    private PaymentMethodType paymentMethodType;
    private String creditCardNumber;
}
