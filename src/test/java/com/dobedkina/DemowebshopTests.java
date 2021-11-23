package com.dobedkina;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class DemowebshopTests {

    @Test
    void addProductToCart() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                                "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .body("updatetopcartsectionhtml", is("(1)"))
                        .extract().response();
        System.out.println("Total: " + response.path("updatetopcartsectionhtml"));
        System.out.println("Message: " + response.path("message"));
    }

    @Test
    void addProductToWishlist() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("addtocart_43.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/43/2")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                        .body("updatetopwishlistsectionhtml", is("(1)"))
                        .extract().response();
        System.out.println("Total: " + response.path("updatetopwishlistsectionhtml"));
        System.out.println("Message: " + response.path("message"));

    }

    //API + UI test
    @Test
    void removeProductFromCart() {
        //добавить товар и скопировать куки
        String cartCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                                "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .statusCode(200)
                        .extract()
                        .cookie("Nop.customer");

        //открыть минимальный элемент
        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        //подложить куки
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cartCookie));

        //Открыть корзину
        open("http://demowebshop.tricentis.com/cart");

        //Проверить, что в корзине один товар
        $$(".cart-item-row").shouldHave(size(1));
        $(".qty-input").shouldHave(value("1"));

        //Очистить корзину
        $(byName("removefromcart")).click();
        $(byName("updatecart")).click();

        //Проверить, что корзина пустая
        $(".order-summary-content").shouldHave(text("Your Shopping Cart is empty!"));
        $$(".cart-item-row").shouldHave(size(0));

    }
}


