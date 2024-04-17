package com.fiap.payments.infrastructure.adapter;

import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.infrastructure.model.PaymentModel;
import com.fiap.payments.infrastructure.persistence.PaymentJpaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentPersistenceAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentRepository;

    @Override
    public Optional<PaymentModel> getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public PaymentModel createPayment(String orderId, BigDecimal orderPrice) {
        var model = PaymentModel.builder()
            .orderId(orderId)
            .orderPrice(orderPrice)
            .status(PaymentStatus.CREATED)
            .build();
        paymentRepository.save(model);
        return model;
    }

    @Override
    public void updatePayment(String paymentId, PaymentStatus status) {
        var model = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        model.setStatus(status);
        paymentRepository.save(model);
    }
}
