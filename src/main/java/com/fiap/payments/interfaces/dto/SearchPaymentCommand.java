package com.fiap.payments.interfaces.dto;

import com.fiap.payments.domain.entity.PaymentStatus;

public record SearchPaymentCommand(String orderId, PaymentStatus status) {
}
