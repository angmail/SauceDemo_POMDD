package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportSetup {
    private static ExtentReports extent;

    public static ExtentReports getExtentReports() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
            spark.config().setDocumentTitle("SauceDemo Automation Report");
            spark.config().setReportName("Test Execution Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}