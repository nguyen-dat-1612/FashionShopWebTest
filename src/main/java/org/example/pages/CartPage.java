package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By selectAllCheckbox = By.id("select-all");
    private final By cartItemsContainer = By.id("cart-items");
    private final By cartItems = By.className("cart-item");
    private final By itemCheckboxes = By.className("item-checkbox");
    private final By subtotalValue = By.id("subtotal");
    private final By shippingValue = By.id("shipping");
    private final By totalValue = By.id("total");
    private final By checkoutButton = By.id("checkout-button");
    private final By emptyCartMessage = By.xpath("//h4[contains(text(), 'Giỏ hàng của bạn đang trống')]");
    private final By continueShoppingButton = By.xpath("//a[contains(text(), 'Tiếp tục mua sắm')]");
    private final By summaryCard = By.className("summary-card");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CartPage open() {
        driver.get("http://localhost:5500/src/main/webapp/pages/cart.html");
        return this;
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemsContainer));
    }

    public boolean isCartPageLoaded() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(cartItems),
                    ExpectedConditions.visibilityOfElementLocated(emptyCartMessage)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmptyCartDisplayed() {
        try {
            return driver.findElement(emptyCartMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSummaryCardDisplayed() {
        try {
            return driver.findElement(summaryCard).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void selectAllItems() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(selectAllCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void deselectAllItems() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(selectAllCheckbox));
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void selectItem(int itemIndex) {
        List<WebElement> checkboxes = driver.findElements(itemCheckboxes);
        if (itemIndex >= 0 && itemIndex < checkboxes.size()) {
            WebElement checkbox = checkboxes.get(itemIndex);
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    public void deselectItem(int itemIndex) {
        List<WebElement> checkboxes = driver.findElements(itemCheckboxes);
        if (itemIndex >= 0 && itemIndex < checkboxes.size()) {
            WebElement checkbox = checkboxes.get(itemIndex);
            if (checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    public void increaseQuantity(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement quantityControls = item.findElement(By.cssSelector(".cart-item-quantity .quantity-controls"));
            List<WebElement> increaseButton = quantityControls.findElements(By.tagName("button"));

            // Lưu lại tổng trước khi click
            WebElement total = driver.findElement(totalValue);
            String oldTotal = total.getText();

            increaseButton.get(1).click();

            // Chờ tổng tiền thay đổi
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElementLocated(totalValue, oldTotal)
            ));
        }
    }

    public void decreaseQuantity(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement quantityControls = item.findElement(By.cssSelector(".cart-item-quantity .quantity-controls"));
            List<WebElement> decreaseButton = quantityControls.findElements(By.tagName("button"));

            // Lưu lại tổng tiền trước khi giảm
            WebElement total = driver.findElement(totalValue);
            String oldTotal = total.getText();

            decreaseButton.get(0).click();

            // Chờ tổng tiền thay đổi
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElementLocated(totalValue, oldTotal)
            ));
        }
    }

    public void removeItem(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement removeButton = item.findElement(By.xpath(".//button[text()='Xóa']"));
            removeButton.click();

            // Accept the confirmation dialog
            driver.switchTo().alert().accept();

            // Wait for the removal operation to complete
            wait.until(ExpectedConditions.invisibilityOf(item));
        }
    }

    public int getQuantity(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement quantityElement = item.findElement(By.className("px-2"));
//            WebElement quantityElement = item.findElement(By.xpath(".//div[@class='cart-item-quantity']//span"));
            return Integer.parseInt(quantityElement.getText().trim());
        }
        return -1;
    }

    public String getItemName(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement nameElement = item.findElement(By.className("mb-1"));
            return nameElement.getText().trim();
        }
        return "";
    }

    public String getItemPrice(int itemIndex) {
        List<WebElement> items = driver.findElements(cartItems);
        if (itemIndex >= 0 && itemIndex < items.size()) {
            WebElement item = items.get(itemIndex);
            WebElement priceElement = item.findElement(By.xpath(".//div[@class='cart-item-total']/span"));
            return priceElement.getText().trim();
        }
        return "";
    }

    public String getSubtotal() {
        return driver.findElement(subtotalValue).getText().trim();
    }

    public String getShipping() {
        return driver.findElement(shippingValue).getText().trim();
    }

    public String getTotal() {
        return driver.findElement(totalValue).getText().trim();
    }

    public void clickCheckoutButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        button.click();
    }

    public void clickContinueShopping() {
        if (isEmptyCartDisplayed()) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
            button.click();
        }
    }

    public List<Boolean> getSelectedItems() {
        List<WebElement> checkboxes = driver.findElements(itemCheckboxes);
        return checkboxes.stream().map(WebElement::isSelected).collect(Collectors.toList());
    }

    public boolean isAllItemsSelected() {
        List<Boolean> selectedStatus = getSelectedItems();
        return !selectedStatus.isEmpty() && selectedStatus.stream().allMatch(selected -> selected);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("alert-danger")));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("alert-success")));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
