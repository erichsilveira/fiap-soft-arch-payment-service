package com.fiap.payments.domain.service;

import com.fiap.payments.application.usecases.UpdatePaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.infrastructure.model.PaymentModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdatePaymentUseCaseImpl implements UpdatePaymentUseCase {

    private final PaymentRepository paymentRepository;

    // webhook simulation
    public void paymentWebhook(String paymentId, boolean success) {
        log.info("Payment webhook for paymentId {} success {}", paymentId, success);

        try {
            updatePayment(paymentId, success ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
        } catch (ResourceNotFoundException e) {
            log.error("Failed to update payment for order {}", paymentId);
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public void updatePayment(String paymentId, PaymentStatus paymentStatus)
        throws ResourceNotFoundException {
        log.info("Updating payment for paymentId {} paymentStatus {}",
            paymentId, paymentStatus);

        PaymentModel currentPaymentModel = paymentRepository.getPayment(paymentId)
            .orElseThrow(ResourceNotFoundException::new);

        Payment convertedPayment = PaymentModel.toPayment(currentPaymentModel);

        if (convertedPayment.canUpdate()) {
            paymentRepository.updatePayment(paymentId, paymentStatus);
        } else {
            throw new RuntimeException("Payment cannot be updated");
        }

        // TODO: isso vai sair daqui e vai ser um hook la no orders
//        if (paymentStatus.equals(PaymentStatus.APPROVED)) {
//            updateOrderUseCase.updateOrder(
//                new UpdateOrderCommand(currentPayment.getOrderId(), OrderStatus.REQUESTED));
//        }
    }
}
