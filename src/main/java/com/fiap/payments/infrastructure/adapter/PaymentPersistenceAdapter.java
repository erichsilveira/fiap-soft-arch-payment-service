package com.fiap.payments.infrastructure.adapter;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.infrastructure.model.PaymentModel;
import com.fiap.payments.infrastructure.persistence.PaymentJpaRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Payment> searchPayments(String orderId, PaymentStatus status) {
        var filter = PaymentModel.builder();
        if (StringUtils.isNotBlank(orderId)) {
            filter.orderId(orderId);
        }
        if (status != null) {
            filter.status(status);
        }
        var entities = paymentRepository.findAll(Example.of(filter.build()),
                Sort.by(Sort.Direction.DESC, "id"));
        return entities.stream()
                .map(PaymentModel::toPayment).toList();
    }
}
