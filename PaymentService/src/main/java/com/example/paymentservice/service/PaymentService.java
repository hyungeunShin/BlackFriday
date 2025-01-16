package com.example.paymentservice.service;

import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.enums.PaymentMethodType;
import com.example.paymentservice.enums.PaymentStatus;
import com.example.paymentservice.pg.CreditCardPaymentAdapter;
import com.example.paymentservice.repository.PaymentMethodRepository;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final CreditCardPaymentAdapter creditCardPaymentAdapter;

    @Transactional
    public PaymentMethod registerPaymentMethod(Long userId, PaymentMethodType paymentMethodType, String creditCardNumber) {
        return paymentMethodRepository.save(new PaymentMethod(userId, paymentMethodType, creditCardNumber));
    }

    @Transactional
    public Payment processPayment(Long userId, Long orderId, Long amountKRW, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow();

        if(paymentMethod.getPaymentMethodType() != PaymentMethodType.CREDIT_CARD) {
            throw new IllegalStateException("지원하지 않는 결제 수단입니다.");
        }

        //외부 시스템으로 결제 진행
        Long referenceCode = creditCardPaymentAdapter.processCreditCardPayment(amountKRW, paymentMethod.getCreditCardNumber());

        return paymentRepository.save(
                new Payment(userId, orderId, amountKRW, paymentMethod.getPaymentMethodType()
                        , paymentMethod.getCreditCardNumber(), PaymentStatus.COMPLETED, referenceCode)
        );
    }

    public List<PaymentMethod> getPaymentMethod(Long userId) {
        return paymentMethodRepository.findByUserId(userId);
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow();
    }
}
