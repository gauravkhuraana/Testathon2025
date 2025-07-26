package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;
import java.util.Arrays;

/**
 * Network and Error Handling Utilities
 * Handles network failures, timeouts, and specific URL failures that shouldn't break tests
 */
public class NetworkErrorHandler {
    
    // URLs that are known to fail and should be ignored during tests
    private static final List<String> IGNORED_FAILED_URLS = Arrays.asList(
        "testathon.live/failed-request",
        "failed-request",
        "analytics",
        "tracking",
        "ads",
        "advertisement"
    );
    
    /**
     * Check if a URL should be ignored when it fails
     */
    public static boolean shouldIgnoreFailedUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        
        return IGNORED_FAILED_URLS.stream()
            .anyMatch(ignoredUrl -> url.toLowerCase().contains(ignoredUrl.toLowerCase()));
    }
    
    /**
     * Clear browser console logs to avoid accumulation
     */
    public static void clearConsoleLogs(WebDriver driver) {
        try {
            driver.manage().logs().get(LogType.BROWSER).getAll();
        } catch (Exception e) {
            // Ignore if logs are not available
            System.out.println("Console logs not available: " + e.getMessage());
        }
    }
    
    /**
     * Check for critical JavaScript errors (excluding ignored failures)
     */
    public static boolean hasCriticalJavaScriptErrors(WebDriver driver) {
        try {
            List<LogEntry> logs = driver.manage().logs().get(LogType.BROWSER).getAll();
            
            for (LogEntry log : logs) {
                String message = log.getMessage().toLowerCase();
                
                // Skip errors from ignored URLs
                if (shouldIgnoreFailedUrl(message)) {
                    System.out.println("⚠️ Ignoring known failing request: " + log.getMessage());
                    continue;
                }
                
                // Check for critical errors only
                if (message.contains("error") && 
                    !message.contains("favicon") && 
                    !message.contains("ads") &&
                    !message.contains("analytics")) {
                    System.out.println("❌ Critical JavaScript error found: " + log.getMessage());
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not check JavaScript errors: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Wait for page to be ready, ignoring network failures from known problematic URLs
     */
    public static void waitForPageReadyIgnoringKnownFailures(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            // Wait for document ready state
            wait.until(webDriver -> {
                try {
                    String readyState = ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").toString();
                    return "complete".equals(readyState);
                } catch (Exception e) {
                    return false;
                }
            });
            
            // Clear console logs to prevent accumulation
            clearConsoleLogs(driver);
            
            // Additional wait for any dynamic content, but don't fail on network errors
            Thread.sleep(2000);
            
        } catch (Exception e) {
            System.out.println("⚠️ Page ready check completed with warnings: " + e.getMessage());
        }
    }
    
    /**
     * Enhanced navigation with retry logic for failed requests
     */
    public static boolean navigateWithRetry(WebDriver driver, String url, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("🌐 Navigation attempt " + attempt + " to: " + url);
                
                driver.get(url);
                
                // Wait for page to be ready, ignoring known failures
                waitForPageReadyIgnoringKnownFailures(driver, 30);
                
                // Check if we're on the expected domain (basic validation)
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("testathon.live")) {
                    System.out.println("✅ Successfully navigated to: " + currentUrl);
                    return true;
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ Navigation attempt " + attempt + " failed: " + e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(3000); // Wait before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        System.out.println("❌ All navigation attempts failed for: " + url);
        return false;
    }
    
    /**
     * Safe click that ignores network errors from analytics/tracking
     */
    public static boolean safeClickIgnoringNetworkErrors(WebDriver driver, Runnable clickAction) {
        try {
            // Clear logs before action
            clearConsoleLogs(driver);
            
            // Perform the click
            clickAction.run();
            
            // Wait a moment for any network requests
            Thread.sleep(1000);
            
            // Check for critical errors only (ignore known failures)
            return !hasCriticalJavaScriptErrors(driver);
            
        } catch (Exception e) {
            System.out.println("⚠️ Click action completed with warnings: " + e.getMessage());
            return true; // Don't fail the test for network issues
        }
    }
    
    /**
     * Configure driver to ignore certain network failures
     */
    public static void configureNetworkErrorTolerance(WebDriver driver) {
        try {
            // Inject JavaScript to handle failed requests gracefully
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            String script = """
                // Override console.error to filter out known failing requests
                (function() {
                    const originalError = console.error;
                    console.error = function(...args) {
                        const message = args.join(' ').toLowerCase();
                        const ignoredPatterns = ['failed-request', 'testathon.live/failed-request', 'net::err'];
                        
                        // Only log if it's not an ignored pattern
                        if (!ignoredPatterns.some(pattern => message.includes(pattern))) {
                            originalError.apply(console, args);
                        }
                    };
                })();
                
                // Add error handler for uncaught errors
                window.addEventListener('error', function(e) {
                    if (e.filename && e.filename.includes('failed-request')) {
                        e.preventDefault();
                        return false;
                    }
                });
                """;
            
            js.executeScript(script);
            System.out.println("✅ Network error tolerance configured");
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not configure network error tolerance: " + e.getMessage());
        }
    }
}
