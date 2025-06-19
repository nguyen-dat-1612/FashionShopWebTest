package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderDetailPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By orderId = By.id("order-id");
    private final By orderDate = By.id("order-date");
    private final By orderStatus = By.id("order-status");
    private final By orderTotal = By.id("order-total");
    private final By orderItems = By.id("order-items");
    private final By orderItemRows = By.cssSelector("#order-items tr");
    private final By cancelOrderButton = By.className("btn-cancel");
    private final By backButton = By.className("btn-back");
    private final By errorMessage = By.className("error");
    private final By sidebarProfile = By.id("profile");
    private final By sidebarChangePassword = By.id("changePassword");
    private final By sidebarLogout = By.id("logout");

    public OrderDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public OrderDetailPage open(String orderId) {
        driver.get("http://localhost:5500/src/main/webapp/pages/order-detail.html?id=" + orderId);
        return this;
    }

    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderItems));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForPageToLoad() {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(
                    driver.findElement(orderItems), "Đang tải...")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderId() {
        return driver.findElement(orderId).getText().trim();
    }

    public String getOrderDate() {
        return driver.findElement(orderDate).getText().trim();
    }

    public String getOrderStatus() {
        return driver.findElement(orderStatus).getText().trim();
    }

    public String getOrderTotal() {
        return driver.findElement(orderTotal).getText().trim();
    }

    public int getOrderItemCount() {
        List<WebElement> items = driver.findElements(orderItemRows);
        return items.size();
    }

    public boolean isCancelButtonDisplayed() {
        try {
            return driver.findElement(cancelOrderButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public OrderDetailPage clickCancelOrder() {
        if (isCancelButtonDisplayed()) {
            driver.findElement(cancelOrderButton).click();
        }
        return this;
    }

    public OrderDetailPage clickBackButton() {
        driver.findElement(backButton).click();
        return this;
    }

    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        if (isErrorDisplayed()) {
            return driver.findElement(errorMessage).getText().trim();
        }
        return "";
    }

    public boolean acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dismissAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void navigateToProfile() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(sidebarProfile));
        button.click();
    }

    public void navigateToChangePassword() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(sidebarChangePassword));
        button.click();
    }

    public void logout() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(sidebarLogout));
        button.click();
    }

    public String getProductInfo(int rowIndex, int columnIndex) {
        List<WebElement> rows = driver.findElements(orderItemRows);
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            List<WebElement> columns = rows.get(rowIndex).findElements(By.tagName("td"));
            if (columnIndex >= 0 && columnIndex < columns.size()) {
                return columns.get(columnIndex).getText().trim();
            }
        }
        return "";
    }

    public String getProductName(int rowIndex) {
        return getProductInfo(rowIndex, 0);
    }

    public String getProductSize(int rowIndex) {
        return getProductInfo(rowIndex, 1);
    }

    public String getProductColor(int rowIndex) {
        return getProductInfo(rowIndex, 2);
    }

    public String getProductQuantity(int rowIndex) {
        return getProductInfo(rowIndex, 3);
    }

    public String getProductPrice(int rowIndex) {
        return getProductInfo(rowIndex, 4);
    }

    public String getProductTotal(int rowIndex) {
        return getProductInfo(rowIndex, 5);
    }
    public boolean isCancelOrderModalVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notification-modal")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getModalTitle() {
        return driver.findElement(By.id("modal-title")).getText();
    }

    public String getModalMessage() {
        return driver.findElement(By.id("modal-message")).getText();
    }

    public boolean isModalConfirmVisible() {
        return driver.findElement(By.id("modal-confirm")).isDisplayed();
    }

    public boolean isModalCancelVisible() {
        return driver.findElement(By.id("modal-cancel")).isDisplayed();
    }

    public void clickModalConfirm() {
        driver.findElement(By.id("modal-confirm")).click();
    }

    public void clickModalCancel() {
        driver.findElement(By.id("modal-cancel")).click();
    }
}