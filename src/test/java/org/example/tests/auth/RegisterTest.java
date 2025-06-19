package org.example.tests.auth;

import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    private final String TEST_NAME = "Nguyen Van A";
    private final String TEST_EMAIL = "testuser_" + System.currentTimeMillis() + "@example.com";
    private final String TEST_PASSWORD = "Password123";
    private final String TEST_PHONE = "0912345678";
    private final String TEST_ADDRESS = "123 ABC Street, HCMC";
    private final String TEST_GENDER = "Nam"; // hoặc "female", "other"
    private final String TEST_BIRTHDAY = "2000-01-01"; // yyyy-MM-dd

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
                TEST_PASSWORD,
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,               // hoặc "female", "other"
                TEST_BIRTHDAY
        );

        // Submit form
        header.submitRegisterForm();
        slowDown(1000);
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
                TEST_PASSWORD,
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,               // hoặc "female", "other"
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @Test(priority = 3, description = "Kiểm tra đăng ký với Họ và tên rỗng")
    public void testRegisterWithEmptyFullName() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD,
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,               // hoặc "female", "other"
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @Test(priority = 4, description = "Kiểm tra đăng ký với email rỗng")
    public void testRegisterWithEmptyEmail() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                "", // Tên rỗng
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD,
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);;

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @Test(priority = 5, description = "Kiểm tra đăng ký với mật khẩu ngắn hơn 6 ký tự")
    public void testRegisterWithShortPassword() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                "123", // Mật khẩu quá ngắn
                "123",
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @Test(priority = 6, description = "Kiểm tra đăng ký với mật khẩu không khớp")
    public void testRegisterWithPasswordMismatch() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                "WrongPassword123",
                TEST_PHONE,
                TEST_ADDRESS,
                TEST_GENDER,
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @Test(priority = 7, description = "Kiểm tra đăng ký với mật khẩu không khớp")
    public void testRegisterWithMissingPhone() {
        HeaderComponent header = homePage.getHeader();
        header.clickRegisterButton();
        header.fillRegisterForm(
                TEST_NAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD,
                "", // Không có số điện thoại
                TEST_ADDRESS,
                TEST_GENDER,
                TEST_BIRTHDAY
        );
        header.submitRegisterForm();

        slowDown(1000);

        Assert.assertTrue(header.isRegisterErrorMessageDisplayed(),
                "Không hiển thị thông báo lỗi email đã tồn tại");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}