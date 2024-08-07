package com.fiap.payments.config;

import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudEventConfig {
    @Bean
    public EventFormat jsonEventFormat() {
        return EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE);
    }
}
