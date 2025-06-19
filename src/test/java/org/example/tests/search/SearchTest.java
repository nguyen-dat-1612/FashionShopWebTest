package org.example.tests.search;

import org.example.constants.Credentials;
import org.example.pages.HeaderComponent;
import org.example.pages.HomePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class SearchTest extends BaseTest {
    private WebDriver driver;
    private HomePage homePage;
    private final String BASE_URL = "http://localhost:5500/src/main/webapp/pages/home.html";

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
    }

    @BeforeMethod
    public void openHomePage() {
        if (driver != null) {
            driver.quit();
        }
        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
        driver.get(BASE_URL);
        homePage.waitForPageToLoad();
    }

    @Test(description = "Đăng nhập và tìm kiếm sản phẩm, chuyển đến trang chi tiết nếu tìm ra 1 kết quả")
    public void testSearchProductAndNavigateToDetail() {
        HeaderComponent header = homePage.getHeader();

        // Đăng nhập trước
        header.clickLoginButton();
        header.fillLoginForm(Credentials.getUsername(), Credentials.getPassword());
        header.submitLoginForm();
        slowDown(1000); // Cho backend xử lý

        Assert.assertTrue(header.isProfileIconDisplayed(), "Đăng nhập không thành công");

        // Mở modal tìm kiếm
        header.openSearchModal();
        Assert.assertTrue(header.isSearchModalDisplayed(), "Search modal không hiển thị");

        String keyword = "Gucci Logo";
        header.enterSearchKeyword(keyword);
        header.submitSearch();

        slowDown(1000); // Cho phép trang chuyển

        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL sau khi tìm kiếm: " + currentUrl);

        // Nếu tìm ra đúng 1 sản phẩm → chuyển sang chi tiết
        Assert.assertTrue(
                currentUrl.contains("search-results.html?keyword=Gucci+Logo"),
                "Không điều hướng tới trang kết quả tìm kiếm"
        );
    }
    @Test(description = "Đăng nhập và tìm kiếm sản phẩm không tồn tại, hiển thị thông báo không tìm thấy")
    public void testSearchProductNotFound() {
        HeaderComponent header = homePage.getHeader();

        // Đăng nhập trước
        header.clickLoginButton();
        header.fillLoginForm(Credentials.getUsername(), Credentials.getPassword());
        header.submitLoginForm();
        slowDown(1000); // Cho backend xử lý

        Assert.assertTrue(header.isProfileIconDisplayed(), "Đăng nhập không thành công");

        // Tìm kiếm sản phẩm không tồn tại
        header.openSearchModal();
        Assert.assertTrue(header.isSearchModalDisplayed(), "Search modal không hiển thị");

        String keyword = "xyzKhôngTồnTạiABC123";
        header.enterSearchKeyword(keyword);
        header.submitSearch();
        slowDown(1500); // Cho trang chuyển

        // Kiểm tra URL chứa keyword
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("keyword="), "URL không chứa keyword sau tìm kiếm");

        // Kiểm tra hiển thị thông báo
        Assert.assertTrue(homePage.isSearchNoResultDisplayed(), "Không hiển thị thông báo không tìm thấy sản phẩm");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
