package com.dobedkina.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;


public class TestBase {
    @BeforeAll
    public static void setUp() {
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub/";
        Configuration.browserSize = "1920x1080";
    }
}
