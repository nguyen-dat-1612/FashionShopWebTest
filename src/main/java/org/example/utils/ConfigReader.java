package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    private ConfigReader() {
        // Private constructor để ngăn khởi tạo
    }

    /**
     * Đọc file cấu hình
     */
    public static void loadConfig() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Không thể đọc file cấu hình: " + e.getMessage());
        }
    }

    /**
     * Lấy giá trị cấu hình theo key
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadConfig();
        }
        return properties.getProperty(key);
    }

    /**
     * Lấy giá trị cấu hình theo key, nếu không có thì trả về giá trị mặc định
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadConfig();
        }
        return properties.getProperty(key, defaultValue);
    }
}