package org.example.tests;


import org.example.utils.ConfigReader;
import org.example.utils.DriverManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        // Khởi tạo driver manager và đọc config từ file
        ConfigReader.loadConfig();
        DriverManager.initDriver();
    }

    @AfterSuite
    public void afterSuite() {
        // Đóng tất cả browser sau khi test suite kết thúc
        DriverManager.quitDriver();
    }

    protected void slowDown(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}