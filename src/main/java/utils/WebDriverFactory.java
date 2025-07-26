package utils;

import config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * WebDriver factory for creating browser instances for both local and BrowserStack execution
 */
public class WebDriverFactory {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    public static WebDriver createDriver(String browser, String os, String osVersion) {
        return createDriver(browser, os, osVersion, null, null);
    }
    
    public static WebDriver createDriver(String browser, String os, String osVersion, String device, String browserVersion) {
        WebDriver driver;
        String environment = System.getProperty("environment", "local");
        
        // Add debug logging
        System.out.println("Creating WebDriver - Environment: " + environment + 
                          ", Browser: '" + browser + "', OS: " + os + ", OSVersion: " + osVersion + 
                          (device != null ? ", Device: " + device : "") +
                          (browserVersion != null ? ", BrowserVersion: " + browserVersion : ""));
        
        if ("browserstack".equalsIgnoreCase(environment)) {
            driver = createBrowserStackDriver(browser, os, osVersion, device, browserVersion);
        } else {
            driver = createLocalDriver(browser);
        }
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getIntProperty("page.load.timeout", 60)));
        
        driverThreadLocal.set(driver);
        return driver;
    }
    
    private static WebDriver createBrowserStackDriver(String browser, String os, String osVersion, String device, String browserVersion) {
        DesiredCapabilities caps = new DesiredCapabilities();
        
        // Get credentials with debugging
        String username = ConfigManager.getBrowserStackUsername();
        String accessKey = ConfigManager.getBrowserStackAccessKey();
        
        System.out.println("========== BrowserStack Configuration Debug ==========");
        System.out.println("Username: " + (username != null ? username.substring(0, Math.min(5, username.length())) + "..." : "null"));
        System.out.println("Access Key: " + (accessKey != null ? accessKey.substring(0, Math.min(5, accessKey.length())) + "..." : "null"));
        System.out.println("Hub URL: " + ConfigManager.getBrowserStackHubUrl());
        System.out.println("Platform: " + browser + " on " + os + " " + osVersion);
        if (device != null && !device.isEmpty()) {
            System.out.println("Device: " + device);
        }
        System.out.println("====================================================");
        
        // Browser and OS capabilities - BrowserStack expects proper case
        caps.setCapability("browserName", browser);
        
        // Set browser version - use parameter if provided, otherwise default to "latest"
        String version = (browserVersion != null && !browserVersion.isEmpty()) ? browserVersion : "latest";
        caps.setCapability("browserVersion", version);
        
        // BrowserStack specific capabilities using W3C format
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", username);
        bstackOptions.put("accessKey", accessKey);
        bstackOptions.put("projectName", "Java Selenium BrowserStack Framework");
        bstackOptions.put("buildName", "Testathon 2025 - " + browser + " " + os);
        bstackOptions.put("sessionName", "Test Session - " + browser);
        
        // Platform information - handle both desktop and mobile
        if (device != null && !device.isEmpty()) {
            // Mobile device configuration
            bstackOptions.put("deviceName", device);
            bstackOptions.put("osVersion", osVersion);
            if ("android".equalsIgnoreCase(os)) {
                bstackOptions.put("platformName", "Android");
            } else if ("ios".equalsIgnoreCase(os)) {
                bstackOptions.put("platformName", "iOS");
            }
        } else {
            // Desktop configuration
            bstackOptions.put("os", os);
            bstackOptions.put("osVersion", osVersion);
        }
        
        // Observability and debugging features - these are essential for SDK features
        bstackOptions.put("debug", true);
        bstackOptions.put("video", true);
        bstackOptions.put("networkLogs", true);
        bstackOptions.put("consoleLogs", "verbose");
        bstackOptions.put("seleniumLogs", true);
        
        // Additional options
        bstackOptions.put("local", false);
        bstackOptions.put("timezone", "UTC");
        bstackOptions.put("geoLocation", "US");
        
        caps.setCapability("bstack:options", bstackOptions);
        
        try {
            return new RemoteWebDriver(new URL(ConfigManager.getBrowserStackHubUrl()), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack URL: " + e.getMessage());
        }
    }
    
    private static WebDriver createLocalDriver(String browser) {
        WebDriver driver;
        boolean headless = ConfigManager.getBooleanProperty("local.headless", false);
        
        // Handle null or empty browser parameter
        if (browser == null || browser.trim().isEmpty()) {
            throw new IllegalArgumentException("Browser parameter cannot be null or empty. Supported browsers: chrome, firefox, edge, safari");
        }
        
        switch (browser.toLowerCase().trim()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-dev-shm-usage");
                edgeOptions.addArguments("--window-size=1920,1080");
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                driver = new SafariDriver(safariOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: '" + browser + "'. Supported browsers: chrome, firefox, edge, safari");
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
