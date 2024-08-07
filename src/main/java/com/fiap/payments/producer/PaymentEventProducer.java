package com.fiap.payments.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.payments.domain.entity.PaymentEvent;
import io.awspring.cloud.sqs.operations.SqsOperations;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventProducer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SqsOperations sqsOperations;

    private final String queueName;

    public PaymentEventProducer(SqsOperations sqsOperations,
                                @Value("${payment.queue.url}") String queueName){
        this.sqsOperations = sqsOperations;
        this.queueName = queueName;
    }

    @SneakyThrows
    public void send(PaymentEvent paymentEvent) {

        log.info("Sending to queue {} - {}", "payment-request-queue", paymentEvent);

        sqsOperations.send(queueName, MAPPER.writeValueAsString(paymentEvent));
        log.info("Message sent {}", paymentEvent);
    }
}
