package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CartPage {
    private WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Kiểm tra một sản phẩm có được chọn không
     * @param variantId ID của biến thể sản phẩm
     * @return true nếu sản phẩm được chọn
     */
    public boolean isItemSelected(int variantId) {
        try {
            // Tìm phần tử cart-item chứa variant ID này
            WebElement cartItem = driver.findElement(
                    By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));

            // Kiểm tra nếu cartItem tồn tại, tức là sản phẩm có trong giỏ hàng
            return cartItem != null && cartItem.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Kiểm tra checkbox của sản phẩm có được chọn không
     * @param variantId ID của biến thể sản phẩm
     * @return true nếu checkbox được chọn
     */
    public boolean isItemCheckboxChecked(int variantId) {
        try {
            // Tìm cart-item chứa variant ID cần kiểm tra
            WebElement cartItem = driver.findElement(
                    By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));

            // Tìm checkbox trong cart-item đó
            WebElement checkbox = cartItem.findElement(By.cssSelector(".item-checkbox"));

            // Kiểm tra trạng thái của checkbox
            return checkbox.isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Kiểm tra sản phẩm có class selected-highlight không
     * @param variantId ID của biến thể sản phẩm
     * @return true nếu sản phẩm có class selected-highlight
     */
    public boolean hasSelectedHighlight(int variantId) {
        try {
            // Tìm cart-item chứa variant ID cần kiểm tra
            WebElement cartItem = driver.findElement(
                    By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));

            // Kiểm tra xem phần tử có class selected-highlight không
            String classes = cartItem.getAttribute("class");
            return classes != null && classes.contains("selected-highlight");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Lấy thông tin màu sắc của sản phẩm
     * @param variantId ID của biến thể sản phẩm
     * @return Chuỗi thể hiện màu sắc
     */
    public String getProductColor(int variantId) {
        WebElement cartItem = driver.findElement(
                By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));
        return cartItem.findElement(By.cssSelector(".cart-item-details .me-2")).getText().replace("Màu: ", "");
    }

    /**
     * Lấy thông tin kích thước của sản phẩm
     * @param variantId ID của biến thể sản phẩm
     * @return Chuỗi thể hiện kích thước
     */
    public String getProductSize(int variantId) {
        WebElement cartItem = driver.findElement(
                By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));
        return cartItem.findElement(By.cssSelector(".cart-item-details span:last-child")).getText().replace("Size: ", "");
    }

    /**
     * Lấy số lượng sản phẩm
     * @param variantId ID của biến thể sản phẩm
     * @return Số lượng sản phẩm
     */
    public int getProductQuantity(int variantId) {
        WebElement cartItem = driver.findElement(
                By.cssSelector(String.format(".cart-item[data-variant-id='%d']", variantId)));
        String quantityText = cartItem.findElement(By.cssSelector(".cart-item-quantity span")).getText();
        return Integer.parseInt(quantityText);
    }
}
