package org.example.tests.orders;

import org.example.constants.Credentials;
import org.example.pages.OrdersPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class OrdersTest extends BaseTest {
    private WebDriver driver;
    private OrdersPage ordersPage;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();

        loginHelper = new LoginHelper(driver);
        ordersPage = new OrdersPage(driver);

        // Đăng nhập trước
        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(1000);

        // Mở trang đơn hàng
        ordersPage.open();

        // Kiểm tra trang đã load thành công
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang đơn hàng không load thành công");
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị mặc định với trạng thái PENDING")
    public void testDefaultPageDisplay() {
        // Đợi trang tải xong
        ordersPage.waitForLoading();

        // Kiểm tra nút filter PENDING có active
        Assert.assertTrue(ordersPage.isFilterButtonActive("PENDING"), "Nút filter PENDING không được active mặc định");

        // Kiểm tra nếu có đơn hàng, tất cả đều có trạng thái PENDING
        if (ordersPage.getOrderCount() > 0) {
            Assert.assertTrue(ordersPage.isOrderStatusMatchingFilter(),
                    "Có đơn hàng với trạng thái không phải PENDING khi filter là PENDING");
        }
        // Nếu không có đơn hàng, kiểm tra thông báo
        else {
            Assert.assertTrue(ordersPage.isNoOrdersMessageDisplayed(),
                    "Không hiển thị thông báo 'Không có đơn hàng nào' khi không có đơn hàng");
        }
    }

    @Test(priority = 2, description = "Kiểm tra chức năng filter đơn hàng theo trạng thái")
    public void testOrderFiltering() {
        // Mảng các trạng thái cần kiểm tra
        String[] statuses = {"CONFIRMED", "DELIVERING", "DELIVERED", "CANCELLED", "PENDING"};

        for (String status : statuses) {
            // Lọc theo trạng thái
            ordersPage.filterByStatus(status);

            // Đợi tải xong
            ordersPage.waitForLoading();

            // Kiểm tra nút filter đã được active
            Assert.assertTrue(ordersPage.isFilterButtonActive(status),
                    "Nút filter " + status + " không được active sau khi click");

            // Kiểm tra trạng thái của các đơn hàng (nếu có)
            if (ordersPage.getOrderCount() > 0) {
                Assert.assertTrue(ordersPage.isOrderStatusMatchingFilter(),
                        "Có đơn hàng với trạng thái không phải " + status + " khi filter là " + status);
            }

            // Ghi nhận số lượng đơn hàng cho mỗi trạng thái (để báo cáo)
            System.out.println("Số đơn hàng với trạng thái " + status + ": " + ordersPage.getOrderCount());
        }
    }

    @Test(priority = 3, description = "Kiểm tra chức năng xem chi tiết đơn hàng")
    public void testViewOrderDetail() {
        // Lọc theo đơn hàng PENDING
        ordersPage.filterByStatus("PENDING");
        ordersPage.waitForLoading();

        // Kiểm tra nếu có đơn hàng để click
        if (ordersPage.getOrderCount() > 0) {
            // Lấy ID của đơn hàng đầu tiên
            String orderId = ordersPage.getOrderId(0);

            // Click vào đơn hàng đầu tiên
            ordersPage.clickOrderCard(0);

            // Đợi chuyển trang
            slowDown(2000);

            // Kiểm tra URL mới
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("order-detail.html"),
                    "Không chuyển hướng đến trang chi tiết đơn hàng");
            Assert.assertTrue(currentUrl.contains("id=" + orderId),
                    "URL không chứa mã đơn hàng chính xác");

            // Quay lại trang orders để tiếp tục test
            ordersPage.open();
            ordersPage.waitForLoading();
        } else {
            System.out.println("Bỏ qua test xem chi tiết đơn hàng vì không có đơn hàng PENDING");
        }
    }

    @Test(priority = 4, description = "Kiểm tra chức năng điều hướng từ sidebar")
    public void testSidebarNavigation() {
        // Kiểm tra điều hướng đến trang profile
        ordersPage.navigateToProfile();
        slowDown(1000);
        Assert.assertTrue(driver.getCurrentUrl().contains("profile.html"),
                "Không chuyển hướng đến trang profile");

        // Quay lại trang orders
        ordersPage.open();
        ordersPage.waitForLoading();

        // Kiểm tra điều hướng đến trang đổi mật khẩu
        ordersPage.navigateToChangePassword();
        slowDown(1000);
        Assert.assertTrue(driver.getCurrentUrl().contains("change-password.html"),
                "Không chuyển hướng đến trang đổi mật khẩu");

        // Quay lại trang orders
        ordersPage.open();
        ordersPage.waitForLoading();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}