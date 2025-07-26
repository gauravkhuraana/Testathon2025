package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

import java.util.List;

/**
 * Page Object for Favorites functionality
 * Handles favorites page interactions and validation
 */
public class FavoritesPage extends BasePage {
    
    // Favorites page elements
    @FindBy(xpath = "//h1[contains(text(), 'Favourites') or contains(text(), 'Favorites') or contains(text(), 'Wishlist')]")
    private WebElement favoritesPageTitle;
    
    @FindBy(xpath = "//div[contains(@class, 'favorites-container') or contains(@class, 'wishlist-container')]")
    private WebElement favoritesContainer;
    
    @FindBy(xpath = "//div[contains(@class, 'favorite-item') or contains(@class, 'wishlist-item')]")
    private List<WebElement> favoriteItems;
    
    @FindBy(xpath = "//div[contains(text(), 'No favorites') or contains(text(), 'No items') or contains(text(), 'Empty')]")
    private WebElement noFavoritesMessage;
    
    // Locators using By selectors
    private final By favoritesPageTitleLocator = By.xpath("//h1[contains(text(), 'Favourites') or contains(text(), 'Favorites') or contains(text(), 'Wishlist')] | //div[contains(@class, 'page-title') and (contains(text(), 'Favourites') or contains(text(), 'Favorites'))]");
    private final By favoritesContainerLocator = By.xpath("//div[contains(@class, 'favorites-container') or contains(@class, 'wishlist-container') or contains(@class, 'favourite-container')]");
    private final By favoriteItemsLocator = By.xpath("//div[contains(@class, 'favorite-item') or contains(@class, 'wishlist-item') or contains(@class, 'favourite-item')] | //div[contains(@class, 'product-card')] | //div[contains(@class, 'shelf-item')]");
    private final By noFavoritesLocator = By.xpath("//div[contains(text(), 'No favorites') or contains(text(), 'No favourites') or contains(text(), 'No items') or contains(text(), 'Empty') or contains(text(), 'wishlist is empty')] | //div[contains(@class, 'empty-state')] | //div[contains(@class, 'no-content')]");
    private final By addToFavoriteButtonLocator = By.xpath("//button[contains(@class, 'favorite') or contains(@class, 'wishlist') or contains(@title, 'favorite')] | //div[contains(@class, 'heart')] | //i[contains(@class, 'heart')]");
    private final By removeFromFavoriteButtonLocator = By.xpath("//button[contains(@class, 'favorite') and contains(@class, 'active')] | //button[contains(text(), 'Remove')] | //i[contains(@class, 'heart') and contains(@class, 'filled')]");
    
