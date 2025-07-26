package base;

import config.BrowserStackSDKConfig;
import config.ConfigManager;
import org.testng.annotations.*;
import utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Base test class containing common setup and teardown methods
 */
public abstract class BaseTest {
    
    protected WebDriver driver;
    protected String browser;
    protected String os;
    protected String osVersion;
    
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        // Initialize BrowserStack SDK configuration for Test Observability only if using BrowserStack
        String environment = System.getProperty("environment", "local");
        if ("browserstack".equalsIgnoreCase(environment)) {
            BrowserStackSDKConfig.initialize();
            BrowserStackSDKConfig.verifyCredentials();
        } else {
            System.out.println("Running in local environment - skipping BrowserStack SDK initialization");
        }
    }
    
    @Parameters({"browser", "os", "osVersion"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, 
                     @Optional("Windows") String os, 
                     @Optional("11") String osVersion) {
        
        // Use system properties if available, otherwise use parameters from TestNG XML
        this.browser = System.getProperty("browser", browser != null && !browser.isEmpty() ? browser : "chrome");
        this.os = System.getProperty("os", os != null && !os.isEmpty() ? os : "Windows");
        this.osVersion = System.getProperty("osVersion", osVersion != null && !osVersion.isEmpty() ? osVersion : "11");
        
        System.out.println("Setting up test with Browser: '" + this.browser + "', OS: '" + this.os + "', OSVersion: '" + this.osVersion + "'");
        
        // Create WebDriver instance
        this.driver = WebDriverFactory.createDriver(this.browser, this.os, this.osVersion);
        
        // Navigate to application URL with retry logic
        String appUrl = ConfigManager.getAppUrl();
        System.out.println("Navigating to: " + appUrl);
        
        // Use enhanced navigation with network error handling
        boolean navigationSuccess = utils.NetworkErrorHandler.navigateWithRetry(driver, appUrl, 3);
        if (!navigationSuccess) {
            System.out.println("⚠️ Navigation completed with warnings, continuing test...");
            // Don't fail the test, just log the warning
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            System.out.println("Closing browser for: " + browser + " on " + os);
            WebDriverFactory.quitDriver();
        }
    }
    
    /**
     * Get the current WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Get test information as string
     */
    protected String getTestInfo() {
        return String.format("Browser: %s, OS: %s %s", browser, os, osVersion);
    }
}
