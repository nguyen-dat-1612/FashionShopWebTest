package org.example.tests.auth;

import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    private final String TEST_NAME = "Nguyen Van A";
    private final String TEST_EMAIL = "testuser_" + System.currentTimeMillis() + "@example.com";
    private final String TEST_PASSWORD = "Password123";
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

    @Test(priority = 1, description = "Kiểm tra đăng ký tài khoản mới thành công")
    public void testSuccessfulRegistration() {
        HeaderComponent header = homePage.getHeader();
        // Mở modal đăng ký
        header.clickRegisterButton();
        Assert.assertTrue(header.isRegisterModalDisplayed(), "Modal đăng ký không hiển thị");

        // Điền thông tin đăng ký
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
        );

        // Submit form
        header.submitRegisterForm();
        slowDown(4000);
        // Kiểm tra kết quả
        Assert.assertTrue(header.isRegisterSuccessMessageDisplayed(),
                "Không hiển thị thông báo đăng ký thành công");

    }

    @Test(priority = 2, description = "Kiểm tra đăng ký với email đã tồn tại")
    public void testRegisterWithExistingEmail() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
        );
        header.submitRegisterForm();

        slowDown(2000);


        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }
}