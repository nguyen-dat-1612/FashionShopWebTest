package org.example.tests.checkout;

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

public class CheckoutTest extends BaseTest {
    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private CartPage cartPage;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        loginHelper = new LoginHelper(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        // Đăng nhập trước
        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(1000);
        // Chuyển tới trang cart và chọn sản phẩm
        cartPage.open();
        cartPage.waitForPageToLoad();

        // Bỏ qua nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            System.out.println("Giỏ hàng trống, bỏ qua test checkout");
            return;
        }

        cartPage.clickCheckoutButton();

        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout không load thành công");

    }

//    @BeforeMethod
//    public void prepareForCheckout() {
//
//    }

    @Test(priority = 1, description = "Kiểm tra hiển thị thông tin đơn hàng trên trang checkout")
    public void testOrderInformationDisplay() {
        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
        if (!checkoutPage.isPageLoaded()) {
            return;
        }

        // Kiểm tra các thông tin hiển thị
        Assert.assertTrue(checkoutPage.getOrderItemCount() > 0, "Không có sản phẩm hiển thị trên trang checkout");
        Assert.assertFalse(checkoutPage.getSubTotal().equals("0 VNĐ"), "Tổng tiền sản phẩm hiển thị không chính xác");
        Assert.assertFalse(checkoutPage.getTotalPrice().equals("0 VNĐ"), "Tổng thanh toán hiển thị không chính xác");
    }

    @Test(priority = 2, description = "Kiểm tra hiển thị lỗi khi không nhập thông tin bắt buộc")
    public void testRequiredFieldValidation() {
        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
        if (!checkoutPage.isPageLoaded()) {
            return;
        }

        // Không nhập thông tin gì, nhấn xác nhận
        checkoutPage.enterPhone("");
        checkoutPage.enterAddress("");
        checkoutPage.confirmOrder();

        // Đợi alert hiển thị
        slowDown(1000);

        // Kiểm tra thông báo lỗi
        Assert.assertTrue(checkoutPage.isValidationErrorDisplayed(), "Không hiển thị thông báo lỗi khi thiếu thông tin bắt buộc");
        String alertText = checkoutPage.getAlertText();
        Assert.assertTrue(alertText.contains("Vui lòng điền đầy đủ thông tin"), "Nội dung thông báo lỗi validation không chính xác");

        // Đóng alert
        checkoutPage.acceptAlert();
    }

    @Test(priority = 3, description = "Kiểm tra chức năng đặt hàng thành công")
    public void testSuccessfulOrderPlacement() {
        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
        if (!checkoutPage.isPageLoaded()) {
            return;
        }

        // Nhập đầy đủ thông tin
        checkoutPage.enterPhone("0987654321");
        checkoutPage.enterAddress("123 Đường ABC, Quận 1, TP. Hồ Chí Minh");
        checkoutPage.selectPaymentMethod("COD");

        // Xác nhận đặt hàng
        checkoutPage.confirmOrder();

        // Đợi modal thành công hiển thị
        slowDown(3000);

        // Kiểm tra kết quả
        Assert.assertTrue(checkoutPage.isOrderSuccessModalDisplayed(), "Modal thông báo đặt hàng thành công không hiển thị");
        Assert.assertFalse(checkoutPage.getOrderId().isEmpty(), "Không hiển thị mã đơn hàng trong thông báo thành công");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}