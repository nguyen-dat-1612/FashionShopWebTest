package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Đại diện cho trang thay đổi mật khẩu của ứng dụng Fashion Store
 */
public class ChangePassPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By oldPasswordInput = By.id("old-password");
    private final By newPasswordInput = By.id("new-password");
    private final By confirmPasswordInput = By.id("confirm-password");
    private final By savePasswordBtn = By.id("save-password");
    private final By messageElement = By.id("message");
    private final By logoutButton = By.id("logout");
    private final By profileLink = By.id("profile");
    private final By ordersLink = By.id("orders");

    public ChangePassPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    public ChangePassPage open() {
        driver.get("http://localhost:5500/src/main/webapp/pages/change-password.html");
        return this;
    }

    public ChangePassPage enterOldPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(oldPasswordInput)).sendKeys(password);
        return this;
    }


    public ChangePassPage enterNewPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordInput)).sendKeys(password);
        return this;
    }

    public ChangePassPage enterConfirmPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput)).sendKeys(password);
        return this;
    }


    public void clickSaveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(savePasswordBtn)).click();
    }



    public void goToProfile() {
        wait.until(ExpectedConditions.elementToBeClickable(profileLink)).click();
    }


    public void goToOrders() {
        wait.until(ExpectedConditions.elementToBeClickable(ordersLink)).click();
    }

    public String getMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(messageElement)).getText();
    }

    public boolean isMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(messageElement)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isMessageTextDisplayed(String expectedText) {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(messageElement));
            return msg.isDisplayed() && msg.getText().equals(expectedText);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isSuccessMessageDisplayed(String expectedMessage) {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(messageElement));
            return msg.isDisplayed() &&
                    msg.getText().equals(expectedMessage) &&
                    msg.getCssValue("color").contains("28a745"); // Màu xanh cho thành công
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isErrorMessageDisplayed(String expectedMessage) {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(messageElement));
            return msg.isDisplayed() &&
                    msg.getText().equals(expectedMessage) &&
                    msg.getCssValue("color").contains("dc3545"); // Màu đỏ cho lỗi
        } catch (Exception e) {
            return false;
        }
    }


    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        enterOldPassword(oldPassword);
        enterNewPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        clickSaveButton();
    }


    public void clearAllFields() {
        driver.findElement(oldPasswordInput).clear();
        driver.findElement(newPasswordInput).clear();
        driver.findElement(confirmPasswordInput).clear();
    }
}