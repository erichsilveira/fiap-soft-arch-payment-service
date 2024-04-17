package com.fiap.payments.infrastructure.model;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {

    public static PaymentModel toPaymentModel(Payment payment) {
        return PaymentModel.builder()
            .id(payment.getId())
            .orderId(payment.getOrderId())
            .orderPrice(payment.getOrderPrice())
            .status(payment.getStatus())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }

    public static Payment toPayment(PaymentModel paymentModel) {
        return Payment.builder()
            .id(paymentModel.getId())
            .orderId(paymentModel.getOrderId())
            .orderPrice(paymentModel.getOrderPrice())
            .status(paymentModel.getStatus())
            .createdAt(paymentModel.getCreatedAt())
            .updatedAt(paymentModel.getUpdatedAt())
            .build();
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name="order_id")
    private String orderId;

    @Column(name="order_price")
    private BigDecimal orderPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
