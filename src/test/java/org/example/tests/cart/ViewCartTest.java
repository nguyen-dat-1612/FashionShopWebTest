package org.example.tests.cart;

import org.example.constants.Credentials;
import org.example.pages.CartPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ViewCartTest extends BaseTest {
    private WebDriver driver;
    private CartPage cartPage;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        loginHelper = new LoginHelper(driver);
        cartPage = new CartPage(driver);

        // Đăng nhập trước
        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(1000);

        // Chuyển tới trang cart sau khi đăng nhập
        cartPage.open();
        cartPage.waitForPageToLoad();
    }


    @Test(description = "Kiểm tra hiển thị giỏ hàng")
    public void testCartPageDisplay() {

        // Kiểm tra trang giỏ hàng đã load thành công
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Trang giỏ hàng không load thành công");

        // Kiểm tra trường hợp có sản phẩm trong giỏ hàng
        if (!cartPage.isEmptyCartDisplayed()) {
            Assert.assertTrue(cartPage.isSummaryCardDisplayed(), "Thẻ tóm tắt không hiển thị");
            Assert.assertTrue(cartPage.getCartItemCount() > 0, "Không có sản phẩm nào trong giỏ hàng");
        } else {
            // Trường hợp giỏ hàng trống
            Assert.assertFalse(cartPage.isSummaryCardDisplayed(), "Thẻ tóm tắt hiển thị khi giỏ hàng trống");
        }
    }

    @Test(description = "Kiểm tra chức năng chọn tất cả sản phẩm")
    public void testSelectAllItems() {
        // Bỏ qua test nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed()) {
            return;
        }

        // Bỏ chọn tất cả sản phẩm
        cartPage.deselectAllItems();

        // Kiểm tra không có sản phẩm nào được chọn
        Assert.assertFalse(cartPage.isAllItemsSelected(), "Vẫn còn sản phẩm được chọn sau khi bỏ chọn tất cả");

        // Chọn tất cả sản phẩm
        cartPage.selectAllItems();

        // Kiểm tra tất cả sản phẩm đã được chọn
        Assert.assertTrue(cartPage.isAllItemsSelected(), "Không phải tất cả sản phẩm đều được chọn");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
