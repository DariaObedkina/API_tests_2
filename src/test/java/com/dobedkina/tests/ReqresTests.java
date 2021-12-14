package com.dobedkina.tests;

import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.dobedkina.specs.Reqres.request;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class ReqresTests {

    @Test
    @Story("Reqres")
    @DisplayName("В списке пользователей присутствует юзер с емейлом byron.fields@reqres.in")
    void findUserEmailInList() {
        given()
                .spec(request)
                .when()
                .get("users?page=2")
                .then()
                .statusCode(200)
                .body("data.findAll{it.email}.email.flatten()", hasItem("byron.fields@reqres.in"));
    }
}
