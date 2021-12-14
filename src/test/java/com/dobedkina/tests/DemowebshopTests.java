package com.dobedkina.tests;

import com.dobedkina.models.demowebshop.ProductAddedToCart;
import com.dobedkina.models.demowebshop.ProductAddedToWishlist;
import com.dobedkina.models.demowebshop.ProductToAdd;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.dobedkina.specs.DemoWebShopSpecs.request;
import static com.dobedkina.specs.DemoWebShopSpecs.response;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DemowebshopTests extends TestBase {

    private ProductAddedToCart productInCart;
    private ProductAddedToWishlist productInWishList;
    private String productAddedToCartMessage = "The product has been added to your <a href=\"/cart\">shopping cart</a>";
    private String productAddedToWishlistMessage = "The product has been added to your <a href=\"/wishlist\">wishlist</a>";

    @Test
    @Story("DemoWebShop")
    @DisplayName("Добавление одного товара в корзину")
    void addProductToCart() {
        ProductToAdd product = new ProductToAdd();
        product.setRawBody("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1");

        step("Добавить товар в корзину", () -> {
            productInCart = given()
                    .spec(request)
                    .body(product.getRawBody())
                    .when()
                    .post("addproducttocart/details/72/1")
                    .then()
                    .spec(response)
                    .extract().as(ProductAddedToCart.class);
            assertEquals(productAddedToCartMessage, productInCart.getMessage());
            assertEquals("(1)", productInCart.getUpdatetopcartsectionhtml());
        });
    }

    @Test
    @Story("DemoWebShop")
    @DisplayName("Добавление одного товара в wishlist")
    void addProductToWishlist() {
        ProductToAdd product = new ProductToAdd();
        product.setRawBody("addtocart_43.EnteredQuantity=1");

        step("Добавить товар в wishlist", () -> {
            productInWishList = given()
                    .spec(request)
                    .body(product.getRawBody())
                    .when()
                    .post("addproducttocart/details/43/2")
                    .then()
                    .spec(response)
                    .extract().as(ProductAddedToWishlist.class);
            assertEquals(productAddedToWishlistMessage, productInWishList.getMessage());
            assertEquals("(1)", productInWishList.getUpdatetopwishlistsectionhtml());
        });
    }

    //API + UI test
    @Test
    @Story("DemoWebShop")
    @DisplayName("Удаление товара из корзины")
    void removeProductFromCart() {
        ProductToAdd product = new ProductToAdd();
        product.setRawBody("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1");

        step("Добавить товар в корзину и подложить куки", () -> {
            String cartCookie = given()
                    .spec(request)
                    .body(product.getRawBody())
                    .when()
                    .post("addproducttocart/details/72/1")
                    .then()
                    .spec(response)
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


