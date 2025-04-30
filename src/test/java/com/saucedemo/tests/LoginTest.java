package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.WebDriverSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest {
    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeMethod
    public void setup() {
        driver = WebDriverSetup.getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        logger.info("Browser opened and navigated to saucedemo.com");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"invalid_user", "invalid_pass", false}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean isValid) {
        loginPage.login(username, password);
        if (isValid) {
            Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed for valid credentials");
            logger.info("Login successful for user: {}", username);
        } else {
            String error = loginPage.getErrorMessage();
            Assert.assertTrue(error.contains("Epic sadface"), "Error message not displayed for invalid login");
            logger.info("Login failed as expected for user: {}", username);
        }
    }

    @AfterMethod
    public void tearDown() {
        WebDriverSetup.quitDriver();
    }
}