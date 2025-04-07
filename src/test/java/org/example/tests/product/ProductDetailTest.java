package org.example.tests.product;

import org.example.tests.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.example.pages.ProductDetailPage;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;

import static org.testng.AssertJUnit.*;

public class ProductDetailTest extends BaseTest {

    private WebDriver driver;
    private static ProductDetailPage productDetailPage;
    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/product-detail.html?id=7";
    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        productDetailPage = new ProductDetailPage(driver);
    }

    @BeforeMethod
    public void navigateToProductDetailPage() {
        driver.get(BASE_URL);
        // Đợi trang tải xong
        productDetailPage.waitForPageToLoad();
    }
    @Test(priority = 1, description = "Hiển thị thông tin sản phẩm")
    public void testProductInformationDisplay() {
        assertTrue("Product image should be displayed",
                productDetailPage.isProductImageDisplayed());

        assertFalse("Product name should not be empty",
                productDetailPage.getProductName().isEmpty());

        assertTrue("Product price should contain VNĐ",
                productDetailPage.getProductPrice().contains("VNĐ"));
    }
}