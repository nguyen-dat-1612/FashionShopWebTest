package org.example.tests.header;


import org.example.pages.HeaderComponent;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HeaderTest {
    private WebDriver driver;
    private HeaderComponent header;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        header = new HeaderComponent(driver);
        loginHelper = new LoginHelper(driver);  // Khởi tạo LoginHelper
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị các phần tử header")
    public void testHeaderElementsDisplay() {
        Assert.assertTrue(header.isLoginModalDisplayed(), "Nút đăng nhập không hiển thị");
        Assert.assertTrue(header.isRegisterModalDisplayed(), "Nút đăng ký không hiển thị");
        Assert.assertFalse(header.isProfileIconDisplayed(), "Profile icon không nên hiển thị khi chưa đăng nhập");
        Assert.assertFalse(header.isCartIconDisplayed(), "Cart icon không nên hiển thị khi chưa đăng nhập");
    }

    @Test(priority = 2, description = "Kiểm tra đăng nhập thành công")
    public void testSuccessfulLogin() {
        // Sử dụng LoginHelper để đăng nhập
        loginHelper.login("nguyendatthcspv@gmail.com", "123456");

        Assert.assertTrue(header.isProfileIconDisplayed(), "Profile icon không hiển thị sau khi đăng nhập");
        Assert.assertTrue(header.isCartIconDisplayed(), "Cart icon không hiển thị sau khi đăng nhập");
    }

    @Test(priority = 6, description = "Kiểm tra click vào profile icon")
    public void testProfileIconClick() {
        header.clickProfileIcon();
        Assert.assertTrue(driver.getCurrentUrl().contains("profile.html"),
                "Không chuyển đến trang profile khi click vào icon");
        driver.navigate().back();
    }

    @Test(priority = 7, description = "Kiểm tra click vào cart icon")
    public void testCartIconClick() {
        header.clickCartIcon();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"),
                "Không chuyển đến trang giỏ hàng khi click vào icon");
        driver.navigate().back();
    }

    @Test
    public void testSearchProduct() {
        HeaderComponent header = new HeaderComponent(driver);
        header.openSearchModal();
        Assert.assertTrue(header.isSearchModalDisplayed());

        header.enterSearchKeyword("áo thun");
        header.submitSearch();

        // Tùy hệ thống của bạn có chuyển sang trang kết quả hay không mà xử lý thêm
        Assert.assertTrue(driver.getCurrentUrl().contains("keyword=Gucci+Logo")); // hoặc kiểm tra URL
    }

    @AfterClass
    public void tearDown() {
        loginHelper.logout();  // Đăng xuất sau khi test xong
        if (driver != null) {
            driver.quit();
        }
    }
}
