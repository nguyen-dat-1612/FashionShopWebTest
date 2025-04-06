package org.example.tests;

import org.example.pages.ProductDetailPage;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class ProductDetailPageTest extends BaseTest {
    private static WebDriver driver;
    private static ProductDetailPage productDetailPage;
    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/product-detail.html?id=7";
    @BeforeClass
    public static void setUp() {
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

    @Test(priority = 2, description = "Chọn màu sắc")
    public void testColorSelection() {
        productDetailPage.selectColor(1);
        slowDown(2000);
        assertTrue("First color option should be selected",
                productDetailPage.isColorSelected(1));
    }

    @Test(priority = 3, description = "Chọn kích thước")
    public void testSizeSelection() {
        productDetailPage.selectSize(1);
        slowDown(2000);
        assertTrue("First size option should be selected",
                productDetailPage.isSizeSelected(1));
    }

    @Test(priority = 4, description = "Điều chỉnh số lượng")
    public void testQuantityAdjustment() {
        int initialQuantity = productDetailPage.getCurrentQuantity();
        slowDown(2000);
        productDetailPage.increaseQuantity(1);
        assertEquals("Quantity should increase by 1",
                initialQuantity + 1, productDetailPage.getCurrentQuantity());

        productDetailPage.decreaseQuantity(1);
        assertEquals("Quantity should return to initial value",
                initialQuantity, productDetailPage.getCurrentQuantity());
    }

    @Test(priority = 5 , description = "Thêm sản phẩm vào giỏ hàng")
    public void testAddToCartFunctionality() {
        productDetailPage.clickAddToCart();
        slowDown(2000);
        // Thêm verification cho thông báo thành công nếu cần
    }

//    @Test
//    public void testDealSectionFunctionality() {
//        assertTrue("Deal section should be displayed",
//                productDetailPage.isDealSectionDisplayed());
//
//        String initialTotal = productDetailPage.getComboTotal();
//        productDetailPage.selectBundleItem(0);
//
//        assertNotEquals("Combo total should change after selecting bundle item",
//                initialTotal, productDetailPage.getComboTotal());
//    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
