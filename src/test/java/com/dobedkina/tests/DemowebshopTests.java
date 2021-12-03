package com.dobedkina.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.dobedkina.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class DemowebshopTests {

    @Test
    @DisplayName("Добавление одного товара в корзину")
    void addProductToCart() {
        step("Добавить товар в корзину", () ->
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                                "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .body("updatetopcartsectionhtml", is("(1)")));

    }

    @Test
    @DisplayName("Добавление одного товара в wishlist")
    void addProductToWishlist() {
        step("Добавить товар в wishlist", () ->
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("addtocart_43.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/43/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                        .body("updatetopwishlistsectionhtml", is("(1)")));

    }

    //API + UI test
    @Test
    @DisplayName("Удаление товара из корзины")
    void removeProductFromCart() {
        step("Добавить товар в корзину и подложить куки", () -> {
            String cartCookie =
                    given()
                            .filter(customLogFilter().withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                                    "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                            .when()
                            .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookie("Nop.customer");

            step("Открыть минимальный элемент", () ->
                    open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png"));
            step("Подложить куки", () ->
                    getWebDriver().manage().addCookie(new Cookie("Nop.customer", cartCookie)));

        });


        step("Открыть корзину", () ->
                open("http://demowebshop.tricentis.com/cart"));

        step("Проверить, что в корзине 1 товар", () -> {
            $$(".cart-item-row").shouldHave(size(1));
            $(".qty-input").shouldHave(value("1"));
        });

        step("Очистить корзину", () -> {
            $(byName("removefromcart")).click();
            $(byName("updatecart")).click();
        });

        step("Проверить, что корзина пустая", () -> {
            $(".order-summary-content").shouldHave(text("Your Shopping Cart is empty!"));
            $$(".cart-item-row").shouldHave(size(0));
        });

    }
}


