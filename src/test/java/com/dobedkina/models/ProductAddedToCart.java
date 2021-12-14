package com.dobedkina.models;

import groovy.transform.builder.Builder;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductAddedToCart {
    private String success;
    private String message;
    private String updatetopcartsectionhtml;
    private String updateflyoutcartsectionhtml;
}
