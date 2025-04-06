package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import static io.reactivex.rxjava3.internal.util.NotificationLite.getValue;

public class ProductDetailPage extends BasePage {

    // Locators
    private final By productImage = By.id("productImage");
    private final By productName = By.id("productName");
    private final By productPrice = By.id("productPrice");
    private final By productStock = By.id("productStock");
    private final By productRating = By.id("productRating");
    private final By productDescription = By.id("productDescription");
    private final By colorOptions = By.cssSelector("#colorOptions button");
    private final By sizeOptions = By.cssSelector("#sizeOptions button");
    private final By quantityInput = By.id("quantity");
    private final By increaseQtyBtn = By.id("increaseQty");
    private final By decreaseQtyBtn = By.id("decreaseQty");
    private final By addToCartBtn = By.id("addToCartBtn");
    private final By buyNowBtn = By.id("buyNowBtn");
    private final By mainDealItem = By.cssSelector(".deal-item.main-item");
    private final By bundleItems = By.cssSelector(".bundle-items .deal-item");
    private final By comboTotal = By.id("comboTotal");
    private final By buyDealBtn = By.cssSelector(".bundle-summary .btn-buy-now");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Đợi cho trang chi tiết sản phẩm tải xong
     */
    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
    }

    /**
     * Kiểm tra ảnh sản phẩm hiển thị
     */
    public boolean isProductImageDisplayed() {
        return isElementDisplayed(productImage);
    }

    /**
     * Lấy tên sản phẩm
     */
    public String getProductName() {
        return getText(productName);
    }

    /**
     * Lấy giá sản phẩm
     */
    public String getProductPrice() {
        return getText(productPrice);
    }

    /**
     * Chọn màu sắc theo index
     */
    public void selectColor(int index) {
        List<WebElement> colors = waitForAllElementsVisible(colorOptions);
        if (index >= 0 && index < colors.size()) {
            colors.get(index).click();
        }
    }

    /**
     * Kiểm tra màu có được chọn không
     */
    public boolean isColorSelected(int index) {
        List<WebElement> colors = driver.findElements(colorOptions);
        if (index >= 0 && index < colors.size()) {
            return colors.get(index).getAttribute("class").contains("active");
        }
        return false;
    }

    /**
     * Chọn kích thước theo index
     */
    public void selectSize(int index) {
        List<WebElement> sizes = waitForAllElementsVisible(sizeOptions);
        if (index >= 0 && index < sizes.size()) {
            sizes.get(index).click();
        }
    }

    /**
     * Kiểm tra kích thước có được chọn không
     */
    public boolean isSizeSelected(int index) {
        List<WebElement> sizes = driver.findElements(sizeOptions);
        if (index >= 0 && index < sizes.size()) {
            return sizes.get(index).getAttribute("class").contains("active");
        }
        return false;
    }

    /**
     * Tăng số lượng sản phẩm
     */
    public void increaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(increaseQtyBtn);
        }
    }

    /**
     * Giảm số lượng sản phẩm
     */
    public void decreaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(decreaseQtyBtn);
        }
    }

    public String getValue(By locator) {
        return driver.findElement(locator).getAttribute("value");
    }
    /**
     * Lấy số lượng hiện tại
     */
    public int getCurrentQuantity() {
        return Integer.parseInt(getValue(quantityInput));
    }

    /**
     * Click nút Thêm vào giỏ hàng
     */
    public void clickAddToCart() {
        scrollAndClick(addToCartBtn);
    }

    /**
     * Click nút Mua ngay
     */
    public void clickBuyNow() {
        scrollAndClick(buyNowBtn);
    }

    /**
     * Chọn sản phẩm bundle theo index
     */
    public void selectBundleItem(int index) {
        List<WebElement> items = waitForAllElementsVisible(bundleItems);
        if (index >= 0 && index < items.size()) {
            WebElement checkbox = items.get(index).findElement(By.cssSelector(".deal-checkbox"));
            checkbox.click(); // Gọi trực tiếp
        }
    }

    /**
     * Lấy tổng giá trị combo
     */
    public String getComboTotal() {
        return getText(comboTotal);
    }

    /**
     * Click nút mua deal sốc
     */
    public void clickBuyDeal() {
        scrollAndClick(buyDealBtn);
    }

    /**
     * Kiểm tra section deal sốc hiển thị
     */
    public boolean isDealSectionDisplayed() {
        return isElementDisplayed(mainDealItem);
    }
}