package org.example.tests.home;

import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageLayoutTest extends BaseTest {

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

    @Test(priority = 8, description = "Kiểm tra tải component header và footer")
    public void testHeaderFooterLoaded() {
        Assert.assertTrue(homePage.isHeaderLoaded(), "Header không được tải");
        Assert.assertTrue(homePage.isFooterLoaded(), "Footer không được tải");
    }
}