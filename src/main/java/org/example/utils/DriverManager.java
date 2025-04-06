package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverManager {
    private static WebDriver driver;

    private DriverManager() {
        // Private constructor để ngăn khởi tạo
    }

    /**
     * Khởi tạo driver
     */
    public static void initDriver() {
        if (driver == null) {
            // Đọc đường dẫn chromedriver từ config hoặc dùng mặc định
            String driverPath = ConfigReader.getProperty("webdriver.chrome.driver");
            if (driverPath == null || driverPath.isEmpty()) {
                driverPath = "src/test/resources/drivers/chromedriver.exe";
            }

            // Thiết lập đường dẫn chromedriver
            System.setProperty("webdriver.chrome.driver", driverPath);

            // Cấu hình ChromeOptions
            ChromeOptions options = new ChromeOptions();

            // Thêm các options tùy theo nhu cầu
            // options.addArguments("--headless"); // Chạy ẩn (không hiển thị browser)
            // options.addArguments("--disable-extensions"); // Tắt extensions
            // options.addArguments("--disable-gpu"); // Tắt GPU
            options.addArguments("--remote-allow-origins=*"); // Cho phép truy cập từ xa

            // Khởi tạo ChromeDriver với options
            driver = new ChromeDriver(options);

            // Cấu hình driver
            driver.manage().window().maximize(); // Phóng to cửa sổ
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Đợi ngầm 10 giây
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Timeout tải trang 30 giây
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30)); // Timeout chạy script 30 giây
        }
    }

    /**
     * Lấy driver hiện tại
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        } else {
            try {
                driver.getTitle(); // Kiểm tra driver còn hoạt động không
            } catch (Exception e) {
                System.out.println("Driver session expired, reinitializing...");
                driver = null;
                initDriver(); // Tạo mới driver
            }
        }
        return driver;
    }

    /**
     * Đóng driver hiện tại
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Làm mới driver hiện tại
     */
    public static void refreshDriver() {
        quitDriver();
        initDriver();
    }
}