package org.example.tests.auth;

import org.example.constants.Credentials;
import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest extends BaseTest {
    private WebDriver driver;
    private HomePage homePage;
    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/home.html";

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
    }

    @BeforeMethod
    public void navigateToHomePage() {
        if (driver != null) {
            driver.quit();
        }

        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);

        driver.get(BASE_URL);
        homePage.waitForPageToLoad();
    }

    @Test(priority = 1, description = "Kiểm tra đăng nhập thành công")
    public void testSuccessfulLogin() {
        HeaderComponent header = homePage.getHeader();
        header.clickLoginButton();
        header.fillLoginForm(Credentials.getUsername(), Credentials.getPassword());
        header.submitLoginForm();

        slowDown(1000);
        homePage.waitForPageToLoad();
        Assert.assertTrue(header.isProfileIconDisplayed(), "Profile icon không hiển thị sau khi đăng nhập");
        Assert.assertTrue(header.isCartIconDisplayed(), "Cart icon không hiển thị sau khi đăng nhập");
    }

    @Test(priority = 2, description = "Kiểm tra đăng nhập không thành công thành công")
    public void testUnSuccessfulLogin() {
        HeaderComponent header = homePage.getHeader();
        header.clickLoginButton();
        header.fillLoginForm(Credentials.getUsername(), "123456789");
        header.submitLoginForm();

        slowDown(1000);

        // Chờ thông báo hiển thị
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginMessage")));

        Assert.assertTrue(header.isLoginErrorDisplayed(), "Không hiển thị lỗi đăng nhập không thành công");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}