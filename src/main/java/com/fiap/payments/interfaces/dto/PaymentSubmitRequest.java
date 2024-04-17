package com.fiap.payments.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentSubmitRequest(@NotNull String orderId,
                                   @NotNull BigDecimal orderPrice) {

}
