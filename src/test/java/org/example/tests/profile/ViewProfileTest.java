package org.example.tests.profile;

import org.example.constants.Credentials;
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

public class ViewProfileTest  extends BaseTest {
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
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());

        slowDown(2000);

        // Đợi một phần tử trên trang profile (ví dụ: avatar hoặc tên người dùng) để đảm bảo đăng nhập hoàn tất
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("avatar-img")));  // Đợi avatar xuất hiện

        // Sau khi login xong, chuyển tới trang profile
        driver.get(PROFILE_URL);
    }

    @Test(description = "Kiểm tra hiển thị thông tin người dùng")
    public void viewProfileTest() {

        slowDown(2000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fullname")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gender")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phone")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("birthday")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("save-btn")));

    }

    @AfterClass
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

}
