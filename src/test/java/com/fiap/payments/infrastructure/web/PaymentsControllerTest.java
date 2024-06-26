package com.fiap.payments.infrastructure.web;

import com.fiap.payments.application.usecases.SearchPaymentUseCase;
import com.fiap.payments.application.usecases.SubmitPaymentUseCase;
import com.fiap.payments.application.usecases.UpdatePaymentUseCase;
import com.fiap.payments.domain.entity.Payment;
import com.fiap.payments.exception.ResourceNotFoundException;
import com.fiap.payments.interfaces.dto.SearchPaymentCommand;
import com.fiap.payments.mocks.PaymentMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentsController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class PaymentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmitPaymentUseCase submitPaymentUseCase;

    @MockBean
    private SearchPaymentUseCase searchPaymentUseCase;

    @MockBean
    private UpdatePaymentUseCase updatePaymentUseCase;

    @Test
    public void testSubmitPayment() throws Exception {
        Payment payment = PaymentMock.generatePayment("payment123", 10000, "order123");

        given(submitPaymentUseCase.submitPayment(anyString(), any(BigDecimal.class))).willReturn(
                payment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":\"order123\",\"orderPrice\":10000}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("payment123"));
    }

    @Test
    public void testPaymentWebhook() throws Exception {
        mockMvc.perform(post("/payments/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paymentId\":\"payment123\",\"success\":true}"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSearchOrderPaymentStatus() throws Exception {
        Payment payment = PaymentMock.generatePayment("payment123", 10000, "order123");

        given(searchPaymentUseCase.searchPaymentById("payment123")).willReturn(payment);

        mockMvc.perform(get("/payments/payment123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("payment123")));
    }

    @Test
    public void testSearchOrderPaymentStatusNotFound() throws Exception {
        given(searchPaymentUseCase.searchPaymentById("not_found")).willThrow(
                ResourceNotFoundException.class);

        mockMvc.perform(get("/payments/not_found"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchPayments() throws Exception {
        // Given
        Payment paymentOne = PaymentMock.generatePayment("payment123", 10000, "order123");
        Payment paymentTwo = PaymentMock.generatePayment("payment124", 20000, "order124");
        List<Payment> payments = Arrays.asList(paymentOne, paymentTwo);

        given(searchPaymentUseCase.searchPayments(any(SearchPaymentCommand.class))).willReturn(payments);

        // When & Then
        mockMvc.perform(get("/payments")
                        .param("orderId", "order123")
                        .param("status", "CREATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("payment123")))
                .andExpect(jsonPath("$[1].id", is("payment124")));
    }
}
