package com.fiap.payments.application.usecases;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.exception.ResourceNotFoundException;

public interface SearchPaymentUseCase {

    Payment searchPayment(String paymentId) throws ResourceNotFoundException;
}
