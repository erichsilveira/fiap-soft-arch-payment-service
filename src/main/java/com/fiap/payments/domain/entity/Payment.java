package com.fiap.payments.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Builder
public class Payment {

    private final String id;

    private final String orderId;

    private BigDecimal orderPrice;

    private PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public boolean canUpdate() {
        return status == PaymentStatus.CREATED || status == PaymentStatus.REJECTED;
    }
}
