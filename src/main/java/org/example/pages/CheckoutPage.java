package org.example.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By phoneField = By.id("phone");
    private final By addressField = By.id("address");
    private final By discountCodeField = By.id("discountCode");
    private final By applyDiscountButton = By.id("applyDiscount");
    private final By confirmOrderButton = By.id("confirmOrder");
    private final By paymentMethods = By.name("payment");
    private final By codPayment = By.id("cod");
    private final By vnpayPayment = By.id("vnpay");
    private final By momoPayment = By.id("momo");
    private final By creditCardPayment = By.id("credit_card");
    private final By orderItems = By.id("orderItems");
    private final By subTotal = By.id("subTotal");
    private final By shippingFee = By.id("shippingFee");
    private final By discount = By.id("discount");
    private final By totalPrice = By.id("totalPrice");
    private final By orderSuccessModal = By.id("orderSuccessModal");
    private final By goToHomeButton = By.id("goToHomeBtn");
    private final By viewOrderButton = By.id("viewOrderBtn");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CheckoutPage open() {
        driver.get("http://localhost:5500/src/main/webapp/pages/checkout.html");
        return this;
    }

    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmOrderButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getOrderItemCount() {
        try {
            List<WebElement> items = driver.findElements(By.className("order-item"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSubTotal() {
        return driver.findElement(subTotal).getText().trim();
    }

    public String getShippingFee() {
        return driver.findElement(shippingFee).getText().trim();
    }

    public String getDiscount() {
        return driver.findElement(discount).getText().trim();
    }

    public String getTotalPrice() {
        return driver.findElement(totalPrice).getText().trim();
    }

    public CheckoutPage enterPhone(String phoneNumber) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField));
        field.clear();
        field.sendKeys(phoneNumber);
        return this;
    }

    public CheckoutPage enterAddress(String addressText) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(addressField));
        field.clear();
        field.sendKeys(addressText);
        return this;
    }

    public CheckoutPage enterDiscountCode(String code) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(discountCodeField));
        field.clear();
        field.sendKeys(code);
        return this;
    }

    public CheckoutPage applyDiscountCode() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(applyDiscountButton));
        button.click();
        return this;
    }

    public CheckoutPage selectPaymentMethod(String method) {
        switch (method.toUpperCase()) {
            case "COD":
                driver.findElement(codPayment).click();
                break;
            case "VNPAY":
                driver.findElement(vnpayPayment).click();
                break;
            case "MOMO":
                driver.findElement(momoPayment).click();
                break;
            case "CREDIT_CARD":
                driver.findElement(creditCardPayment).click();
                break;
        }
        return this;
    }

    public CheckoutPage confirmOrder() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
        button.click();
        return this;
    }

    public boolean isOrderSuccessModalDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(orderSuccessModal)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickGoToHomeButton() {
        if (isOrderSuccessModalDisplayed()) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(goToHomeButton));
            button.click();
        }
    }

    public void clickViewOrderButton() {
        if (isOrderSuccessModalDisplayed()) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(viewOrderButton));
            button.click();
        }
    }

    public String getOrderId() {
        if (isOrderSuccessModalDisplayed()) {
            WebElement modalBody = driver.findElement(orderSuccessModal).findElement(By.className("modal-body"));
            String text = modalBody.getText();
            // Extract order ID using regex
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Mã đơn hàng của bạn: ([A-Za-z0-9]+)");
            java.util.regex.Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    public boolean isErrorAlertPresent() {
        try {
            return driver.switchTo().alert().getText().contains("lỗi");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Ignore if no alert is present
        }
    }
}