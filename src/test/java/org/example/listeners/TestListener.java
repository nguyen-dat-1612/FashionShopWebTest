package org.example.listeners;

import org.example.utils.DriverManager;
import org.example.utils.TestUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.logging.Logger;

public class TestListener implements ITestListener {
    private static final Logger LOGGER = Logger.getLogger(TestListener.class.getName());

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("===============================================================");
        LOGGER.info("Test Suite " + context.getName() + " bắt đầu");
        LOGGER.info("===============================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("===============================================================");
        LOGGER.info("Test Suite " + context.getName() + " kết thúc");
        LOGGER.info("Total tests run: " + context.getAllTestMethods().length);
        LOGGER.info("Passed tests: " + context.getPassedTests().size());
        LOGGER.info("Failed tests: " + context.getFailedTests().size());
        LOGGER.info("Skipped tests: " + context.getSkippedTests().size());
        LOGGER.info("===============================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("---------------");
        LOGGER.info("Đang chạy test: " + result.getMethod().getMethodName());
        LOGGER.info("Mô tả: " + result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test " + result.getMethod().getMethodName() + " đã PASS");
        LOGGER.info("Thời gian chạy: " + (result.getEndMillis() - result.getStartMillis()) + " ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.severe("Test " + result.getMethod().getMethodName() + " đã FAIL");
        LOGGER.severe("Lỗi: " + result.getThrowable().getMessage());

        // Chụp ảnh màn hình khi test fail
        try {
            WebDriver driver = DriverManager.getDriver();
            String screenshotPath = TestUtils.takeScreenshot(driver, result.getMethod().getMethodName());
            LOGGER.info("Screenshot đã được lưu tại: " + screenshotPath);
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi chụp màn hình: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warning("Test " + result.getMethod().getMethodName() + " đã bị BỎ QUA");
        if (result.getThrowable() != null) {
            LOGGER.warning("Lý do: " + result.getThrowable().getMessage());
        }
    }
}
