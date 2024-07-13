package com.fiap.payments.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PaymentSubmitRequest(@NotNull @Size(min = 1, max = 100) String orderId,
                                   @NotNull BigDecimal orderPrice) {

}
