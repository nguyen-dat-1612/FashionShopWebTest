package org.example.tests.profile;

import org.example.pages.ProfilePage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class UpdateProfileTest extends BaseTest {
    private static WebDriver driver;
    private final String PROFILE_URL = "http://localhost:5500/src/main/webapp/pages/profile.html";
    private static ProfilePage profilePage;
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        loginHelper = new LoginHelper(driver);
        profilePage = new ProfilePage(driver);
    }

    @BeforeMethod
    public void navigateToProfilePage() {
        // Mở trang có form login để thực hiện đăng nhập
        driver.get("http://localhost:5500/src/main/webapp/pages/home.html"); // hoặc login.html nếu bạn có

        // Đăng nhập trước
        loginHelper.login("nguyendatthcspv@gmail.com", "123456");

        slowDown(2000);

        // Đợi một phần tử trên trang profile (ví dụ: avatar hoặc tên người dùng) để đảm bảo đăng nhập hoàn tất
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("avatar-img")));  // Đợi avatar xuất hiện

        // Sau khi login xong, chuyển tới trang profile
        driver.get(PROFILE_URL);
    }

    @Test(description = "Kiểm tra cập nhật thông tin người dùng")
    public void updateProfileTestSuccess() {
        slowDown(2000);
        profilePage.fillFullName("Nguyễn Đạt");
        profilePage.fillPhone("123456789");
        profilePage.fillAddress("112 Vườn Lài, Hiệp Phú, Gò Vấp, Thành Phố Hồ Chí Minh");
        profilePage.fillGender("Nam");
        profilePage.fillBirthday("16/12/1990");
        profilePage.clickSaveBtn();

        // Chờ thông báo hiển thị thay vì sleep cứng
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));


        // Verify thông báo thành công
        String successMessage = "Cập nhật thông tin thành công!";
        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);

    }
//
//    @Test(description = "Họ tên không hợp lệ (để trống)")
//    public void updateProfileWithEmptyFullName() {
//
//        driver.get(PROFILE_URL);
//
//        profilePage.fillFullName("");
//        profilePage.fillPhone("0123456789");
//        profilePage.fillAddress("112 Vườn Lài, Gò Vấp");
//        profilePage.fillGender("Nam");
//        profilePage.fillBirthday("01/01/2000");
//        profilePage.clickSaveBtn();
//
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }
//
//    @Test(description = "Số điện thoại không hợp lệ (quá ngắn)")
//    public void updateProfileWithInvalidShortPhone() {
//        driver.get(PROFILE_URL);
//
//        profilePage.fillFullName("Nguyễn Văn A");
//        profilePage.fillPhone("123"); // quá ngắn
//        profilePage.fillAddress("112 Vườn Lài, Gò Vấp");
//        profilePage.fillGender("Nam");
//        profilePage.fillBirthday("01/01/2000");
//        profilePage.clickSaveBtn();
//
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }
//
//    @Test(description = "Số điện thoại không hợp lệ (chứa ký tự)")
//    public void updateProfileWithNonNumericPhone() {
//
//        driver.get(PROFILE_URL);
//
//        profilePage.fillPhone("abc123xyz");
//        profilePage.fillAddress("112 Vườn Lài, Gò Vấp");
//        profilePage.fillGender("Nam");
//        profilePage.fillBirthday("01/01/2000");
//        profilePage.clickSaveBtn();
//
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }
//
//    @Test(description = "Ngày sinh không hợp lệ (định dạng sai)")
//    public void updateProfileWithInvalidBirthdayFormat() {
//
//        driver.get(PROFILE_URL);
//
//        profilePage.fillFullName("Nguyễn Văn A");
//        profilePage.fillPhone("0123456789");
//        profilePage.fillAddress("112 Vườn Lài, Gò Vấp");
//        profilePage.fillGender("Nam");
//        profilePage.fillBirthday("199123123"); // định dạng sai
//        profilePage.clickSaveBtn();
//
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }
//
//    @Test(description = "Địa chỉ không hợp lệ (để trống)")
//    public void updateProfileWithEmptyAddress() {
//
//        driver.get(PROFILE_URL);
//
//        profilePage.fillFullName("Nguyễn Văn A");
//        profilePage.fillPhone("0123456789");
//        profilePage.fillAddress("");
//        profilePage.fillGender("Nam");
//        profilePage.fillBirthday("01/01/2000");
//        profilePage.clickSaveBtn();
//
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }
//
//    @Test(description = "Giới tính không hợp lệ")
//    public void updateProfileWithInvalidGender() {
//
//        driver.get(PROFILE_URL);
//
//        profilePage.fillFullName("Nguyễn Văn A");
//        profilePage.fillPhone("0123456789");
//        profilePage.fillAddress("112 Vườn Lài");
//        profilePage.fillGender("Khác"); // không có trong select
//        profilePage.fillBirthday("01/01/2000");
//        profilePage.clickSaveBtn();
//        // Chờ thông báo hiển thị thay vì sleep cứng
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
//
//
//        // Verify thông báo thành công
//        String successMessage = "Cập nhật thất bại!";
//        Assert.assertTrue(profilePage.isAlertMessageDisplayed(successMessage),
//                "Thông báo thành công không hiển thị hoặc nội dung sai: " + successMessage);
//    }


    // Hoặc nếu bạn muốn đóng sau khi tất cả các test hoàn thành
    @AfterClass
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();  // Đảm bảo đóng trình duyệt sau tất cả các test
        }
    }
}
