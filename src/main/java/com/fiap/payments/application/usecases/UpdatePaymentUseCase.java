package com.fiap.payments.application.usecases;

import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.exception.ResourceNotFoundException;

public interface UpdatePaymentUseCase {

    void paymentWebhook(String paymentId, boolean success);

    void updatePayment(String payment, PaymentStatus status) throws ResourceNotFoundException;
}
