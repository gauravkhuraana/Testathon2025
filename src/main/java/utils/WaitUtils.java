package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Enhanced wait utilities for better test stability
 */
public class WaitUtils {
    
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int POLLING_INTERVAL = 500;
    
    /**
     * Wait for element to be displayed and clickable with retry mechanism
     */
    public static boolean waitForElementToBeReady(WebDriver driver, By locator, int timeoutSeconds) {
        int attempts = 0;
        int maxAttempts = 3;
        
        while (attempts < maxAttempts) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                
                // Wait for element to be present first
                wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                
                // Then wait for it to be visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                
                // Finally wait for it to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(locator));
                
                return true;
                
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("Stale element detected, retrying... Attempt: " + attempts);
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } catch (TimeoutException e) {
                if (attempts == maxAttempts - 1) {
                    System.out.println("Element not ready after " + maxAttempts + " attempts: " + locator);
                    return false;
                }
                attempts++;
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Wait for element to be ready with default timeout
     */
    public static boolean waitForElementToBeReady(WebDriver driver, By locator) {
        return waitForElementToBeReady(driver, locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for page to be fully loaded and ready for interaction
     */
    public static void waitForPageToBeReady(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        // Wait for page to load
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        
        // Wait for jQuery to be ready (if present)
        try {
            wait.until(webDriver -> {
                Object result = ((JavascriptExecutor) webDriver)
                        .executeScript("return typeof jQuery !== 'undefined' ? jQuery.active === 0 : true");
                return result instanceof Boolean && (Boolean) result;
            });
        } catch (Exception e) {
            // jQuery not present, continue
        }
        
        // Additional wait for any remaining scripts
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Wait for page to be ready with default timeout
     */
    public static void waitForPageToBeReady(WebDriver driver) {
        waitForPageToBeReady(driver, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for any of multiple elements to appear and be clickable
     */
    public static WebElement waitForAnyElementReady(WebDriver driver, int timeoutSeconds, By... locators) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        for (By locator : locators) {
            try {
                if (waitForElementToBeReady(driver, locator, 2)) {
                    return driver.findElement(locator);
                }
            } catch (Exception e) {
                // Continue to next locator
            }
        }
        
        // If none worked with individual waits, try the traditional approach
        for (By locator : locators) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (TimeoutException e) {
                // Continue to next locator
            }
        }
        
        return null;
    }
    
    /**
     * Wait for text to appear in element with enhanced stability
     */
    public static boolean waitForTextInElement(WebDriver driver, By locator, String expectedText, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            return wait.until(webDriver -> {
                try {
                    WebElement element = webDriver.findElement(locator);
                    String actualText = element.getText().trim();
                    return actualText.contains(expectedText);
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Wait for element attribute to have specific value
     */
    public static boolean waitForElementAttribute(WebDriver driver, By locator, String attribute, String expectedValue, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            return wait.until(webDriver -> {
                try {
                    WebElement element = webDriver.findElement(locator);
                    String actualValue = element.getAttribute(attribute);
                    return expectedValue.equals(actualValue);
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Wait for element to become invisible or disappear
     */
    public static boolean waitForElementToDisappear(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Enhanced element click with multiple retry strategies
     */
    public static boolean enhancedClick(WebDriver driver, By locator, int timeoutSeconds) {
        if (!waitForElementToBeReady(driver, locator, timeoutSeconds)) {
            return false;
        }
        
        int attempts = 0;
        int maxAttempts = 3;
        
        while (attempts < maxAttempts) {
            try {
                WebElement element = driver.findElement(locator);
                
                // Try regular click first
                element.click();
                return true;
                
            } catch (ElementClickInterceptedException e) {
                // Try JavaScript click
                try {
                    WebElement element = driver.findElement(locator);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return true;
                } catch (Exception jsException) {
                    attempts++;
                    System.out.println("Click intercepted, retrying... Attempt: " + attempts);
                }
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("Stale element during click, retrying... Attempt: " + attempts);
            } catch (Exception e) {
                System.out.println("Unexpected error during click: " + e.getMessage());
                return false;
            }
            
            if (attempts < maxAttempts) {
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Enhanced element click with default timeout
     */
    public static boolean enhancedClick(WebDriver driver, By locator) {
        return enhancedClick(driver, locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for a list of elements to be displayed
     */
    public static boolean waitForElementsToBeDisplayed(WebDriver driver, List<By> locators, int timeoutSeconds) {
        for (By locator : locators) {
            if (!waitForElementToBeReady(driver, locator, timeoutSeconds)) {
                return false;
            }
        }
        return true;
    }
}
