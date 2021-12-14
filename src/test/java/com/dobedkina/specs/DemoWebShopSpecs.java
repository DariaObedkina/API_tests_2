package com.dobedkina.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.dobedkina.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.is;

public class DemoWebShopSpecs {
    public static RequestSpecification request = with()
            .filter(customLogFilter().withCustomTemplates())
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .baseUri("http://demowebshop.tricentis.com/")
            .log().all();

    public static ResponseSpecification response = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody("success", is(true))
            .build();
}
