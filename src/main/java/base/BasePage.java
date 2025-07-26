package base;

import config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SeleniumUtils;

import java.time.Duration;

/**
 * Base page class containing common functionality for all page objects
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Navigate to URL
     */
    public void navigateTo(String url) {
        driver.get(url);
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Refresh the page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Navigate back
     */
    public void navigateBack() {
        driver.navigate().back();
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Navigate forward
     */
    public void navigateForward() {
        driver.navigate().forward();
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Wait for page to load
     */
    protected void waitForPageLoad() {
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Take screenshot
     */
    public String takeScreenshot(String testName) {
        return SeleniumUtils.takeScreenshot(driver, testName);
    }
    
    /**
     * Abstract method to verify page is loaded
     * Each page should implement this to verify page-specific elements
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Wait for page to be ready
     */
    public void waitForPageReady() {
        waitForPageLoad();
        if (!isPageLoaded()) {
            throw new RuntimeException("Page did not load properly: " + getClass().getSimpleName());
        }
    }
}
