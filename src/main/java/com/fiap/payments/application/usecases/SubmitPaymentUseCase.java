package com.fiap.payments.application.usecases;

import com.fiap.payments.domain.entity.Payment;
import java.math.BigDecimal;

public interface SubmitPaymentUseCase {

    Payment submitPayment(String orderId, BigDecimal orderPrice);
}
