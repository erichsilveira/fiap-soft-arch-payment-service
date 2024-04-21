package com.fiap.payments.infrastructure.adapter;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.entity.PaymentStatus;
import com.fiap.payments.infrastructure.model.PaymentModel;
import com.fiap.payments.infrastructure.persistence.PaymentJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentPersistenceAdapterTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @InjectMocks
    private PaymentPersistenceAdapter paymentPersistenceAdapter;

    @Test
    public void testGetPayment_Found() {
        String paymentId = "payment123";
        Optional<PaymentModel> expected = Optional.of(new PaymentModel());
        when(paymentJpaRepository.findById(paymentId)).thenReturn(expected);

        Optional<PaymentModel> result = paymentPersistenceAdapter.getPayment(paymentId);

        assertTrue(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    public void testGetPayment_NotFound() {
        String paymentId = "paymentNotFound";
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.empty());

        Optional<PaymentModel> result = paymentPersistenceAdapter.getPayment(paymentId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCreatePayment() {
        String orderId = "order123";
        BigDecimal orderPrice = new BigDecimal("100.00");
        PaymentModel savedPaymentModel = new PaymentModel();
        when(paymentJpaRepository.save(any(PaymentModel.class))).thenReturn(savedPaymentModel);

        PaymentModel result = paymentPersistenceAdapter.createPayment(orderId, orderPrice);

        assertNotNull(result);
        verify(paymentJpaRepository).save(any(PaymentModel.class));
    }

    @Test
    public void testUpdatePayment_Exists() {
        String paymentId = "payment123";
        PaymentModel foundPaymentModel = new PaymentModel();
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.of(foundPaymentModel));
        when(paymentJpaRepository.save(foundPaymentModel)).thenReturn(foundPaymentModel);

        assertDoesNotThrow(
                () -> paymentPersistenceAdapter.updatePayment(paymentId, PaymentStatus.APPROVED));
        assertEquals(PaymentStatus.APPROVED, foundPaymentModel.getStatus());
        verify(paymentJpaRepository).save(foundPaymentModel);
    }

    @Test
    public void testUpdatePayment_NotFound() {
        String paymentId = "paymentNotFound";
        when(paymentJpaRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> paymentPersistenceAdapter.updatePayment(paymentId, PaymentStatus.APPROVED));
    }

    @Test
    void testSearchPayments_WithFilters() {
        // Setup
        String orderId = "order123";
        PaymentStatus status = PaymentStatus.CREATED;
        PaymentModel paymentModel = PaymentModel.builder()
                .orderId(orderId)
                .status(status)
                .orderPrice(new BigDecimal("99.99"))
                .build();
        List<PaymentModel> expectedList = Collections.singletonList(paymentModel);

        when(paymentJpaRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(expectedList);

        // Execution
        List<Payment> result = paymentPersistenceAdapter.searchPayments(orderId, status);

        // Assertions
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    void testSearchPayments_NoFilters() {
        // Setup
        when(paymentJpaRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(Collections.emptyList());

        // Execution
        List<Payment> result = paymentPersistenceAdapter.searchPayments(null, null);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
