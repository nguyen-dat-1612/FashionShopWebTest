package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductDetailPage extends BasePage {

    // Locators
    private final By productImage = By.id("productImage");
    private final By productName = By.id("productName");
    private final By productPrice = By.id("productPrice");
    private final By colorOptions = By.cssSelector(".color-options button");
    private final By sizeOptions = By.cssSelector(".size-options button");
    private final By quantityInput = By.id("quantity");
    private final By increaseQtyBtn = By.id("increaseQty");
    private final By decreaseQtyBtn = By.id("decreaseQty");
    private final By addToCartBtn = By.id("addToCartBtn");
    private final By buyNowBtn = By.id("buyNowBtn");
    private final By cartCountLocator = By.id("cartCount");
    private final By alertLocator = By.id("loginAlert");
    private final By productStockLocator = By.id("productStock");

    private final WebDriverWait wait;
    private final HeaderComponent headerComponent;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.headerComponent = new HeaderComponent(driver);
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
    }

    public boolean isProductImageDisplayed() {
        return isElementDisplayed(productImage);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public void selectOption(By optionLocator, int index) {
        List<WebElement> options = waitForAllElementsVisible(optionLocator);
        if (index >= 0 && index < options.size()) {
            WebElement option = options.get(index);
            if (option.isEnabled()) {
                option.click();
                wait.until(ExpectedConditions.attributeContains(option, "class", "active"));
            }
        }
    }

    public void selectColor(int index) {
        selectOption(colorOptions, index);
    }

    public void selectSize(int index) {
        selectOption(sizeOptions, index);
    }

    public boolean isOptionSelected(By optionLocator, int index) {
        List<WebElement> options = driver.findElements(optionLocator);
        return index >= 0 && index < options.size() && options.get(index).getAttribute("class").contains("active");
    }

    public boolean isColorSelected(int index) {
        return isOptionSelected(colorOptions, index);
    }

    public boolean isSizeSelected(int index) {
        return isOptionSelected(sizeOptions, index);
    }

    public void increaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(increaseQtyBtn);
        }
    }

    public void decreaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(decreaseQtyBtn);
        }
    }

    public void setQuantity(int quantity) {
        WebElement input = driver.findElement(quantityInput);
        input.clear();
        input.sendKeys(String.valueOf(quantity));
    }

    public int getQuantity() {
        WebElement input = driver.findElement(quantityInput);
        return Integer.parseInt(input.getAttribute("value"));
    }

    public int getCurrentQuantity() {
        return getQuantity();
    }

    public void clickAddToCart() {
        scrollAndClick(addToCartBtn);
    }

    public void clickBuyNow() {
        scrollAndClick(buyNowBtn);
    }

    public int getCartCount() {
        try {
            WebElement cartCount = driver.findElement(cartCountLocator);
            String text = cartCount.getText().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isAlertMessageDisplayed(String expectedMessage) {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
            return alert.getText().contains(expectedMessage) && !alert.getAttribute("class").contains("d-none");
        } catch (Exception e) {
            return false;
        }
    }

    public int getMaxStockForCurrentVariant() {
        try {
            WebElement stockElement = driver.findElement(productStockLocator);
            String stockText = stockElement.getText();
            String[] parts = stockText.split(":");
            if (parts.length > 1) {
                return Integer.parseInt(parts[1].trim());
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public String getCurrentVariantId() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript(
                    "const selectedColor = document.querySelector('.color-options .active')?.textContent?.trim();" +
                            "const selectedSize = document.querySelector('.size-options .active')?.textContent?.trim();" +
                            "const productVariantList = window.product?.productVariantList || [];" +
                            "const selectedVariant = productVariantList.find(v => " +
                            "  v.color?.trim() === selectedColor && " +
                            "  v.size?.trim() === selectedSize" +
                            ");" +
                            "return selectedVariant ? selectedVariant.id : null;"
            );
        } catch (Exception e) {
            return null;
        }
    }

    public boolean waitAndCheckAlertMessage(String expectedText, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginAlert")));
            return alert.getText().trim().equals(expectedText);
        } catch (Exception e) {
            return false;
        }
    }
    public HeaderComponent getHeader() {
        return headerComponent;
    }
}
