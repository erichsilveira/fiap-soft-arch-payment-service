package com.fiap.payments.infrastructure.web;

import com.fiap.payments.application.usecases.SearchPaymentUseCase;
import com.fiap.payments.application.usecases.SubmitPaymentUseCase;
import com.fiap.payments.application.usecases.UpdatePaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.interfaces.dto.PaymentSubmitRequest;
import com.fiap.payments.interfaces.dto.PaymentWebhookUpdateRequest;
import com.fiap.payments.interfaces.dto.SearchPaymentCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final SubmitPaymentUseCase submitPaymentUseCase;

    private final SearchPaymentUseCase searchPaymentUseCase;

    private final UpdatePaymentUseCase updatePaymentUseCase;

    @PostMapping
    ResponseEntity<String> submit(
            @RequestBody @Valid PaymentSubmitRequest submitRequest) {
        var domainEntity = submitPaymentUseCase.submitPayment(submitRequest.orderId(),
                submitRequest.orderPrice());

        return new ResponseEntity<>(domainEntity.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/webhook")
    ResponseEntity paymentWebhook(
            @RequestBody @Valid PaymentWebhookUpdateRequest webhookUpdateRequest) {
        updatePaymentUseCase.paymentWebhook(
                webhookUpdateRequest.paymentId(), webhookUpdateRequest.success());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Payment> searchByID(
            @PathVariable String id) throws ResourceNotFoundException {
        try {
            var domainEntity = searchPaymentUseCase.searchPaymentById(id);
            return ResponseEntity.ok(domainEntity);
        } catch (ResourceNotFoundException ex) {
            log.error("PaymentId not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    ResponseEntity<List<Payment>> search(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) PaymentStatus status) {
        var domainEntities = searchPaymentUseCase.searchPayments(
                new SearchPaymentCommand(orderId, status));

        return ResponseEntity.ok(domainEntities);
    }

}
