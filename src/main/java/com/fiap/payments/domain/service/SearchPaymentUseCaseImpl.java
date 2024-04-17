package com.fiap.payments.domain.service;

import com.fiap.payments.application.usecases.SearchPaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.infrastructure.model.PaymentModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class SearchPaymentUseCaseImpl implements SearchPaymentUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment searchPayment(String paymentId) throws ResourceNotFoundException {
        log.info("Searching payment with id {}", paymentId);
        PaymentModel paymentModel = paymentRepository.getPayment(paymentId)
            .orElseThrow(ResourceNotFoundException::new);

        return PaymentModel.toPayment(paymentModel);
    }
}
