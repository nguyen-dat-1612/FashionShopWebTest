package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrdersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By ordersList = By.id("orders-list");
    private final By loadingIndicator = By.className("loading");
    private final By orderCards = By.className("order-card");
    private final By filterButtons = By.className("filter-btn");
    private final By pendingFilterButton = By.cssSelector(".filter-btn[data-status='PENDING']");
    private final By confirmedFilterButton = By.cssSelector(".filter-btn[data-status='CONFIRMED']");
    private final By deliveringFilterButton = By.cssSelector(".filter-btn[data-status='DELIVERING']");
    private final By deliveredFilterButton = By.cssSelector(".filter-btn[data-status='DELIVERED']");
    private final By cancelledFilterButton = By.cssSelector(".filter-btn[data-status='CANCELLED']");
    private final By sidebarProfile = By.id("profile");
    private final By sidebarChangePassword = By.id("changePassword");
    private final By sidebarLogout = By.id("logout");
    private final By errorMessage = By.className("error");

    public OrdersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public OrdersPage open() {
        driver.get("http://localhost:5500/src/main/webapp/pages/orders.html");
        return this;
    }

    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ordersList));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoading() {
        try {
            return driver.findElement(loadingIndicator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForLoading() {
        try {
            // Wait for loading to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(loadingIndicator));
            // Wait for loading to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getOrderCount() {
        try {
            List<WebElement> orders = driver.findElements(orderCards);
            return orders.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoOrdersMessageDisplayed() {
        try {
            WebElement ordersListElem = driver.findElement(ordersList);
            return ordersListElem.getText().contains("Không có đơn hàng nào");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public OrdersPage filterByStatus(String status) {
        WebElement button;
        switch (status.toUpperCase()) {
            case "PENDING":
                button = wait.until(ExpectedConditions.elementToBeClickable(pendingFilterButton));
                break;
            case "CONFIRMED":
                button = wait.until(ExpectedConditions.elementToBeClickable(confirmedFilterButton));
                break;
            case "DELIVERING":
                button = wait.until(ExpectedConditions.elementToBeClickable(deliveringFilterButton));
                break;
            case "DELIVERED":
                button = wait.until(ExpectedConditions.elementToBeClickable(deliveredFilterButton));
                break;
            case "CANCELLED":
                button = wait.until(ExpectedConditions.elementToBeClickable(cancelledFilterButton));
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
        button.click();
        return this;
    }

    public boolean isFilterButtonActive(String status) {
        By locator;
        switch (status.toUpperCase()) {
            case "PENDING":
                locator = pendingFilterButton;
                break;
            case "CONFIRMED":
                locator = confirmedFilterButton;
                break;
            case "DELIVERING":
                locator = deliveringFilterButton;
                break;
            case "DELIVERED":
                locator = deliveredFilterButton;
                break;
            case "CANCELLED":
                locator = cancelledFilterButton;
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
        WebElement button = driver.findElement(locator);
        return button.getAttribute("class").contains("active");
    }

    public String getCurrentActiveFilter() {
        List<WebElement> buttons = driver.findElements(filterButtons);
        for (WebElement button : buttons) {
            if (button.getAttribute("class").contains("active")) {
                return button.getAttribute("data-status");
            }
        }
        return "";
    }

    public OrdersPage clickOrderCard(int index) {
        List<WebElement> orders = driver.findElements(orderCards);
        if (index >= 0 && index < orders.size()) {
            orders.get(index).click();
        }
        return this;
    }

    public String getOrderId(int index) {
        List<WebElement> orders = driver.findElements(orderCards);
        if (index >= 0 && index < orders.size()) {
            WebElement orderIdElement = orders.get(index).findElement(By.className("order-id"));
            String text = orderIdElement.getText();
            // Extract order ID using regex or string operations
            return text.replace("Đơn hàng #", "");
        }
        return "";
    }

    public String getOrderStatus(int index) {
        List<WebElement> orders = driver.findElements(orderCards);
        if (index >= 0 && index < orders.size()) {
            WebElement statusElement = orders.get(index).findElement(By.className("order-status"));
            return statusElement.getText().trim();
        }
        return "";
    }

    public boolean isOrderStatusMatchingFilter() {
        String currentFilter = getCurrentActiveFilter();
        List<WebElement> orders = driver.findElements(orderCards);

        // If no orders, return true (nothing to check)
        if (orders.isEmpty()) return true;

        for (WebElement order : orders) {
            WebElement statusElement = order.findElement(By.className("order-status"));
            String statusText = statusElement.getText().trim();

            String statusCode;
            switch (statusText) {
                case "ĐANG CHỜ": statusCode = "PENDING"; break;
                case "ĐÃ XÁC NHẬN": statusCode = "CONFIRMED"; break;
                case "ĐANG GIAO": statusCode = "DELIVERING"; break;
                case "ĐÃ GIAO": statusCode = "DELIVERED"; break;
                case "ĐÃ HỦY": statusCode = "CANCELLED"; break;
                default: statusCode = ""; break;
            }

            if (!statusCode.equals(currentFilter)) {
                return false;
            }
        }
        return true;
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
}
