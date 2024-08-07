package com.fiap.payments.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.payments.domain.entity.OrderPaymentEvent;
import com.fiap.payments.domain.entity.PaymentEvent;
import io.awspring.cloud.sqs.operations.SqsOperations;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPaymentEventProducer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SqsOperations sqsOperations;

    private final String queueName;

    public OrderPaymentEventProducer(SqsOperations sqsOperations,
                                @Value("${order.payment.queue.url}") String queueName){
        this.sqsOperations = sqsOperations;
        this.queueName = queueName;
    }

    @SneakyThrows
    public void send(OrderPaymentEvent orderPaymentEvent) {

        log.info("Sending to queue {} - {}", "order-payment-queue", orderPaymentEvent);

        sqsOperations.send(queueName, MAPPER.writeValueAsString(orderPaymentEvent));
        log.info("Message sent {}", orderPaymentEvent);
    }


}
