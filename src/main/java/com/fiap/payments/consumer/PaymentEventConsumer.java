package com.fiap.payments.consumer;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.payments.domain.entity.OrderPaymentEvent;
import com.fiap.payments.domain.entity.PaymentEvent;
import com.fiap.payments.domain.service.UpdatePaymentUseCaseImpl;
import com.fiap.payments.producer.OrderPaymentEventProducer;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;


@Slf4j
@AllArgsConstructor
@Component
public class PaymentEventConsumer {

    private final UpdatePaymentUseCaseImpl updatePaymentUseCase;

    private final OrderPaymentEventProducer orderPaymentEventProducer;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    @SqsListener(value = "${payment.queue.url}", acknowledgementMode = "ALWAYS")
    public void consumePaymentEvent(final String rawMessage) {
        log.info("Processing payment request event: {}", rawMessage);

        try {
            String deserializable = MAPPER.readValue(rawMessage, String.class);
            PaymentEvent paymentEvent = MAPPER.readValue(deserializable, PaymentEvent.class);
            log.info("Deserialized Payment event: {}", paymentEvent);

            log.info("Contacting 3rd party payment gateway to process payment");
            simulateCustomerPayment(paymentEvent.getId());
            log.info("Payment event successfully processed");

            orderPaymentEventProducer.send(OrderPaymentEvent.builder()
                    .orderId(paymentEvent.getOrderId())
                    .status("DENIED").build());
            log.info("Order Payment event successfully sent");

        } catch (Exception e) {
            log.error("Error consuming payment event", e);
        }
    }

    private void simulateCustomerPayment(String paymentId) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);

                // get random between boolean
                boolean success = Math.random() < 0.5;

                updatePaymentUseCase.paymentWebhook(paymentId, success);
            } catch (Exception e) {
                log.error("Failed to simulate customer payment");
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

}