    public FavoritesPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        return SeleniumUtils.isElementVisible(driver, favoritesPageTitleLocator) ||
               SeleniumUtils.isElementVisible(driver, favoritesContainerLocator) ||
               driver.getCurrentUrl().toLowerCase().contains("favourites") ||
               driver.getCurrentUrl().toLowerCase().contains("favorites") ||
               driver.getCurrentUrl().toLowerCase().contains("wishlist");
    }
    
    /**
     * Check if favorites page is displayed
     */
    public boolean isFavoritesPageDisplayed() {
        return isPageLoaded();
    }
    
    /**
     * Check if favorites are displayed
     * Returns true if favorite items are present, false if no data
     */
    public boolean areFavoritesDisplayed() {
        return SeleniumUtils.isElementVisible(driver, favoriteItemsLocator);
    }
    
    /**
     * Check if "no favorites" message is displayed
     */
    public boolean isNoFavoritesMessageDisplayed() {
        return SeleniumUtils.isElementVisible(driver, noFavoritesLocator);
    }
    
    /**
     * Get count of favorite items displayed
     */
    public int getFavoritesCount() {
        if (areFavoritesDisplayed()) {
            return driver.findElements(favoriteItemsLocator).size();
        }
        return 0;
    }
    
    /**
     * Get favorites page title
     */
    public String getPageTitle() {
        if (SeleniumUtils.isElementVisible(driver, favoritesPageTitleLocator)) {
            return SeleniumUtils.getTextSafely(driver, favoritesPageTitleLocator);
        }
        return super.getPageTitle();
    }
    
    /**
     * Get the no favorites message text
     */
    public String getNoFavoritesMessage() {
        if (isNoFavoritesMessageDisplayed()) {
            return SeleniumUtils.getTextSafely(driver, noFavoritesLocator);
        }
        return null;
    }
    
    /**
     * Add first available product to favorites (for testing)
     */
    public void addFirstProductToFavorites() {
        if (SeleniumUtils.isElementVisible(driver, addToFavoriteButtonLocator)) {
            SeleniumUtils.safeClick(driver, addToFavoriteButtonLocator);
            System.out.println("✅ Added first product to favorites");
        } else {
            System.out.println("⚠️ No 'Add to Favorites' button found");
        }
    }
    
    /**
     * Remove first favorite item
     */
    public void removeFirstFavoriteItem() {
        if (SeleniumUtils.isElementVisible(driver, removeFromFavoriteButtonLocator)) {
            SeleniumUtils.safeClick(driver, removeFromFavoriteButtonLocator);
            System.out.println("✅ Removed first favorite item");
        } else {
            System.out.println("⚠️ No 'Remove from Favorites' button found");
        }
    }
    
    /**
     * Wait for favorites to load with timeout
     * Returns true if favorites loaded, false if timeout or no data
     */
    public boolean waitForFavoritesToLoad(int timeoutSeconds) {
        try {
            // First wait for page to be ready
            SeleniumUtils.waitForElementVisible(driver, favoritesPageTitleLocator, timeoutSeconds);
            
            // Then wait for either favorites to appear or no-data message
            long startTime = System.currentTimeMillis();
            long timeout = timeoutSeconds * 1000L;
            
            while (System.currentTimeMillis() - startTime < timeout) {
                if (areFavoritesDisplayed() || isNoFavoritesMessageDisplayed()) {
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
            System.out.println("⚠️ Error waiting for favorites to load: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Comprehensive validation of favorites page state
     * Returns detailed status of the page
     */
    public String validateFavoritesPageState() {
        StringBuilder status = new StringBuilder();
        
        status.append("⭐ Favorites Page State Analysis:\n");
        status.append("- Page loaded: ").append(isPageLoaded()).append("\n");
        status.append("- Favorites displayed: ").append(areFavoritesDisplayed()).append("\n");
        status.append("- Favorites count: ").append(getFavoritesCount()).append("\n");
        status.append("- No favorites message: ").append(isNoFavoritesMessageDisplayed()).append("\n");
        status.append("- Current URL: ").append(getCurrentUrl()).append("\n");
        
        if (isNoFavoritesMessageDisplayed()) {
            status.append("- No favorites message text: ").append(getNoFavoritesMessage()).append("\n");
        }
        
        return status.toString();
    }
    
    /**
     * Check if user has access to favorites functionality
     * This validates that fav_user can access the favorites feature
     */
    public boolean isFavoritesFunctionalityAccessible() {
        // Check if page loads and either shows favorites or shows "no favorites" message
        // Both scenarios indicate that the user has access to the functionality
        return isPageLoaded() && (areFavoritesDisplayed() || isNoFavoritesMessageDisplayed());
    }
    
    /**
     * Validate that fav_user specific functionality works
     * This is a comprehensive test for fav_user capabilities
     */
    public boolean validateFavUserFunctionality() {
        System.out.println("🔍 Validating fav_user specific functionality...");
        
        // Wait for page to load
        if (!waitForFavoritesToLoad(10)) {
            System.out.println("❌ Favorites page failed to load within timeout");
            return false;
        }
        
        // Check if user has access to favorites functionality
        if (!isFavoritesFunctionalityAccessible()) {
            System.out.println("❌ Favorites functionality not accessible");
            return false;
        }
        
        // Log current state
        System.out.println(validateFavoritesPageState());
        
        // Validate based on what's found
        if (areFavoritesDisplayed()) {
            System.out.println("✅ fav_user has existing favorites - functionality confirmed");
            return true;
        } else if (isNoFavoritesMessageDisplayed()) {
            System.out.println("✅ fav_user can access favorites page (currently empty) - functionality confirmed");
            return true;
        } else {
            System.out.println("⚠️ Unexpected state - no favorites or empty message found");
            return false;
        }
    }
}
