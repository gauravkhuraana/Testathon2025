package utils;

import config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for test execution events
 */
public class TestListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("========================================");
        System.out.println("Starting Test: " + result.getMethod().getMethodName());
        System.out.println("Test Class: " + result.getTestClass().getName());
        System.out.println("========================================");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("========================================");
        System.out.println("✅ Test PASSED: " + result.getMethod().getMethodName());
        System.out.println("Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + " ms");
        System.out.println("========================================");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("========================================");
        System.out.println("❌ Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("Failure Reason: " + result.getThrowable().getMessage());
        System.out.println("========================================");
        
        // Take screenshot on failure if enabled
        if (ConfigManager.isScreenshotOnFailureEnabled()) {
            takeScreenshotOnFailure(result);
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("========================================");
        System.out.println("⏭️ Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            System.out.println("Skip Reason: " + result.getThrowable().getMessage());
        }
        System.out.println("========================================");
    }
    
    private void takeScreenshotOnFailure(ITestResult result) {
        try {
            WebDriver driver = WebDriverFactory.getDriver();
            if (driver != null) {
                String testName = result.getMethod().getMethodName();
                String screenshotPath = SeleniumUtils.takeScreenshot(driver, testName + "_FAILED");
                if (screenshotPath != null) {
                    System.out.println("Screenshot captured: " + screenshotPath);
                    
                    // Set screenshot path in test result for reporting tools
                    System.setProperty("screenshot.path", screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot on failure: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test failed but within success percentage: " + result.getMethod().getMethodName());
    }
}
