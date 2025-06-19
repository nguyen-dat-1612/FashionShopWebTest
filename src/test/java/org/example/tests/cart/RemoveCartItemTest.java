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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class RemoveCartItemTest extends BaseTest {
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

        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(1000);
        // Chuyển tới trang cart sau khi đăng nhập
        cartPage.open();
        cartPage.waitForPageToLoad();
    }

    @Test(description = "Kiểm tra chức năng xóa sản phẩm khỏi giỏ hàng")
    public void testRemoveCartItem() {
        // Bỏ qua test nếu giỏ hàng trống
        if (cartPage.isEmptyCartDisplayed() || cartPage.getCartItemCount() == 0) {
            return;
        }

        // Lấy số lượng sản phẩm ban đầu
        int initialItemCount = cartPage.getCartItemCount();

        // Lấy thông tin sản phẩm trước khi xóa để kiểm tra sau
        String itemName = cartPage.getItemName(0);

        // Xóa sản phẩm đầu tiên
        cartPage.removeItem(0);

        slowDown(1000); // Đợi để hệ thống xử lý

        // Kiểm tra xem thông báo thành công có hiển thị không
        Assert.assertTrue(cartPage.isSuccessMessageDisplayed(), "Không hiển thị thông báo thành công sau khi xóa sản phẩm");

        // Kiểm tra số lượng sản phẩm đã giảm
        int newItemCount = cartPage.getCartItemCount();
        Assert.assertEquals(newItemCount, initialItemCount - 1, "Số lượng sản phẩm không giảm sau khi xóa");

        // Nếu giỏ hàng rỗng sau khi xóa
        if (newItemCount == 0) {
            Assert.assertTrue(cartPage.isEmptyCartDisplayed(), "Không hiển thị thông báo giỏ hàng trống");
        } else {
            // Kiểm tra xem sản phẩm đầu tiên còn lại có phải là sản phẩm đã xóa không
            String newFirstItemName = cartPage.getItemName(0);
            Assert.assertNotEquals(newFirstItemName, itemName, "Sản phẩm không được xóa khỏi giỏ hàng");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
