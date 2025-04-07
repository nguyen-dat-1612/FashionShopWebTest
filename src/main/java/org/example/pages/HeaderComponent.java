package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

public class HeaderComponent {
    private WebDriver webDriver;
    private final WebDriverWait wait;

    // Locator
    private final By loginBtn = By.cssSelector(".btn.btn-outline-primary.me-2");
    private final By registerBtn = By.cssSelector(".btn.btn-primary");
    private final By loginEmail = By.id("loginEmail");
    private final By loginPassword = By.id("loginPassword");
    private final By submitBtnLogin = By.cssSelector("#loginForm button[type='submit']");
    private final By submitBtnRegister = By.id("registerBtn");
    private final By profileIcon = By.id("profileIcon");
    private final By cartIcon = By.id("cartIcon");
    private final By loginModal = By.id("loginModal");
    private final By registerModal = By.id("registerModal");
    private final By loginBtnVisible = By.cssSelector(".btn.btn-outline-primary.me-2:not([style*='display:none'])");
    private final By registerBtnVisible = By.cssSelector(".btn.btn-primary:not([style*='display:none'])");
    private final By loginMessageVisible = By.id("loginMessage");
    private final By registerMessageVisible = By.id("registerMessage");
    public HeaderComponent(WebDriver driver) {
        this.webDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Click vào nút login
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public void clickProfileIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(profileIcon)).click();
    }

    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }

    public boolean isLoginModalDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtnVisible)).isDisplayed();
    }

    public boolean isRegisterModalDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(registerBtnVisible)).isDisplayed();
    }

    public void fillLoginForm(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmail)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPassword)).sendKeys(password);
    }

    public void submitLoginForm() {
        webDriver.findElement(submitBtnLogin).click();
    }
    public void fillRegisterForm(String fullName, String email, String password, String confirmPassword) {
        WebElement registerForm = wait.until(ExpectedConditions.visibilityOfElementLocated(registerModal));
        registerForm.findElement(By.id("registerFullname")).sendKeys(fullName);
        registerForm.findElement(By.id("registerEmail")).sendKeys(email);
        registerForm.findElement(By.id("registerPassword")).sendKeys(password);
        registerForm.findElement(By.id("confirmPassword")).sendKeys(confirmPassword);
    }

    public void submitRegisterForm() {
        webDriver.findElement(submitBtnRegister).click();
    }

    public boolean isProfileIconDisplayed() {
        try {
            return webDriver.findElement(profileIcon).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCartIconDisplayed() {
        try {
            return webDriver.findElement(cartIcon).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Và các phương thức kiểm tra
    public boolean isLoginButtonDisplayed() {
        try {
            return webDriver.findElement(loginBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRegisterButtonDisplayed() {
        try {
            return webDriver.findElement(registerBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            return webDriver.findElement(loginMessageVisible).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isRegisterSuccessMessageDisplayed() {
        try {
            WebElement successMessage = webDriver.findElement(By.cssSelector(".alert-success"));

            // Scroll vào tầm nhìn nếu cần
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", successMessage);

            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRegisterErrorMessageDisplayed() {
        try {
            WebElement message = webDriver.findElement(registerMessageVisible);
            return message.isDisplayed() && message.getAttribute("class").contains("alert-danger");
        } catch (Exception e) {
            return false;
        }
    }

}
