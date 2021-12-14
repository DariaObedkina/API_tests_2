package com.dobedkina.models.demowebshop;

import groovy.transform.builder.Builder;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductToAdd {
    private String rawBody;
}
