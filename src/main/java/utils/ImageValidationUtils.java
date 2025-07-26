package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Utility class for validating image loading status
 */
public class ImageValidationUtils {
    
    /**
     * Check if an image element is loaded completely
     * @param driver WebDriver instance
     * @param imageElement The image WebElement to check
     * @return true if image is loaded, false otherwise
     */
    public static boolean isImageLoaded(WebDriver driver, WebElement imageElement) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Check if image is complete and naturalHeight > 0
            Boolean isComplete = (Boolean) js.executeScript(
                "return arguments[0].complete && " +
                "typeof arguments[0].naturalHeight != 'undefined' && " +
                "arguments[0].naturalHeight > 0", imageElement);
                
            return isComplete != null && isComplete;
        } catch (Exception e) {
            System.out.println("Error checking image load status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if an image has a broken src or failed to load
     * @param driver WebDriver instance
     * @param imageElement The image WebElement to check
     * @return true if image is broken, false if loaded successfully
     */
    public static boolean isImageBroken(WebDriver driver, WebElement imageElement) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Check if image failed to load (naturalHeight = 0 or error state)
            Boolean isBroken = (Boolean) js.executeScript(
                "return arguments[0].complete && " +
                "(typeof arguments[0].naturalHeight == 'undefined' || " +
                "arguments[0].naturalHeight == 0)", imageElement);
                
            return isBroken != null && isBroken;
        } catch (Exception e) {
            System.out.println("Error checking if image is broken: " + e.getMessage());
            return true; // Assume broken if we can't check
        }
    }
    
    /**
     * Get all image elements on the current page
     * @param driver WebDriver instance
     * @return List of all img elements
     */
    public static List<WebElement> getAllImages(WebDriver driver) {
        return driver.findElements(By.tagName("img"));
    }
    
    /**
     * Get all images within a specific container
     * @param driver WebDriver instance
     * @param containerLocator By locator for the container element
     * @return List of img elements within the container
     */
    public static List<WebElement> getImagesInContainer(WebDriver driver, By containerLocator) {
        try {
            WebElement container = driver.findElement(containerLocator);
            return container.findElements(By.tagName("img"));
        } catch (Exception e) {
            System.out.println("Error finding images in container: " + e.getMessage());
            return driver.findElements(By.tagName("img")); // Fallback to all images
        }
    }
    
    /**
     * Count broken images on the page
     * @param driver WebDriver instance
     * @return Number of broken images found
     */
    public static int countBrokenImages(WebDriver driver) {
        List<WebElement> images = getAllImages(driver);
        int brokenCount = 0;
        
        for (WebElement image : images) {
            if (isImageBroken(driver, image)) {
                brokenCount++;
                String src = image.getAttribute("src");
                String alt = image.getAttribute("alt");
                System.out.println("Broken image found - src: " + src + ", alt: " + alt);
            }
        }
        
        return brokenCount;
    }
    
    /**
     * Validate that all images on the page are loaded
     * @param driver WebDriver instance
     * @param pageName Name of the page for reporting
     * @return true if all images are loaded, false if any are broken
     */
    public static boolean validateAllImagesLoaded(WebDriver driver, String pageName) {
        List<WebElement> images = getAllImages(driver);
        int totalImages = images.size();
        int brokenImages = 0;
        
        System.out.println("🖼️ Validating " + totalImages + " images on " + pageName);
        
        for (WebElement image : images) {
            if (isImageBroken(driver, image)) {
                brokenImages++;
                String src = image.getAttribute("src");
                String alt = image.getAttribute("alt");
                System.out.println("❌ Broken image found - src: " + src + ", alt: " + alt);
            }
        }
        
        if (brokenImages > 0) {
            System.out.println("❌ " + brokenImages + " out of " + totalImages + " images failed to load on " + pageName);
            return false;
        } else {
            System.out.println("✅ All " + totalImages + " images loaded successfully on " + pageName);
            return true;
        }
    }
    
    /**
     * Wait for images to load on the page
     * @param driver WebDriver instance
     * @param timeoutSeconds Maximum time to wait
     */
    public static void waitForImagesToLoad(WebDriver driver, int timeoutSeconds) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            long startTime = System.currentTimeMillis();
            long timeout = timeoutSeconds * 1000;
            
            while (System.currentTimeMillis() - startTime < timeout) {
                Boolean imagesLoaded = (Boolean) js.executeScript(
                    "var images = document.images;" +
                    "for (var i = 0; i < images.length; i++) {" +
                    "  if (!images[i].complete) return false;" +
                    "}" +
                    "return true;"
                );
                
                if (imagesLoaded != null && imagesLoaded) {
                    break;
                }
                
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println("Error waiting for images to load: " + e.getMessage());
        }
    }
    
    /**
     * Get detailed image information for debugging
     * @param driver WebDriver instance
     * @param imageElement The image element to analyze
     * @return String containing image details
     */
    public static String getImageDetails(WebDriver driver, WebElement imageElement) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            String src = imageElement.getAttribute("src");
            String alt = imageElement.getAttribute("alt");
            String title = imageElement.getAttribute("title");
            
            Object naturalWidth = js.executeScript("return arguments[0].naturalWidth", imageElement);
            Object naturalHeight = js.executeScript("return arguments[0].naturalHeight", imageElement);
            Boolean complete = (Boolean) js.executeScript("return arguments[0].complete", imageElement);
            
            return String.format("Image Details - src: %s, alt: %s, title: %s, naturalWidth: %s, naturalHeight: %s, complete: %s",
                src, alt, title, naturalWidth, naturalHeight, complete);
                
        } catch (Exception e) {
            return "Error getting image details: " + e.getMessage();
        }
    }
}
