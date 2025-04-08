package org.example.tests.product;

import org.example.constants.Credentials;
import org.example.pages.CartPage;
import org.example.pages.ProductDetailPage;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class BuyNowTest {
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

    @Test(description = "Nhấn mua sản phẩm thành công khi đã đăng nhập")
    public void testBuyNowWhenLoggedIn() {
        // Đăng nhập sử dụng LoginHelper
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        // Chọn màu và size
        productDetailPage.selectColor(1);
        productDetailPage.selectSize(1);

        // Nhấn mua ngay
        productDetailPage.clickBuyNow();

        // Chờ cho URL thay đổi và kiểm tra URL mới
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("cart.html?selectItem="));

        // Kiểm tra URL đúng định dạng mong đợi
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart.html?selectItem="),
                "URL không chuyển đến trang giỏ hàng với sản phẩm được chọn");

        // Lấy tham số `selectItem` từ URL và tìm sản phẩm đã chọn
        String selectedVariantId = currentUrl.split("selectItem=")[1];

        // Đợi phần tử được chọn hiển thị (phần tử có class 'selected-highlight')
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selected-highlight")));

        // Kiểm tra sản phẩm có được chọn trong giỏ hàng
        WebElement selectedProduct = driver.findElement(By.cssSelector(".cart-item[data-variant-id='" + selectedVariantId + "']"));
        Assert.assertTrue(selectedProduct.isDisplayed(), "Sản phẩm không hiển thị trong giỏ hàng");

    }
    @Test(description = "Nhấn mua sản phẩm thất bại khi chưa đăng nhập")
    public void testBuyNowWhenNotLoggedIn() {
        // Đảm bảo chưa đăng nhập
        driver.manage().deleteAllCookies(); // Xóa cookie
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();"); // Xóa localStorage bằng JS

        // Chọn màu và size
        productDetailPage.selectColor(1);
        productDetailPage.selectSize(1);

        // Thêm vào giỏ hàng
        productDetailPage.clickBuyNow();

        // Chờ thông báo hiển thị thay vì sleep cứng
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));

        // Verify thông báo yêu cầu đăng nhập
        String loginMessage = "Bạn cần đăng nhập để mua ngay!";
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
