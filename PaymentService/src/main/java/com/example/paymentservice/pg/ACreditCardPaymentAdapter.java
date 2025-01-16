package com.example.paymentservice.pg;

import org.springframework.stereotype.Service;

@Service
public class ACreditCardPaymentAdapter implements CreditCardPaymentAdapter {
    @Override
    public Long processCreditCardPayment(Long amountKRW, String creditCardNumber) {
        return Math.round(Math.random() * 100000000);
    }
}
