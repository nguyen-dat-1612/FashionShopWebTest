package org.example.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {

    /**
     * Chụp ảnh màn hình khi test fail
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // Tạo timestamp để đặt tên file
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = now.format(formatter);

            // Tạo tên file
            String screenshotName = testName + "_" + timestamp + ".png";

            // Tạo thư mục screenshots nếu chưa tồn tại
            Path screenshotDir = Paths.get("test-output", "screenshots");
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            // Đường dẫn đầy đủ đến file screenshot
            Path screenshotPath = screenshotDir.resolve(screenshotName);

            // Chụp screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            // Lưu file
            Files.copy(source.toPath(), screenshotPath);

            return screenshotPath.toString();
        } catch (IOException e) {
            System.err.println("Lỗi khi chụp màn hình: " + e.getMessage());
            return null;
        }
    }

    /**
     * Thực thi JavaScript
     */
    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Cuộn đến element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        executeJavaScript(driver, "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

        // Đợi sau khi cuộn để tránh vấn đề với animation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Highlight element (hữu ích khi debug test)
     */
    public static void highlightElement(WebDriver driver, WebElement element) {
        String originalStyle = element.getAttribute("style");
        executeJavaScript(driver,
                "arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');",
                element);

        // Đợi 500ms để nhìn thấy highlight
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Khôi phục style ban đầu
        executeJavaScript(driver, "arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
    }

    /**
     * Tạo dữ liệu test ngẫu nhiên
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Đợi JavaScript hoàn thành
     */
    public static void waitForJSToComplete(WebDriver driver, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (System.currentTimeMillis() - startTime < timeoutInSeconds * 1000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Kiểm tra trạng thái JavaScript
            Boolean jsReady = (Boolean) js.executeScript("return document.readyState === 'complete' && jQuery.active === 0");

            if (Boolean.TRUE.equals(jsReady)) {
                break;
            }
        }
    }
}
