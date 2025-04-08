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
        private final String PRODUCT_URL = "http://localhost:5500/src/main/webapp/pages/product-detail.html?id=7";
        private LoginHelper loginHelper;
        @BeforeClass
        public void setUp() {
            driver = DriverManager.getDriver();
            driver.manage().window().maximize();
            productDetailPage = new ProductDetailPage(driver);
            loginHelper = new LoginHelper(driver);  // Sử dụng LoginHelper

        }

        @BeforeMethod
        public void navigateToProductDetailPage() {
            driver.get(PRODUCT_URL);
            // Đợi trang tải xong
            productDetailPage.waitForPageToLoad();
        }
        @Test (description = "Thêm sản phẩm vào giỏ hàng thành công khi đã đăng nhập")
        public void testAddSingleProductToCartWhenLoggedIn() {
            // Đăng nhập sử dụng LoginHelper
            loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Thêm vào giỏ hàng
            productDetailPage.clickAddToCart();

            // Chờ thông báo hiển thị thay vì sleep cứng
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));


            // Verify thông báo thành công
            String successMessage = "Đã thêm sản phẩm vào giỏ hàng!";
            Assert.assertTrue(productDetailPage.isAlertMessageDisplayed(successMessage),
                    "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
        }

        @Test(description = "Thêm sản phẩm vào giỏ hàng thất bại khi chưa đăng nhập")
        public void testAddSingleProductToCartWhenNotLoggedIn() {
            // Đảm bảo chưa đăng nhập
            driver.manage().deleteAllCookies(); // Xóa cookie
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.localStorage.clear();"); // Xóa localStorage bằng JS

            // Chọn màu và size
            productDetailPage.selectColor(1);
            productDetailPage.selectSize(1);

            // Thêm vào giỏ hàng
            productDetailPage.clickAddToCart();

            // Chờ thông báo hiển thị thay vì sleep cứng
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));

            // Verify thông báo yêu cầu đăng nhập
            String loginMessage = "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng!";
            boolean isDisplayed = productDetailPage.isAlertMessageDisplayed(loginMessage);
            Assert.assertTrue(isDisplayed,
                    "Thông báo yêu cầu đăng nhập không hiển thị hoặc nội dung sai. Kỳ vọng: '" + loginMessage + "'");
        }


        // Hoặc nếu bạn muốn đóng sau khi tất cả các test hoàn thành
        @AfterClass
        public void tearDownClass() {
            if (driver != null) {
                driver.quit();  // Đảm bảo đóng trình duyệt sau tất cả các test
            }
        }

    }

