package com.fiap.payments.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudEvent<T> {

    @NotNull
    private String id;

    @NotNull
    @Builder.Default
    @JsonProperty("specversion")
    private String specVersion = "1.0";

    @Builder.Default
    @JsonProperty("datacontenttype")
    private String dataContentType = "application/json";

    @NotNull
    private String source;

    @NotNull
    private String subject;

    @NotNull
    private String type;

    @Valid
    private T data;

    private String time;
}
