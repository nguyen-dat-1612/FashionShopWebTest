    package org.example.tests.product;

    import org.example.constants.Credentials;
    import org.example.pages.HeaderComponent;
    import org.example.pages.ProductDetailPage;
    import org.example.tests.BaseTest;
    import org.example.tests.auth.LoginTest;
    import org.example.utils.DriverManager;
    import org.example.utils.LoginHelper;
    import org.openqa.selenium.By;
    import org.openqa.selenium.JavascriptExecutor;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.Assert;
    import org.testng.annotations.AfterClass;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.BeforeMethod;
    import org.testng.annotations.Test;

    import java.time.Duration;

    public class AddToCartTest extends BaseTest {
        private static WebDriver driver;
        private static ProductDetailPage productDetailPage;
        private final String PRODUCT_URL = "http://localhost:5500/src/main/webapp/pages/product-detail.html?id=10";
        private LoginHelper loginHelper;
        @BeforeClass
        public void setUp() {
            driver = DriverManager.getDriver();
            driver.manage().window().maximize();
            productDetailPage = new ProductDetailPage(driver);
            loginHelper = new LoginHelper(driver);  // Sử dụng LoginHelper

            driver.get(PRODUCT_URL);
            // Đăng nhập sử dụng LoginHelper
            loginHelper.login(Credentials.getUsername(), Credentials.getPassword());
        }

        @Test(priority = 1, description = "Thêm sản phẩm vào giỏ hàng thành công khi đã đăng nhập")
        public void testAddSingleProductToCartWhenLoggedIn() {

            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Ghi lại số lượng hiện tại trong giỏ hàng (nếu có)
            int initialCartCount = productDetailPage.getCartCount();

            // Thêm vào giỏ hàng
            productDetailPage.clickAddToCart();

            slowDown(2000);
            String expectedMessage = "Đã thêm 1 sản phẩm vào giỏ hàng!";
            Assert.assertTrue(productDetailPage.waitAndCheckAlertMessage(expectedMessage, Duration.ofSeconds(5)),
                    "Không thấy thông báo hoặc sai nội dung: " + expectedMessage);
        }


        @Test(priority = 2, description = "Kiểm tra nút tăng số lượng sản phẩm")
        public void testIncreaseQuantityButton() {

            slowDown(2000);
            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Lấy số lượng ban đầu
            int initialQuantity = productDetailPage.getQuantity();

            // Nhấn nút tăng số lượng
            productDetailPage.increaseQuantity(1);

            // Kiểm tra số lượng đã tăng
            int newQuantity = productDetailPage.getQuantity();
            Assert.assertEquals(newQuantity, initialQuantity + 1,
                    "Số lượng không tăng sau khi nhấn nút tăng số lượng");
        }

        @Test(priority = 3, description = "Kiểm tra nút giảm số lượng sản phẩm")
        public void testDecreaseQuantityButton() {
            slowDown(2000);
            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Đặt số lượng ban đầu là 3
            productDetailPage.setQuantity(3);

            // Nhấn nút giảm số lượng
            productDetailPage.decreaseQuantity(1);

            // Kiểm tra số lượng đã giảm
            int newQuantity = productDetailPage.getQuantity();
            Assert.assertEquals(newQuantity, 2,
                    "Số lượng không giảm sau khi nhấn nút giảm số lượng");
        }

        @Test(priority = 4, description = "Kiểm tra giới hạn số lượng tối thiểu là 1")
        public void testMinimumQuantityLimit() {
            slowDown(2000);
            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Đặt số lượng ban đầu là 1
            productDetailPage.setQuantity(1);

            // Nhấn nút giảm số lượng
            productDetailPage.decreaseQuantity(1);

            // Chờ thông báo hiển thị
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));

            // Kiểm tra thông báo lỗi
            String errorMessage = "Số lượng tối thiểu là 1";
            Assert.assertTrue(productDetailPage.isAlertMessageDisplayed(errorMessage),
                    "Thông báo giới hạn số lượng tối thiểu không hiển thị");

            // Kiểm tra số lượng vẫn là 1
            Assert.assertEquals(productDetailPage.getQuantity(), 1,
                    "Số lượng không được giữ ở mức tối thiểu là 1");
        }

        @Test(priority = 5, description = "Kiểm tra giới hạn số lượng tối đa")
        public void testMaximumQuantityLimit() {
            slowDown(2000);
            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Lấy số lượng tồn kho của biến thể đã chọn
            int maxStock = productDetailPage.getMaxStockForCurrentVariant();

            // Đặt số lượng là số lượng tồn kho tối đa
            productDetailPage.setQuantity(maxStock);

            // Nhấn nút tăng số lượng
            productDetailPage.increaseQuantity(maxStock);

            // Chờ thông báo hiển thị
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));

            // Kiểm tra thông báo lỗi về giới hạn tối đa
            String errorMessage = "Số lượng tối đa có thể mua là " + maxStock;
            Assert.assertTrue(productDetailPage.isAlertMessageDisplayed(errorMessage),
                    "Thông báo giới hạn số lượng tối đa không hiển thị");

            // Kiểm tra số lượng không vượt quá giới hạn
            Assert.assertEquals(productDetailPage.getQuantity(), maxStock,
                    "Số lượng đã vượt quá giới hạn tối đa");
        }

        @Test(priority = 6, description = "Thêm sản phẩm vào giỏ hàng thất bại khi chưa đăng nhập")
        public void testAddSingleProductToCartWhenNotLoggedIn() {
            if (driver != null) {
                driver.quit();
            }
            driver = DriverManager.getDriver();
            productDetailPage = new ProductDetailPage(driver);
            driver.get(PRODUCT_URL);

            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Thêm vào giỏ hàng
            productDetailPage.clickAddToCart();

            // Verify thông báo yêu cầu đăng nhập
            String loginMessage = "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng!";
            Assert.assertTrue(productDetailPage.waitAndCheckAlertMessage(loginMessage, Duration.ofSeconds(5)),
                    "Không thấy thông báo hoặc sai nội dung: " + loginMessage);
        }

        // Hoặc nếu bạn muốn đóng sau khi tất cả các test hoàn thành
        @AfterClass
        public void tearDownClass() {
            if (driver != null) {
                driver.quit();  // Đảm bảo đóng trình duyệt sau tất cả các test
            }
        }

    }

