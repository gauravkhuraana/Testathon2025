package utils;

import config.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for common Selenium operations
 */
public class SeleniumUtils {
    
    private static final int DEFAULT_TIMEOUT = ConfigManager.getDefaultTimeout();
    
    /**
     * Wait for element to be visible and return it
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable and return it
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator) {
        return waitForElementPresent(driver, locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementPresent(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextInElement(WebDriver driver, By locator, String text) {
        return waitForTextInElement(driver, locator, text, DEFAULT_TIMEOUT);
    }
    
    public static boolean waitForTextInElement(WebDriver driver, By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Safe click method that waits for element to be clickable
     */
    public static void safeClick(WebDriver driver, By locator) {
        WebElement element = waitForElementClickable(driver, locator);
        element.click();
    }
    
    /**
     * Safe send keys method that waits for element to be visible
     */
    public static void safeSendKeys(WebDriver driver, By locator, String text) {
        WebElement element = waitForElementVisible(driver, locator);
        try {
            element.clear();
        } catch (Exception e) {
            System.out.println("Warning: Could not clear element, proceeding with sendKeys. Error: " + e.getMessage());
        }
        element.sendKeys(text);
    }
    
    /**
     * Get text from element with wait
     */
    public static String getTextSafely(WebDriver driver, By locator) {
        WebElement element = waitForElementVisible(driver, locator);
        return element.getText();
    }
    
    /**
     * Check if element is present without throwing exception
     */
    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Check if element is visible without throwing exception
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Scroll to element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Scroll to element by locator
     */
    public static void scrollToElement(WebDriver driver, By locator) {
        WebElement element = waitForElementPresent(driver, locator);
        scrollToElement(driver, element);
    }
    
    /**
     * JavaScript click
     */
    public static void javascriptClick(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
    
    /**
     * JavaScript click by locator
     */
    public static void javascriptClick(WebDriver driver, By locator) {
        WebElement element = waitForElementPresent(driver, locator);
        javascriptClick(driver, element);
    }
    
    /**
     * Take screenshot and return file path
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String screenshotPath = "test-output/screenshots/" + fileName;
            
            File destFile = new File(screenshotPath);
            destFile.getParentFile().mkdirs();
            
            FileUtils.copyFile(sourceFile, destFile);
            return destFile.getAbsolutePath();
            
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(WebDriver driver) {
        waitForPageLoad(driver, DEFAULT_TIMEOUT);
    }
    
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Switch to window by title
     */
    public static void switchToWindowByTitle(WebDriver driver, String title) {
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains(title)) {
                break;
            }
        }
    }
    
    /**
     * Close all windows except main window
     */
    public static void closeAllWindowsExceptMain(WebDriver driver) {
        String mainWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindow)) {
                driver.switchTo().window(windowHandle);
                driver.close();
            }
        }
        driver.switchTo().window(mainWindow);
    }
}
