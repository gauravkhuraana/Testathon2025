package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

/**
 * Page Object for Offers functionality
 * Handles offers page interactions including location popup handling
 */
public class OffersPage extends BasePage {
    
    // Offers page elements
    @FindBy(xpath = "//h1[contains(text(), 'Offers') or contains(text(), 'Special Offers')]")
    private WebElement offersPageTitle;
    
    @FindBy(className = "offers-container")
    private WebElement offersContainer;
    
    @FindBy(xpath = "//div[contains(@class, 'offer-card')]")
    private WebElement offerCards;
    
    // Location popup elements - various patterns for cross-browser compatibility
    @FindBy(xpath = "//div[contains(text(), 'Enable Location') or contains(text(), 'Allow Location') or contains(text(), 'Share Location')]")
    private WebElement locationPopupTitle;
    
    @FindBy(xpath = "//button[contains(text(), 'Allow') or contains(text(), 'Enable') or contains(text(), 'Yes')]")
    private WebElement allowLocationButton;
    
    @FindBy(xpath = "//button[contains(text(), 'Block') or contains(text(), 'Deny') or contains(text(), 'No') or contains(text(), 'Cancel')]")
    private WebElement blockLocationButton;
    
    @FindBy(xpath = "//div[contains(@class, 'location-popup') or contains(@class, 'modal') or contains(@class, 'permission-dialog')]")
    private WebElement locationPopupContainer;
    
    // No offers/data message elements
    @FindBy(xpath = "//div[contains(text(), 'No offers') or contains(text(), 'No data') or contains(text(), 'Unable to load')]")
    private WebElement noOffersMessage;
    
    @FindBy(xpath = "//div[contains(text(), 'Location required') or contains(text(), 'Enable location')]")
    private WebElement locationRequiredMessage;
    
    // Locators using By selectors
    private final By offersPageTitleLocator = By.xpath("//h1[contains(text(), 'Offers') or contains(text(), 'Special Offers')] | //div[contains(@class, 'page-title') and contains(text(), 'Offers')]");
    private final By offersContainerLocator = By.xpath("//div[contains(@class, 'offers-container') or contains(@class, 'offers-list') or contains(@class, 'deals-container')]");
    private final By offerCardsLocator = By.xpath("//div[contains(@class, 'offer-card') or contains(@class, 'deal-card') or contains(@class, 'promo-card')]");
    
    // Location popup locators - comprehensive patterns for cross-device compatibility
    private final By locationPopupLocator = By.xpath("//div[contains(@class, 'location-popup') or contains(@class, 'permission-dialog') or contains(@class, 'geolocation-modal')] | //div[contains(text(), 'wants to') and contains(text(), 'location')] | //div[contains(text(), 'Enable Location') or contains(text(), 'Share Location')]");
    private final By allowLocationLocator = By.xpath("//button[contains(text(), 'Allow') or contains(text(), 'Enable') or contains(text(), 'Yes') or contains(text(), 'OK')] | //button[@class='permission-allow'] | //button[contains(@class, 'allow')]");
    private final By blockLocationLocator = By.xpath("//button[contains(text(), 'Block') or contains(text(), 'Deny') or contains(text(), 'No') or contains(text(), 'Cancel')] | //button[@class='permission-deny'] | //button[contains(@class, 'deny')]");
    
    // No data/offers locators
    private final By noOffersLocator = By.xpath("//div[contains(text(), 'No offers') or contains(text(), 'No data') or contains(text(), 'Unable to load') or contains(text(), 'No deals available')] | //div[contains(@class, 'empty-state')] | //div[contains(@class, 'no-content')]");
    private final By locationRequiredLocator = By.xpath("//div[contains(text(), 'Location required') or contains(text(), 'Enable location') or contains(text(), 'Location access needed')]");
    
