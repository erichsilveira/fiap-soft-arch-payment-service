package com.fiap.payments.domain.service;

import com.fiap.payments.application.usecases.SubmitPaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentEvent;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.infrastructure.model.PaymentModel;
import com.fiap.payments.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubmitPaymentUseCaseImpl implements SubmitPaymentUseCase {

    private final PaymentRepository paymentRepository;

    // only to simulate payment webhook
    private final UpdatePaymentUseCaseImpl updatePaymentUseCase;

    private final PaymentEventProducer paymentEventProducer;

    @Override
    public Payment submitPayment(String orderId, BigDecimal orderPrice) {
        log.info("Creating payment for orderId {} orderPrice {}",
                orderId, orderPrice);

        var payment = paymentRepository.createPayment(orderId, orderPrice);
        paymentEventProducer.send(new PaymentEvent(payment.getId(),"order123", "100.00", "CREATED"));

        // start a payment timer process to simulate a payment
//        simulateCustomerPayment(payment.getId());

        return PaymentModel.toPayment(payment);
    }

    private void simulateCustomerPayment(String paymentId) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);

                // get random between boolean
                boolean success = Math.random() < 0.5;

                updatePaymentUseCase.paymentWebhook(paymentId, success);

            } catch (Exception e) {
                log.error("Failed to simulate customer payment");
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }
}
