package org.example.tests.orders;

import org.example.constants.Credentials;
import org.example.pages.OrderDetailPage;
import org.example.pages.OrdersPage;
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
import org.testng.annotations.Test;

import java.time.Duration;

public class OrderDetailTest extends BaseTest {
    private WebDriver driver;
    private OrderDetailPage orderDetailPage;
    private OrdersPage ordersPage;
    private LoginHelper loginHelper;
    private String testOrderId;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        loginHelper = new LoginHelper(driver);
        ordersPage = new OrdersPage(driver);
        orderDetailPage = new OrderDetailPage(driver);

        // Đăng nhập trước
        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());
        slowDown(1000);

        // Mở trang đơn hàng để lấy ID đơn hàng
        ordersPage.open();
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang đơn hàng không load thành công");
        ordersPage.waitForLoading();

        // Tìm một đơn hàng để sử dụng trong test
        if (ordersPage.getOrderCount() > 0) {
            testOrderId = ordersPage.getOrderId(0);
            System.out.println("Sử dụng đơn hàng ID: " + testOrderId + " cho các test");
        } else {
            // Tìm kiếm trong các tab khác nếu không có đơn hàng PENDING
            String[] statuses = {"CONFIRMED", "DELIVERING", "DELIVERED", "CANCELLED"};
            boolean foundOrder = false;

            for (String status : statuses) {
                ordersPage.filterByStatus(status);
                ordersPage.waitForLoading();

                if (ordersPage.getOrderCount() > 0) {
                    testOrderId = ordersPage.getOrderId(0);
                    System.out.println("Sử dụng đơn hàng ID: " + testOrderId + " (status: " + status + ") cho các test");
                    foundOrder = true;
                    break;
                }
            }

            if (!foundOrder) {
                System.out.println("Không tìm thấy đơn hàng nào để test. Các test có thể sẽ bị bỏ qua.");
            }
        }
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị chi tiết đơn hàng")
    public void testOrderDetailDisplay() {
        // Bỏ qua test nếu không có đơn hàng để test
        if (testOrderId == null || testOrderId.isEmpty()) {
            System.out.println("Bỏ qua test vì không có đơn hàng");
            return;
        }

        // Mở trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        Assert.assertTrue(orderDetailPage.isPageLoaded(), "Trang chi tiết đơn hàng không load thành công");
        orderDetailPage.waitForPageToLoad();

        // Kiểm tra các thông tin cơ bản
        Assert.assertEquals(orderDetailPage.getOrderId(), testOrderId, "ID đơn hàng hiển thị không chính xác");
        Assert.assertFalse(orderDetailPage.getOrderDate().isEmpty(), "Ngày đặt hàng không hiển thị");
        Assert.assertFalse(orderDetailPage.getOrderStatus().isEmpty(), "Trạng thái đơn hàng không hiển thị");
        Assert.assertFalse(orderDetailPage.getOrderTotal().isEmpty(), "Tổng tiền không hiển thị");

        // Kiểm tra danh sách sản phẩm
        int itemCount = orderDetailPage.getOrderItemCount();
        Assert.assertTrue(itemCount > 0, "Không có sản phẩm nào trong đơn hàng");

        // Kiểm tra chi tiết sản phẩm đầu tiên
        Assert.assertFalse(orderDetailPage.getProductName(0).isEmpty(), "Tên sản phẩm không hiển thị");
        Assert.assertFalse(orderDetailPage.getProductQuantity(0).isEmpty(), "Số lượng sản phẩm không hiển thị");
        Assert.assertFalse(orderDetailPage.getProductPrice(0).isEmpty(), "Đơn giá sản phẩm không hiển thị");
        Assert.assertFalse(orderDetailPage.getProductTotal(0).isEmpty(), "Thành tiền sản phẩm không hiển thị");
    }

    @Test(priority = 2, description = "Kiểm tra hiển thị nút hủy đơn hàng dựa vào trạng thái")
    public void testCancelButtonVisibility() {
        // Bỏ qua test nếu không có đơn hàng để test
        if (testOrderId == null || testOrderId.isEmpty()) {
            System.out.println("Bỏ qua test vì không có đơn hàng");
            return;
        }

        // Mở trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        orderDetailPage.waitForPageToLoad();

        // Kiểm tra trạng thái và hiển thị nút hủy
        String status = orderDetailPage.getOrderStatus();

        if (status.equals("Đang chờ")) {
            Assert.assertTrue(orderDetailPage.isCancelButtonDisplayed(),
                    "Nút hủy đơn hàng không hiển thị cho đơn hàng PENDING");
        } else {
            Assert.assertFalse(orderDetailPage.isCancelButtonDisplayed(),
                    "Nút hủy đơn hàng hiển thị cho đơn hàng không phải PENDING");
        }
    }

    @Test(priority = 3, description = "Kiểm tra chức năng hủy đơn hàng")
    public void testCancelOrder() {
        // Bỏ qua test nếu không có đơn hàng để test
        if (testOrderId == null || testOrderId.isEmpty()) {
            System.out.println("Bỏ qua test vì không có đơn hàng");
            return;
        }

        // Mở trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        orderDetailPage.waitForPageToLoad();

        // Kiểm tra nếu đơn hàng có thể hủy
        if (orderDetailPage.isCancelButtonDisplayed()) {
            orderDetailPage.clickCancelOrder();
            Assert.assertTrue(orderDetailPage.isCancelOrderModalVisible());

            Assert.assertEquals("XÁC NHẬN HỦY ĐƠN HÀNG", orderDetailPage.getModalTitle());
            Assert.assertEquals("Bạn có chắc muốn hủy đơn hàng này?", orderDetailPage.getModalMessage());

            Assert.assertTrue(orderDetailPage.isModalConfirmVisible());
            Assert.assertTrue(orderDetailPage.isModalCancelVisible());

            orderDetailPage.clickModalConfirm();

            // Đợi thông báo thành công
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("modal-title"), "THÔNG BÁO"));

            Assert.assertEquals("Đơn hàng đã được hủy thành công!", orderDetailPage.getModalMessage());

            orderDetailPage.clickModalConfirm();

            slowDown(2000);
            wait.until(ExpectedConditions.urlContains("/src/main/webapp/pages/orders.html"));
        } else {
            System.out.println("Bỏ qua test hủy đơn hàng vì đơn hàng này không thể hủy (không phải PENDING)");
        }
    }

    @Test(priority = 4, description = "Kiểm tra chức năng quay lại từ chi tiết đơn hàng")
    public void testBackButtonFunction() {
        // Tìm một đơn hàng mới nếu đơn hàng trước đã bị hủy
        if (ordersPage.getOrderCount() == 0) {
            ordersPage.open();
            ordersPage.waitForLoading();

            if (ordersPage.getOrderCount() > 0) {
                testOrderId = ordersPage.getOrderId(0);
            } else {
                // Tìm trong các tab khác
                String[] statuses = {"CONFIRMED", "DELIVERING", "DELIVERED", "CANCELLED"};
                for (String status : statuses) {
                    ordersPage.filterByStatus(status);
                    ordersPage.waitForLoading();

                    if (ordersPage.getOrderCount() > 0) {
                        testOrderId = ordersPage.getOrderId(0);
                        break;
                    }
                }
            }
        }

        // Bỏ qua test nếu không có đơn hàng để test
        if (testOrderId == null || testOrderId.isEmpty()) {
            System.out.println("Bỏ qua test vì không có đơn hàng");
            return;
        }

        // Mở trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        orderDetailPage.waitForPageToLoad();

        // Click nút quay lại
        orderDetailPage.clickBackButton();
        slowDown(1000);

        // Kiểm tra đã quay về trang danh sách đơn hàng
        Assert.assertTrue(driver.getCurrentUrl().contains("orders.html"),
                "Không chuyển về trang danh sách đơn hàng khi click nút quay lại");
    }

    @Test(priority = 5, description = "Kiểm tra xử lý lỗi khi ID đơn hàng không hợp lệ")
    public void testInvalidOrderId() {
        // Mở trang chi tiết với ID không hợp lệ
        String invalidId = "invalid-id-123456";
        orderDetailPage.open(invalidId);
        slowDown(2000);

        // Kiểm tra hiển thị thông báo lỗi
        Assert.assertTrue(orderDetailPage.isErrorDisplayed(),
                "Không hiển thị thông báo lỗi khi sử dụng ID đơn hàng không hợp lệ");
    }

    @Test(priority = 6, description = "Kiểm tra chức năng điều hướng từ sidebar")
    public void testSidebarNavigation() {
        // Bỏ qua test nếu không có đơn hàng để test
        if (testOrderId == null || testOrderId.isEmpty()) {
            System.out.println("Bỏ qua test vì không có đơn hàng");
            return;
        }

        // Mở trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        orderDetailPage.waitForPageToLoad();

        // Kiểm tra điều hướng đến trang profile
        orderDetailPage.navigateToProfile();
        slowDown(1000);
        Assert.assertTrue(driver.getCurrentUrl().contains("profile.html"),
                "Không chuyển hướng đến trang profile");

        // Quay lại trang chi tiết đơn hàng
        orderDetailPage.open(testOrderId);
        orderDetailPage.waitForPageToLoad();

        // Kiểm tra điều hướng đến trang đổi mật khẩu
        orderDetailPage.navigateToChangePassword();
        slowDown(1000);
        Assert.assertTrue(driver.getCurrentUrl().contains("change-password.html"),
                "Không chuyển hướng đến trang đổi mật khẩu");
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}