package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Đợi cho element hiển thị
     */
    protected WebElement waitForElementVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Đợi cho element có thể click được
     */
    protected WebElement waitForElementClickable(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Kiểm tra element có hiển thị không
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Kiểm tra element có tồn tại không
     */
    protected boolean isElementExists(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Click vào element
     */
    protected void click(By locator) {
        waitForElementClickable(locator, 10).click();
    }

    /**
     * Điền text vào element
     */
    protected void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator, 10);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Lấy text của element
     */
    protected String getText(By locator) {
        return waitForElementVisible(locator, 10).getText();
    }

    /**
     * Lấy attribute của element
     */
    protected String getAttribute(By locator, String attribute) {
        return waitForElementVisible(locator, 10).getAttribute(attribute);
    }

    protected void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        element.click();
    }

    protected List<WebElement> waitForAllElementsVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

}
