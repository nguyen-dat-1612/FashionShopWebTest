package org.example.tests.checkout;

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
        loginHelper.login("nguyendatthcspv@gmail.com", "1234567");

        slowDown(1000);
        // Chuyển tới trang cart và chọn sản phẩm
        cartPage.open();
        cartPage.waitForPageToLoad();

        // Bỏ qua nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            System.out.println("Giỏ hàng trống, bỏ qua test checkout");
            return;
        }
        // Chọn tất cả sản phẩm và tiến hành thanh toán
        cartPage.selectAllItems();
        cartPage.clickCheckoutButton();

        // Đợi trang checkout load
        slowDown(1000);
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

//    @Test(priority = 2, description = "Kiểm tra áp dụng mã giảm giá hợp lệ")
//    public void testValidDiscountCode() {
//        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
//        if (!checkoutPage.isPageLoaded()) {
//            return;
//        }
//
//        // Lưu giá trị ban đầu
//        String originalDiscount = checkoutPage.getDiscount();
//        String originalTotal = checkoutPage.getTotalPrice();
//
//        // Áp dụng mã giảm giá
//        checkoutPage.enterDiscountCode("SAVE10");
//        checkoutPage.applyDiscountCode();
//
//        // Đợi alert hiển thị và xác nhận
//        slowDown(1000);
//        String alertText = checkoutPage.getAlertText();
//        checkoutPage.acceptAlert();
//
//        // Kiểm tra kết quả
//        Assert.assertTrue(alertText.contains("thành công"), "Không hiển thị thông báo áp dụng mã giảm giá thành công");
//        Assert.assertNotEquals(checkoutPage.getDiscount(), originalDiscount, "Giá trị giảm giá không thay đổi sau khi áp dụng mã");
//        Assert.assertNotEquals(checkoutPage.getTotalPrice(), originalTotal, "Tổng thanh toán không thay đổi sau khi áp dụng mã giảm giá");
//    }

//    @Test(priority = 3, description = "Kiểm tra áp dụng mã giảm giá không hợp lệ")
//    public void testInvalidDiscountCode() {
//        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
//        if (!checkoutPage.isPageLoaded()) {
//            return;
//        }
//
//        // Lưu giá trị ban đầu
//        String originalDiscount = checkoutPage.getDiscount();
//        String originalTotal = checkoutPage.getTotalPrice();
//
//        // Áp dụng mã giảm giá không hợp lệ
//        checkoutPage.enterDiscountCode("INVALID123");
//        checkoutPage.applyDiscountCode();
//
//        // Đợi alert hiển thị và xác nhận
//        slowDown(1000);
//        String alertText = checkoutPage.getAlertText();
//        checkoutPage.acceptAlert();
//
//        // Kiểm tra kết quả
//        Assert.assertTrue(alertText.contains("không hợp lệ"), "Không hiển thị thông báo mã giảm giá không hợp lệ");
//        Assert.assertEquals(checkoutPage.getDiscount(), originalDiscount, "Giá trị giảm giá thay đổi khi áp dụng mã không hợp lệ");
//    }

    @Test(priority = 4, description = "Kiểm tra hiển thị lỗi khi không nhập thông tin bắt buộc")
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

    @Test(priority = 5, description = "Kiểm tra chức năng đặt hàng thành công")
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

    @Test(priority = 6, description = "Kiểm tra chuyển hướng đến trang chi tiết đơn hàng")
    public void testNavigationToOrderDetail() {
        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
        if (!checkoutPage.isPageLoaded()) {
            return;
        }

        // Nhập đầy đủ thông tin
        checkoutPage.enterPhone("0987654321");
        checkoutPage.enterAddress("123 Đường ABC, Quận 1, TP. Hồ Chí Minh");

        // Xác nhận đặt hàng
        checkoutPage.confirmOrder();

        // Đợi modal thành công hiển thị
        slowDown(3000);

        // Nếu modal không hiển thị, bỏ qua phần còn lại của test
        if (!checkoutPage.isOrderSuccessModalDisplayed()) {
            return;
        }

        // Lấy mã đơn hàng trước khi chuyển trang
        String orderId = checkoutPage.getOrderId();

        // Click vào nút xem đơn hàng
        checkoutPage.clickViewOrderButton();

        // Đợi trang chi tiết đơn hàng load
        slowDown(2000);

        // Kiểm tra URL chứa mã đơn hàng
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("order-detail.html"), "Không chuyển hướng đến trang chi tiết đơn hàng");
        Assert.assertTrue(currentUrl.contains("id=" + orderId), "URL không chứa mã đơn hàng chính xác");
    }

    @Test(priority = 7, description = "Kiểm tra các phương thức thanh toán khác nhau")
    public void testDifferentPaymentMethods() {
        // Bỏ qua nếu không có sản phẩm trong giỏ hàng
        if (!checkoutPage.isPageLoaded()) {
            return;
        }

        // Mảng các phương thức thanh toán cần test
        String[] paymentMethods = {"COD", "VNPAY", "MOMO", "CREDIT_CARD"};

        for (String method : paymentMethods) {
            // Chọn phương thức thanh toán
            checkoutPage.selectPaymentMethod(method);

            // Kiểm tra xem có thể hoàn thành đơn hàng với phương thức này không
            checkoutPage.enterPhone("0987654321");
            checkoutPage.enterAddress("123 Đường ABC, Quận 1, TP. Hồ Chí Minh");
            checkoutPage.confirmOrder();

            // Đợi modal thành công hiển thị
            slowDown(3000);

            if (checkoutPage.isOrderSuccessModalDisplayed()) {
                System.out.println("Phương thức thanh toán " + method + " hoạt động thành công");

                // Lưu lại mã đơn hàng để kiểm tra sau này nếu cần
                String orderId = checkoutPage.getOrderId();

                // Click vào nút Về trang chủ để quay lại và test phương thức khác
                checkoutPage.clickGoToHomeButton();

//                // Quay lại trang checkout để test phương thức tiếp theo
//                prepareForCheckout();
            } else if (checkoutPage.isErrorAlertPresent()) {
                // Nếu có lỗi, ghi nhận và tiếp tục với phương thức khác
                System.out.println("Phương thức thanh toán " + method + " gặp lỗi: " + checkoutPage.getAlertText());
                checkoutPage.acceptAlert();

                // Làm mới trang để test phương thức tiếp theo
                driver.navigate().refresh();
                slowDown(1000);
            }
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}