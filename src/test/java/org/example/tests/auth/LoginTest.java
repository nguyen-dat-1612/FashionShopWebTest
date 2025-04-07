package org.example.tests.auth;

import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
        driver.get(BASE_URL);
        // Đợi trang tải xong
        homePage.waitForPageToLoad();
    }

    @Test(priority = 1, description = "Kiểm tra đăng nhập thành công")
    public void testSuccessfulLogin() {
        HeaderComponent header = homePage.getHeader();
        header.clickLoginButton();
        header.fillLoginForm("nguyendatthcspv@gmail.com", "123456");
        header.submitLoginForm();


        homePage.waitForPageToLoad();

        Assert.assertTrue(header.isProfileIconDisplayed(), "Profile icon không hiển thị sau khi đăng nhập");
        Assert.assertTrue(header.isCartIconDisplayed(), "Cart icon không hiển thị sau khi đăng nhập");
    }

    @Test(priority = 2, description = "Kiểm tra đăng nhập không thành công thành công")
    public void testUnSuccessfulLogin() {
        HeaderComponent header = homePage.getHeader();
        header.clickLoginButton();
        header.fillLoginForm("nguyendatthcspv@gmail.com", "1234567");
        header.submitLoginForm();

        // Chờ thông báo hiển thị thay vì sleep cứng
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginMessage")));

        Assert.assertTrue(header.isLoginErrorDisplayed(), "Không hiển thị lỗi đăng nhập không thành công");
    }
}