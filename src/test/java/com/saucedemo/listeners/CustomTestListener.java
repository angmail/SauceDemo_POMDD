package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ExtentReportSetup;
import com.saucedemo.utils.ScreenshotUtil;
import com.saucedemo.utils.WebDriverSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(CustomTestListener.class);
    private static ExtentReports extent = ExtentReportSetup.getExtentReports();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getMethod().getMethodName());
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        test.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        test.get().log(Status.FAIL, "Test failed: " + result.getThrowable());
        String screenshotPath = ScreenshotUtil.captureScreenshot(WebDriverSetup.getDriver(), result.getMethod().getMethodName());
        try {
            test.get().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to report: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        test.get().log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("Test suite finished");
    }
}