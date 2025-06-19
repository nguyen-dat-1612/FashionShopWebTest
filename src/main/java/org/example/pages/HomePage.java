package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage {

    // Locators
    private final By carousel = By.id("heroCarousel");
    private final By carouselCaption = By.className("carousel-caption");
    private final By categorySection = By.className("categories");
    private final By categorySectionTitle = By.cssSelector(".categories .section-title");
    private final By categoryList = By.id("category-list");
    private final By categoryCards = By.cssSelector("#category-list .category-card");
    private final By productSection = By.className("featured-products");
    private final By productSectionTitle = By.cssSelector(".featured-products .section-title");
    private final By productList = By.id("products-list");
    private final By productCards = By.cssSelector("#products-list .product-card");
    private final By header = By.id("header");
    private final By footer = By.id("footer");
    private final By noResultMessage = By.cssSelector("p.no-results");

    private HeaderComponent headerComponent;

    public HomePage(WebDriver driver) {
        super(driver);
        this.headerComponent = new HeaderComponent(driver);
    }

    /**
     * Đợi cho trang tải xong
     */
    public void waitForPageToLoad() {
        // Đợi carousel xuất hiện như một chỉ báo trang đã tải
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(carousel));
    }

    /**
     * Kiểm tra carousel hiển thị
     */
    public boolean isCarouselDisplayed() {
        return isElementDisplayed(carousel);
    }

    /**
     * Kiểm tra carousel caption hiển thị
     */
    public boolean isCarouselCaptionDisplayed() {
        return isElementDisplayed(carouselCaption);
    }

    /**
     * Kiểm tra phần danh mục sản phẩm hiển thị
     */
    public boolean isCategorySectionDisplayed() {
        return isElementDisplayed(categorySection);
    }

    /**
     * Lấy tiêu đề phần danh mục
     */
    public String getCategorySectionTitle() {
        return driver.findElement(categorySectionTitle).getText();
    }

    /**
     * Kiểm tra phần sản phẩm nổi bật hiển thị
     */
    public boolean isProductSectionDisplayed() {
        return isElementDisplayed(productSection);
    }

    /**
     * Lấy tiêu đề phần sản phẩm
     */
    public String getProductSectionTitle() {
        return driver.findElement(productSectionTitle).getText();
    }

    /**
     * Đợi danh mục được tải
     */
    public void waitForCategoriesLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(driver -> !driver.findElements(categoryCards).isEmpty());
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for categories to load: " + e.getMessage());
        }
    }

    /**
     * Đợi danh sách sản phẩm được tải
     */
    public void waitForProductsLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(driver -> !driver.findElements(productCards).isEmpty());
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for products to load: " + e.getMessage());
        }
    }

    /**
     * Lấy số lượng danh mục
     */
    public int getCategoriesCount() {
        return driver.findElements(categoryCards).size();
    }

    /**
     * Lấy số lượng sản phẩm
     */
    public int getProductsCount() {
        return driver.findElements(productCards).size();
    }

    /**
     * Kiểm tra hình ảnh sản phẩm hiển thị
     */
    public boolean isProductImageDisplayed(int index) {
        List<WebElement> products = driver.findElements(productCards);
        if (index < products.size()) {
            WebElement productCard = products.get(index);
            WebElement image = productCard.findElement(By.tagName("img"));
            return image.isDisplayed();
        }
        return false;
    }

    /**
     * Kiểm tra tên sản phẩm hiển thị
     */
    public boolean isProductNameDisplayed(int index) {
        List<WebElement> products = driver.findElements(productCards);
        if (index < products.size()) {
            WebElement productCard = products.get(index);
            WebElement name = productCard.findElement(By.tagName("h5"));
            return name.isDisplayed();
        }
        return false;
    }

    /**
     * Kiểm tra giá sản phẩm hiển thị
     */
    public boolean isProductPriceDisplayed(int index) {
        List<WebElement> products = driver.findElements(productCards);
        if (index < products.size()) {
            WebElement productCard = products.get(index);
            WebElement price = productCard.findElement(By.tagName("p"));
            return price.isDisplayed();
        }
        return false;
    }

    /**
     * Click vào sản phẩm
     */
    public void clickProductCard(int index) {
        // Tìm tất cả các sản phẩm
        List<WebElement> products = driver.findElements(By.cssSelector("#products-list .product-card"));

        if (index < products.size()) {
            WebElement product = products.get(index);

            // Cuộn đến phần tử trước khi click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", product);

            // Đợi một chút sau khi cuộn
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

            // Sử dụng JavaScript để click thay vì click trực tiếp
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);
        } else {
            throw new IndexOutOfBoundsException("Không tìm thấy sản phẩm với index: " + index);
        }
    }

    /**
     * Đợi cho URL thay đổi
     */
    public void waitForUrlChange(String oldUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(oldUrl)));
    }

    /**
     * Kiểm tra header được tải
     */
    public boolean isHeaderLoaded() {
        WebElement headerElement = driver.findElement(header);
        return !headerElement.getText().isEmpty();
    }


    public boolean isSearchNoResultDisplayed() {
        try {
            return driver.findElement(noResultMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Kiểm tra footer được tải
     */
    public boolean isFooterLoaded() {
        WebElement footerElement = driver.findElement(footer);
        return !footerElement.getText().isEmpty();
    }

    public HeaderComponent getHeader() {
        return headerComponent;
    }
}