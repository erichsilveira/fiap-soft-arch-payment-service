package com.fiap.payments.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.jackson.PojoCloudEventDataMapper;

import java.nio.charset.StandardCharsets;

import static io.cloudevents.core.CloudEventUtils.mapData;

public abstract class BaseConsumer {

    private final EventFormat jsonEventFormat;
    private final ObjectMapper objectMapper;

    protected BaseConsumer(final EventFormat jsonEventFormat,
                                     final ObjectMapper objectMapper) {
        this.jsonEventFormat = jsonEventFormat;
        this.objectMapper = objectMapper;
    }

    protected CloudEvent deserializeMessage(final String rawMessage)
            throws Exception {
        if (rawMessage == null) {
            throw new Exception(
                    "Invalid message received: null");
        }
        final CloudEvent cloudEvent = this.jsonEventFormat.deserialize(
                rawMessage.getBytes(StandardCharsets.UTF_8));

        if (cloudEvent == null || cloudEvent.getType() == null) {
            throw new Exception(
                    String.format("Invalid cloud event message '%s'", rawMessage));
        }
        return cloudEvent;
    }

    protected <T> T getCloudEventData(
            CloudEvent cloudEvent, Class<T> target) throws Exception {
        final PojoCloudEventData<T> cloudEventData = mapData(
                cloudEvent,
                PojoCloudEventDataMapper.from(this.objectMapper, target));

        if (cloudEventData == null) {
            throw new Exception(
                    "It was not possible to parse the cloud event data");
        }
        return cloudEventData.getValue();
    }
}
