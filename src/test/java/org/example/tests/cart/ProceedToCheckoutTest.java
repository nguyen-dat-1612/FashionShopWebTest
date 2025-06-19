package org.example.tests.cart;

import org.example.constants.Credentials;
import org.example.pages.CartPage;
import org.example.pages.CheckoutPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class ProceedToCheckoutTest extends BaseTest {
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
        cartPage.waitForPageToLoad();
    }

    @Test(description = "Kiểm tra chức năng tiến hành thanh toán với giỏ hàng có sản phẩm")
    public void testProceedToCheckout() {
        // Bỏ qua test nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            return;
        }

        // Đảm bảo đã chọn tất cả sản phẩm
        cartPage.selectAllItems();

        // Nhấn nút "Đặt Hàng"
        cartPage.clickCheckoutButton();

        // Đợi để chuyển trang
        slowDown(2000);

        // Kiểm tra xem đã chuyển đến trang checkout chưa
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("checkout.html"),
                "Không chuyển hướng đến trang checkout sau khi nhấn nút Đặt Hàng");

        // Kiểm tra xem trang checkout có được hiển thị đúng không
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout không load thành công");
    }

    @Test(description = "Kiểm tra chức năng tiếp tục mua sắm khi giỏ hàng trống")
    public void testContinueShopping() {
        // Chỉ chạy test này nếu giỏ hàng trống
        if (!cartPage.isEmptyCartDisplayed()) {
            return;
        }

        // Nhấn nút "Tiếp tục mua sắm"
        cartPage.clickContinueShopping();

        // Đợi để chuyển trang
        slowDown(2000);

        // Kiểm tra xem đã chuyển đến trang home chưa
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("home.html"),
                "Không chuyển hướng đến trang home sau khi nhấn nút Tiếp tục mua sắm");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}