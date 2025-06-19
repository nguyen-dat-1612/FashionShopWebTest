package org.example.tests.cart;

import org.example.constants.Credentials;
import org.example.pages.CartPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class CartQuantityTest extends BaseTest {
    private WebDriver driver;
    private CartPage cartPage;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        loginHelper = new LoginHelper(driver);
        cartPage = new CartPage(driver);

        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(1000);

        // Chuyển tới trang cart sau khi đăng nhập
        cartPage.open();
    }


    @Test(description = "Kiểm tra chức năng tăng số lượng sản phẩm")
    public void testIncreaseQuantity() {

        slowDown(1000);
        // Bỏ qua test nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            return;
        }

        // Tăng số lượng sản phẩm
        cartPage.increaseQuantity(0);

        slowDown(1000); // Đợi để hệ thống xử lý

        // Chờ thông báo xuất hiện
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement successToast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-toast")));

        // Kiểm tra nội dung thông báo
        String message = successToast.getText();
        Assert.assertTrue(message.contains("✓"));
        Assert.assertTrue(message.contains("Đã cập nhật số lượng sản phẩm")); // ví dụ

    }

    @Test(description = "Kiểm tra chức năng giảm số lượng sản phẩm")
    public void testDecreaseQuantity() {
        // Bỏ qua test nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            return;
        }

        // Đảm bảo số lượng ban đầu lớn hơn 1 để có thể giảm
        int initialQuantity = cartPage.getQuantity(0);
        if (initialQuantity <= 1) {
            cartPage.increaseQuantity(0);
            slowDown(1000); // Đợi để hệ thống xử lý
            initialQuantity = cartPage.getQuantity(0);
        }

        // Giảm số lượng sản phẩm
        cartPage.decreaseQuantity(1);

        // Chờ thông báo xuất hiện
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement successToast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-toast")));

        // Kiểm tra nội dung thông báo
        String message = successToast.getText();
        Assert.assertTrue(message.contains("✓"));
        Assert.assertTrue(message.contains("Đã cập nhật số lượng sản phẩm")); // ví dụ

    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
