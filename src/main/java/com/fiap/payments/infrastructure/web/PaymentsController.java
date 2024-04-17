package com.fiap.payments.infrastructure.web;

import com.fiap.payments.application.usecases.SearchPaymentUseCase;
import com.fiap.payments.application.usecases.SubmitPaymentUseCase;
import com.fiap.payments.application.usecases.UpdatePaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.interfaces.dto.PaymentSubmitRequest;
import com.fiap.payments.interfaces.dto.PaymentWebhookUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentsController {

    private final SubmitPaymentUseCase submitPaymentUseCase;

    private final SearchPaymentUseCase searchPaymentUseCase;

    private final UpdatePaymentUseCase updatePaymentUseCase;

    @PostMapping("/payments")
    ResponseEntity<String> submit(
        @RequestBody @Valid PaymentSubmitRequest submitRequest) {
        var domainEntity = submitPaymentUseCase.submitPayment(submitRequest.orderId(),
            submitRequest.orderPrice());

        return new ResponseEntity<>(domainEntity.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/payments/webhook")
    ResponseEntity paymentWebhook(
        @RequestBody @Valid PaymentWebhookUpdateRequest webhookUpdateRequest) {
        updatePaymentUseCase.paymentWebhook(
            webhookUpdateRequest.paymentId(), webhookUpdateRequest.success());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments/{id}")
    ResponseEntity<Payment> searchOrderPaymentStatus(
        @PathVariable String id) throws ResourceNotFoundException {
        try {
            var domainEntity = searchPaymentUseCase.searchPayment(id);
            return ResponseEntity.ok(domainEntity);
        } catch (ResourceNotFoundException ex) {
            log.error("PaymentId not found");
            return ResponseEntity.notFound().build();
        }
    }

}
