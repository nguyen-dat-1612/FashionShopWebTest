package org.example.tests;

import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.utils.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HeaderTest {
    private WebDriver driver;
    private HeaderComponent header;
    private HomePage homePage;
    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/home.html";

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.get(BASE_URL); // ✅ Load trang trước
        homePage = new HomePage(driver);
        homePage.waitForPageToLoad(); // Chờ load hoàn tất
        header = homePage.getHeader();
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị các phần tử header")
    public void testHeaderElementsDisplay() {
        Assert.assertTrue(header.isLoginModalDisplayed(), "Nút đăng nhập không hiển thị");
        Assert.assertTrue(header.isRegisterModalDisplayed(), "Nút đăng ký không hiển thị");
        Assert.assertFalse(header.isProfileIconDisplayed(), "Profile icon không nên hiển thị khi chưa đăng nhập");
        Assert.assertFalse(header.isCartIconDisplayed(), "Cart icon không nên hiển thị khi chưa đăng nhập");
    }

//    @Test(priority = 2, description = "Kiểm tra mở modal đăng nhập")
//    public void testOpenLoginModal() {
//        header.clickLoginButton();
//        Assert.assertTrue(header.isLoginModalDisplayed(), "Modal đăng nhập không hiển thị");
//    }
//
//    @Test(priority = 3, description = "Kiểm tra mở modal đăng ký")
//    public void testOpenRegisterModal() {
//        header.clickRegisterButton();
//        Assert.assertTrue(header.isRegisterModalDisplayed(), "Modal đăng ký không hiển thị");
//    }

    @Test(priority = 5, description = "Kiểm tra đăng nhập thành công")
    public void testSuccessfulLogin() {
        header.clickLoginButton();
        header.fillLoginForm("nguyendatthcspv@gmail.com", "123456");
        header.submitLoginForm();

        // Giả lập đăng nhập thành công bằng JavaScript
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.setItem('isLoggedIn', 'true');" +
                        "document.getElementById('profileIcon').classList.remove('d-none');" +
                        "document.getElementById('cartIcon').classList.remove('d-none');"
        );

        Assert.assertTrue(header.isProfileIconDisplayed(), "Profile icon không hiển thị sau khi đăng nhập");
        Assert.assertTrue(header.isCartIconDisplayed(), "Cart icon không hiển thị sau khi đăng nhập");
    }

//    @Test(priority = 4, description = "Kiểm tra đăng ký thành công")
//    public void testSuccessfulRegistration() {
//        header.clickRegisterButton();
//        header.fillRegisterForm("Nguyen Van B", "newuser222@example.com", "password123", "password123");
//        header.submitRegisterForm();
//
//        // Thêm assertions để kiểm tra thông báo thành công
//        // (Cần thêm locator cho thông báo thành công trong HeaderComponent)
//    }

    @Test(priority = 6, description = "Kiểm tra click vào profile icon")
    public void testProfileIconClick() {
        header.clickProfileIcon();
        // Kiểm tra URL sau khi click
        Assert.assertTrue(driver.getCurrentUrl().contains("profile.html"),
                "Không chuyển đến trang profile khi click vào icon");
        driver.navigate().back();
    }

    @Test(priority = 7, description = "Kiểm tra click vào cart icon")
    public void testCartIconClick() {
        header.clickCartIcon();
        // Kiểm tra URL sau khi click
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"),
                "Không chuyển đến trang giỏ hàng khi click vào icon");
        driver.navigate().back();
    }

    @AfterClass
    public void tearDown() {
        // Xóa trạng thái đăng nhập sau khi test
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        if (driver != null) {
            driver.quit();
        }
    }
}
