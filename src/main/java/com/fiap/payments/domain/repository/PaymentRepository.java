package com.fiap.payments.domain.repository;

import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.infrastructure.model.PaymentModel;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public interface PaymentRepository {

    Optional<PaymentModel> getPayment(String paymentId);

    PaymentModel createPayment(String paymentId, BigDecimal orderPrice);

    void updatePayment(String paymentId, PaymentStatus status);
}
