package com.fiap.payments.domain.service;

import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.domain.repository.PaymentRepository;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.infrastructure.model.PaymentModel;
import com.fiap.payments.mocks.PaymentMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchPaymentUseCaseTest {

    @InjectMocks
    private static SearchPaymentUseCaseImpl useCase;
    @Mock
    private PaymentRepository mockRepository;

    @AfterEach
    void resetMocks() {
        reset(mockRepository);
    }

    @Test
    public void testSearchPayment_Found() throws ResourceNotFoundException {
        String paymentId = "123";
        long price = 10;

        PaymentModel mockPaymentModel = PaymentMock.generatePaymentModel(paymentId, price);
        when(mockRepository.getPayment(paymentId)).thenReturn(Optional.of(mockPaymentModel));

        Payment result = useCase.searchPaymentById(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(price, result.getOrderPrice().longValue());

        verify(mockRepository).getPayment(paymentId);
    }

    @Test
    public void testSearchPayment_NotFound() {
        String paymentId = "not_found";

        assertThrows(ResourceNotFoundException.class, () -> {
            when(mockRepository.getPayment(paymentId)).thenReturn(Optional.empty());
            useCase.searchPaymentById(paymentId);
        });
    }
}

