package org.example.tests.home;

import org.example.pages.HomePage;
import org.example.pages.ProductDetailPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageProductsTest extends BaseTest {
    private WebDriver driver;
    private HomePage homePage;
    private static ProductDetailPage productDetailPage;

    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/home.html";

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
    }
    @BeforeMethod
    public void navigateToHomePage() {
        driver.get(BASE_URL);
        // Đợi trang tải xong
        homePage.waitForPageToLoad();
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị sản phẩm nổi bật")
    public void testProductSectionDisplay() {
        Assert.assertTrue(homePage.isProductSectionDisplayed());
        Assert.assertEquals(homePage.getProductSectionTitle(), "Sản Phẩm Nổi Bật");
    }

    @Test(priority = 2, description = "Kiểm tra danh sách sản phẩm")
    public void testProductsLoaded() {
        homePage.waitForProductsLoaded();
        Assert.assertTrue(homePage.getProductsCount() > 0);
    }

    @Test(priority = 3, description = "Kiểm tra click vào sản phẩm")
    public void testProductCardClick() {
        homePage.waitForProductsLoaded();
        if (homePage.getProductsCount() > 0) {
            String currentUrl = driver.getCurrentUrl();
            homePage.clickProductCard(0);
            homePage.waitForUrlChange(currentUrl);
            Assert.assertTrue(driver.getCurrentUrl().contains("product-detail.html?id="));
        }
    }
}