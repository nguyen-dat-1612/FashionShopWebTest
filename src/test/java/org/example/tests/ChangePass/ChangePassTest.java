package org.example.tests.ChangePass;

import org.example.constants.Credentials;
import org.example.pages.ChangePassPage;
import org.example.tests.BaseTest;
import org.example.utils.DriverManager;
import org.example.utils.LoginHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ChangePassTest extends BaseTest {
    private WebDriver driver;
    private ChangePassPage changePassPage;
    private final String PROFILE_URL = "http://localhost:5500/src/main/webapp/pages/profile.html";
    private LoginHelper loginHelper;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        changePassPage = new ChangePassPage(driver);
        loginHelper = new LoginHelper(driver);

        driver.get("http://localhost:5500/src/main/webapp/pages/home.html");
        loginHelper.login(Credentials.getUsername(), Credentials.getPassword());
        slowDown(2000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("avatar-img")));

        driver.get(PROFILE_URL);
        slowDown(2000);
        changePassPage.open();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testChangePasswordSuccess() {
        changePassPage.open();
        slowDown(2000);

        // Đổi mật khẩu từ cũ sang mới
        changePassPage.changePassword("1234567", "12345678", "12345678");

        // Kiểm tra message thành công
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Đổi mật khẩu thành công!", "Thông báo sai hoặc không đúng");
        System.out.println(actualMessage);
        // Trả mật khẩu về lại như cũ
        changePassPage.open();
        changePassPage.changePassword("12345678", "1234567", "1234567");

        // Kiểm tra trả lại mật khẩu thành công
        WebElement rollbackMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String rollbackMessage = rollbackMessageElement.getText();
        System.out.println(rollbackMessage);
        Assert.assertEquals(rollbackMessage, "Đổi mật khẩu thành công!", "Không đổi lại mật khẩu như ban đầu thành công");
    }

    @Test
    public void testIncorrectOldPassword() {
        changePassPage.open();
        changePassPage.changePassword("12345678", "newpassword123", "newpassword123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Mật khẩu hiện tại không chính xác", "Thông báo sai hoặc không đúng");
        System.out.println(actualMessage);
    }

    @Test
    public void testMissingInformation() {
        changePassPage.open();

        // TH1: Thiếu mật khẩu cũ
        changePassPage.enterNewPassword("newpassword123")
                .enterConfirmPassword("newpassword123")
                .clickSaveButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Vui lòng điền đầy đủ thông tin", "Thông báo sai hoặc không đúng khi thiếu mật khẩu cũ");
        System.out.println("TH1 - Thiếu mật khẩu cũ: " + actualMessage);

        // TH2: Thiếu mật khẩu mới
        changePassPage.clearAllFields();
        changePassPage.enterOldPassword("1234567")
                .enterConfirmPassword("newpassword123")
                .clickSaveButton();

        WebElement messageElement2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage2 = messageElement2.getText();
        Assert.assertEquals(actualMessage2, "Vui lòng điền đầy đủ thông tin", "Thông báo sai hoặc không đúng khi thiếu mật khẩu mới");
        System.out.println("TH2 - Thiếu mật khẩu mới: " + actualMessage2);
    }

    @Test
    public void testPasswordMismatch() {
        changePassPage.open();
        changePassPage.changePassword("password123", "newpassword123", "differentpassword");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Mật khẩu mới và xác nhận mật khẩu không khớp", "Thông báo sai hoặc không đúng");
        System.out.println(actualMessage);
    }

    @Test
    public void testPasswordTooShort() {
        changePassPage.open();
        changePassPage.changePassword("password123", "short", "short");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Mật khẩu mới phải có ít nhất 6 ký tự", "Thông báo sai hoặc không đúng");
        System.out.println(actualMessage);
    }

    @Test
    public void testNewPasswordSameAsOld() {
        changePassPage.open();
        changePassPage.changePassword("password123", "password123", "password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, "Mật khẩu mới phải khác mật khẩu cũ", "Thông báo sai hoặc không đúng");
        System.out.println(actualMessage);
    }
}
