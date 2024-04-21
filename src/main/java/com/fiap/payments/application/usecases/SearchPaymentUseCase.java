package com.fiap.payments.application.usecases;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.interfaces.dto.SearchPaymentCommand;

import java.util.List;

public interface SearchPaymentUseCase {


    List<Payment> searchPayments(SearchPaymentCommand command);

    Payment searchPaymentById(String paymentId) throws ResourceNotFoundException;
}
