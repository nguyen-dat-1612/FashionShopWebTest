package org.example.tests;


import org.example.pages.HomePage;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class HomePageTest extends BaseTest {

    private WebDriver driver;
    private HomePage homePage;
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

    @Test(priority = 1, description = "Kiểm tra tiêu đề trang")
    public void testPageTitle() {
        String expectedTitle = "Fashion Style - Trang Chủ";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Tiêu đề trang không chính xác");
    }

    @Test(priority = 2, description = "Kiểm tra Carousel hiển thị đúng")
    public void testCarouselExists() {
        Assert.assertTrue(homePage.isCarouselDisplayed(), "Carousel không hiển thị");
        Assert.assertTrue(homePage.isCarouselCaptionDisplayed(), "Caption của carousel không hiển thị");
    }

    @Test(priority = 3, description = "Kiểm tra phần Danh mục sản phẩm hiển thị")
    public void testCategorySectionExists() {
        Assert.assertTrue(homePage.isCategorySectionDisplayed(), "Phần danh mục sản phẩm không hiển thị");
        String expectedSectionTitle = "Danh Mục Sản Phẩm";
        String actualSectionTitle = homePage.getCategorySectionTitle();
        Assert.assertEquals(actualSectionTitle, expectedSectionTitle, "Tiêu đề phần danh mục không chính xác");
    }

    @Test(priority = 4, description = "Kiểm tra phần Sản phẩm nổi bật hiển thị")
    public void testProductSectionExists() {
        Assert.assertTrue(homePage.isProductSectionDisplayed(), "Phần sản phẩm nổi bật không hiển thị");
        String expectedSectionTitle = "Sản Phẩm Nổi Bật";
        String actualSectionTitle = homePage.getProductSectionTitle();
        Assert.assertEquals(actualSectionTitle, expectedSectionTitle, "Tiêu đề phần sản phẩm không chính xác");
    }

    @Test(priority = 5, description = "Kiểm tra danh sách danh mục được tải")
    public void testCategoriesLoaded() {
        // Đợi cho danh mục được tải
        homePage.waitForCategoriesLoaded();
        Assert.assertTrue(homePage.getCategoriesCount() > 0, "Danh sách danh mục trống");
    }

    @Test(priority = 6, description = "Kiểm tra danh sách sản phẩm được tải")
    public void testProductsLoaded() {
        // Đợi cho sản phẩm được tải
        homePage.waitForProductsLoaded();
        Assert.assertTrue(homePage.getProductsCount() > 0, "Danh sách sản phẩm trống");
    }

    @Test(priority = 7, description = "Kiểm tra thông tin sản phẩm hiển thị đúng")
    public void testProductCardDetails() {
        // Đợi cho sản phẩm được tải
        homePage.waitForProductsLoaded();

        // Kiểm tra nếu có ít nhất một sản phẩm
        if (homePage.getProductsCount() > 0) {
            Assert.assertTrue(homePage.isProductImageDisplayed(0), "Hình ảnh sản phẩm không hiển thị");
            Assert.assertTrue(homePage.isProductNameDisplayed(0), "Tên sản phẩm không hiển thị");
            Assert.assertTrue(homePage.isProductPriceDisplayed(0), "Giá sản phẩm không hiển thị");
        } else {
            Assert.fail("Không có sản phẩm nào để kiểm tra");
        }
    }


    @Test(priority = 8, description = "Kiểm tra tải component header và footer")
    public void testHeaderFooterLoaded() {
        Assert.assertTrue(homePage.isHeaderLoaded(), "Header không được tải");
        Assert.assertTrue(homePage.isFooterLoaded(), "Footer không được tải");
    }

    @Test(priority = 9, description = "Kiểm tra chức năng click vào sản phẩm")
    public void testProductCardClick() {
        // Đợi cho sản phẩm được tải
        homePage.waitForProductsLoaded();

        // Kiểm tra nếu có ít nhất một sản phẩm
        if (homePage.getProductsCount() > 0) {
            String currentUrl = driver.getCurrentUrl();
            homePage.clickProductCard(0);

            // Đợi cho trang chi tiết tải
            homePage.waitForUrlChange(currentUrl);

            // Kiểm tra URL có chứa tham số id
            String newUrl = driver.getCurrentUrl();
            Assert.assertTrue(newUrl.contains("product-detail.html?id="),
                    "URL không chuyển đến trang chi tiết sản phẩm");
        } else {
            Assert.fail("Không có sản phẩm nào để kiểm tra");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
