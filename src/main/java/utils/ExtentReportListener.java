package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import config.ConfigManager;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReports listener for enhanced reporting
 */
public class ExtentReportListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void onStart(org.testng.ITestContext context) {
        initializeExtentReports();
    }
    
    @Override
    public void onFinish(org.testng.ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        
        ExtentTest extentTest = extent.createTest(testName, testDescription);
        test.set(extentTest);
        
        // Add browser and OS information
        String browser = System.getProperty("browser", "Chrome");
        String os = System.getProperty("os", "Windows");
        String osVersion = System.getProperty("osVersion", "11");
        
        extentTest.assignCategory(browser);
        extentTest.assignCategory(os);
        extentTest.info("Browser: " + browser);
        extentTest.info("OS: " + os + " " + osVersion);
        extentTest.info("Test started at: " + getCurrentTimestamp());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed successfully");
        test.get().info("Test completed at: " + getCurrentTimestamp());
        test.get().info("Execution time: " + (result.getEndMillis() - result.getStartMillis()) + " ms");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        
        // Attach screenshot if available
        String screenshotPath = System.getProperty("screenshot.path");
        if (screenshotPath != null) {
            try {
                test.get().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                test.get().info("Could not attach screenshot: " + e.getMessage());
            }
        }
        
        test.get().info("Test failed at: " + getCurrentTimestamp());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test skipped: " + 
                      (result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason provided"));
        test.get().info("Test skipped at: " + getCurrentTimestamp());
    }
    
    private void initializeExtentReports() {
        String reportPath = ConfigManager.getProperty("extent.report.path", "test-output/extent-reports/");
        String reportName = ConfigManager.getProperty("extent.report.name", "Test Execution Report");
        
        // Create reports directory if it doesn't exist
        File reportsDir = new File(reportPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        // Generate report file name with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportFileName = reportPath + "ExtentReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);
        sparkReporter.config().setDocumentTitle(reportName);
        sparkReporter.config().setReportName("Selenium BrowserStack Automation Report");
        sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        extent.setSystemInfo("Framework", ConfigManager.getProperty("framework.name", "Selenium BrowserStack Framework"));
        extent.setSystemInfo("Version", ConfigManager.getProperty("framework.version", "1.0.0"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Environment", System.getProperty("environment", "BrowserStack"));
        extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        extent.setSystemInfo("Execution Date", getCurrentTimestamp());
        
        System.out.println("ExtentReports initialized. Report will be generated at: " + reportFileName);
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}
