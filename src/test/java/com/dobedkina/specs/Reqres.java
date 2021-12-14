package com.dobedkina.specs;

import io.restassured.specification.RequestSpecification;

import static com.dobedkina.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;

public class Reqres {
    public static RequestSpecification request = with()
            .filter(customLogFilter().withCustomTemplates())
            .baseUri("https://reqres.in/")
            .basePath("api/")
            .log().all();
}

