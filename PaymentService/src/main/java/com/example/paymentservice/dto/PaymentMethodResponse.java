package com.example.paymentservice.dto;

import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.enums.PaymentMethodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodResponse {
    private Long id;
    private Long userId;
    private PaymentMethodType paymentMethodType;
    private String creditCardNumber;

    public PaymentMethodResponse(PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.userId = paymentMethod.getUserId();
        this.paymentMethodType = paymentMethod.getPaymentMethodType();
        this.creditCardNumber = paymentMethod.getCreditCardNumber();
    }
}