    public OffersPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        return SeleniumUtils.isElementVisible(driver, offersPageTitleLocator) ||
               SeleniumUtils.isElementVisible(driver, offersContainerLocator) ||
               driver.getCurrentUrl().toLowerCase().contains("offers");
    }
    
    /**
     * Check if offers page is displayed
     */
    public boolean isOffersPageDisplayed() {
        return isPageLoaded();
    }
    
    /**
     * Check if location popup is displayed
     * This method checks for various popup patterns across different browsers
     */
    public boolean isLocationPopupDisplayed() {
        // Check multiple possible popup indicators
        return SeleniumUtils.isElementVisible(driver, locationPopupLocator) ||
               SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(text(), 'wants to') and contains(text(), 'location')]")) ||
               SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(@class, 'permission-dialog')]")) ||
               // Browser native popup detection (Chrome, Firefox, Safari patterns)
               SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(@class, 'notification') and contains(text(), 'location')]"));
    }
    
    /**
     * Handle location popup by clicking allow/enable
     * Critical for cross-browser and cross-device testing
     */
    public void allowLocationAccess() {
        if (isLocationPopupDisplayed()) {
            System.out.println("📍 Location popup detected - clicking Allow/Enable");
            
            // Try multiple allow button patterns
            if (SeleniumUtils.isElementVisible(driver, allowLocationLocator)) {
                SeleniumUtils.safeClick(driver, allowLocationLocator);
                System.out.println("✅ Clicked Allow location button");
            } else {
                // Fallback: try common allow button texts
                String[] allowTexts = {"Allow", "Enable", "Yes", "OK", "Continue"};
                for (String text : allowTexts) {
                    By buttonLocator = By.xpath("//button[contains(text(), '" + text + "')]");
                    if (SeleniumUtils.isElementVisible(driver, buttonLocator)) {
                        SeleniumUtils.safeClick(driver, buttonLocator);
                        System.out.println("✅ Clicked '" + text + "' location button");
                        break;
                    }
                }
            }
            
            // Wait for popup to disappear
            SeleniumUtils.waitForElementToDisappear(driver, locationPopupLocator, 10);
        } else {
            System.out.println("ℹ️ No location popup detected");
        }
    }
    
    /**
     * Handle location popup by clicking deny/block
     * Tests scenario where user denies location access
     */
    public void denyLocationAccess() {
        if (isLocationPopupDisplayed()) {
            System.out.println("📍 Location popup detected - clicking Deny/Block");
            
            // Try multiple deny button patterns
            if (SeleniumUtils.isElementVisible(driver, blockLocationLocator)) {
                SeleniumUtils.safeClick(driver, blockLocationLocator);
                System.out.println("✅ Clicked Deny location button");
            } else {
                // Fallback: try common deny button texts
                String[] denyTexts = {"Block", "Deny", "No", "Cancel", "Not now"};
                for (String text : denyTexts) {
                    By buttonLocator = By.xpath("//button[contains(text(), '" + text + "')]");
                    if (SeleniumUtils.isElementVisible(driver, buttonLocator)) {
                        SeleniumUtils.safeClick(driver, buttonLocator);
                        System.out.println("✅ Clicked '" + text + "' location button");
                        break;
                    }
                }
            }
            
            // Wait for popup to disappear
            SeleniumUtils.waitForElementToDisappear(driver, locationPopupLocator, 10);
        } else {
            System.out.println("ℹ️ No location popup detected");
        }
    }
    
    /**
     * Check if offers are loaded and displayed
     * Returns true if offers are present, false if no data
     */
    public boolean areOffersDisplayed() {
        return SeleniumUtils.isElementVisible(driver, offerCardsLocator) ||
               SeleniumUtils.isElementVisible(driver, offersContainerLocator);
    }
    
    /**
     * Check if "no offers" or "no data" message is displayed
     * Critical for validating location-dependent content
     */
    public boolean isNoOffersMessageDisplayed() {
        return SeleniumUtils.isElementVisible(driver, noOffersLocator);
    }
    
    /**
     * Check if location required message is displayed
     */
    public boolean isLocationRequiredMessageDisplayed() {
        return SeleniumUtils.isElementVisible(driver, locationRequiredLocator);
    }
    
    /**
     * Get the no offers message text
     */
    public String getNoOffersMessage() {
        if (isNoOffersMessageDisplayed()) {
            return SeleniumUtils.getTextSafely(driver, noOffersLocator);
        }
        return null;
    }
    
    /**
     * Get the location required message text
     */
    public String getLocationRequiredMessage() {
        if (isLocationRequiredMessageDisplayed()) {
            return SeleniumUtils.getTextSafely(driver, locationRequiredLocator);
        }
        return null;
    }
    
    /**
     * Get offers page title
     */
    public String getPageTitle() {
        if (SeleniumUtils.isElementVisible(driver, offersPageTitleLocator)) {
            return SeleniumUtils.getTextSafely(driver, offersPageTitleLocator);
        }
        return super.getPageTitle();
    }
    
    /**
     * Get count of offer cards displayed
     */
    public int getOffersCount() {
        if (areOffersDisplayed()) {
            return driver.findElements(offerCardsLocator).size();
        }
        return 0;
    }
    
    /**
     * Refresh page to retry loading offers
     * Useful when location permission changes
     */
    public void refreshOffersPage() {
        System.out.println("🔄 Refreshing offers page...");
        refreshPage();
        
        // Wait for page to reload
        waitForPageReady();
    }
    
    /**
     * Wait for offers to load with timeout
     * Returns true if offers loaded, false if timeout or no data
     */
    public boolean waitForOffersToLoad(int timeoutSeconds) {
        try {
            // First wait for page to be ready
            SeleniumUtils.waitForElementVisible(driver, offersPageTitleLocator, timeoutSeconds);
            
            // Then wait for either offers to appear or no-data message
            long startTime = System.currentTimeMillis();
            long timeout = timeoutSeconds * 1000L;
            
            while (System.currentTimeMillis() - startTime < timeout) {
                if (areOffersDisplayed() || isNoOffersMessageDisplayed() || isLocationRequiredMessageDisplayed()) {
                    return true;
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Error waiting for offers to load: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Comprehensive validation of offers page state
     * Returns detailed status of the page
     */
    public String validateOffersPageState() {
        StringBuilder status = new StringBuilder();
        
        status.append("📊 Offers Page State Analysis:\n");
        status.append("- Page loaded: ").append(isPageLoaded()).append("\n");
        status.append("- Offers displayed: ").append(areOffersDisplayed()).append("\n");
        status.append("- Offers count: ").append(getOffersCount()).append("\n");
        status.append("- No offers message: ").append(isNoOffersMessageDisplayed()).append("\n");
        status.append("- Location required message: ").append(isLocationRequiredMessageDisplayed()).append("\n");
        status.append("- Location popup visible: ").append(isLocationPopupDisplayed()).append("\n");
        
        if (isNoOffersMessageDisplayed()) {
            status.append("- No offers message text: ").append(getNoOffersMessage()).append("\n");
        }
        
        if (isLocationRequiredMessageDisplayed()) {
            status.append("- Location message text: ").append(getLocationRequiredMessage()).append("\n");
        }
        
        return status.toString();
    }
}
