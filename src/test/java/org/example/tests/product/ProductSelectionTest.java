package org.example.tests.product;

import org.example.pages.ProductDetailPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class ProductSelectionTest extends BaseTest {
    private WebDriver driver;
    private static ProductDetailPage productDetailPage;
    private final String PRODUCT_URL = "http://localhost:5500/src/main/webapp/pages/product-detail.html?id=7";

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        productDetailPage = new ProductDetailPage(driver);
    }
    @BeforeMethod
    public void navigateToProductPage() {
        driver.get(PRODUCT_URL);
        productDetailPage.waitForPageToLoad();
    }

    @Test(description = "Chọn màu sắc sản phẩm")
    public void testColorSelection() {
        productDetailPage.selectColor(1);
        slowDown(2000);
        assertTrue(productDetailPage.isColorSelected(1));
    }

    @Test(description = "Chọn kích thước sản phẩm")
    public void testSizeSelection() {
        productDetailPage.selectSize(1);
        slowDown(2000);
        assertTrue(productDetailPage.isSizeSelected(1));
    }
}
