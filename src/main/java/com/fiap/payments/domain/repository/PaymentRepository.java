package com.fiap.payments.domain.repository;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.infrastructure.model.PaymentModel;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@EnableTransactionManagement
public interface PaymentRepository {

    Optional<PaymentModel> getPayment(String paymentId);

    PaymentModel createPayment(String paymentId, BigDecimal orderPrice);

    void updatePayment(String paymentId, PaymentStatus status);

    List<Payment> searchPayments(String orderId, PaymentStatus status);
}
