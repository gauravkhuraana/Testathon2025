package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

/**
 * Page Object for testathon.live Home Page (StackDemo)
 */
public class HomePage extends BasePage {
    
    // Locators for testathon.live site
    @FindBy(linkText = "Sign In")
    private WebElement signInButton;
    
    @FindBy(xpath = "//div[contains(@class, 'bag')]")
    private WebElement cartIcon;
    
    @FindBy(xpath = "//div[text()='25 Product(s) found.']")
    private WebElement productCountText;
    
    // Locators using By selectors for testathon.live  
    private final By signInButtonLocator = By.linkText("Sign In");
    private final By productGridLocator = By.xpath("//div[contains(text(), 'Product(s) found')]");
    private final By addToCartButtonLocator = By.xpath("//div[text()='Add to cart']");
    private final By cartBadgeLocator = By.xpath("//div[contains(@class, 'badge')]");
    private final By productItemsLocator = By.xpath("//div[contains(@class, 'product-item')]");
    private final By vendorFiltersLocator = By.xpath("//div[text()='Vendors:']");
    
    // Vendor filter locators
    private final By appleFilterLocator = By.xpath("//div[text()='Apple']/preceding-sibling::input[@type='checkbox']");
    private final By samsungFilterLocator = By.xpath("//div[text()='Samsung']/preceding-sibling::input[@type='checkbox']");
    private final By googleFilterLocator = By.xpath("//div[text()='Google']/preceding-sibling::input[@type='checkbox']");
    private final By onePlusFilterLocator = By.xpath("//div[text()='OnePlus']/preceding-sibling::input[@type='checkbox']");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        // Wait for page to load and verify critical elements are displayed
        SeleniumUtils.waitForPageLoad(driver);
        return verifyElementDisplayed(vendorFiltersLocator) || 
               verifyElementDisplayed(productGridLocator);
    }
    
    /**
     * Check if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        return waitForAnyElementDisplayed(vendorFiltersLocator, productGridLocator) != null;
    }
    
    /**
     * Click Sign In button
     */
    public void clickSignIn() {
        safeClickWithWait(signInButtonLocator);
    }
    
    /**
     * Get number of products displayed
     */
    public int getProductCount() {
        SeleniumUtils.waitForElementVisible(driver, productGridLocator);
        String productText = SeleniumUtils.getTextSafely(driver, productGridLocator);
        // Extract number from text like "25 Product(s) found."
        String[] parts = productText.split(" ");
        if (parts.length > 0) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                System.out.println("Could not parse product count from: " + productText);
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * Click on a product by name
     */
    public void clickProductByName(String productName) {
        By productLocator = By.xpath(String.format("//p[contains(text(), '%s')]", productName));
        safeClickWithWait(productLocator);
    }
    
    /**
     * Add first product to cart
     */
    public void addFirstProductToCart() {
        waitForElementDisplayed(addToCartButtonLocator);
        safeClickWithWait(addToCartButtonLocator);
    }
    
    /**
     * Add product to cart by SKU
     */
    public void addProductToCartBySku(String sku) {
        By productLocator = By.cssSelector(String.format("[data-sku='%s'] .shelf-item__buy-btn", sku));
        safeClickWithWait(productLocator);
    }
    
    /**
     * Get cart quantity
     */
    public int getCartQuantity() {
        if (SeleniumUtils.isElementVisible(driver, By.className("bag__quantity"))) {
            String quantity = SeleniumUtils.getTextSafely(driver, By.className("bag__quantity"));
            return Integer.parseInt(quantity);
        }
        return 0;
    }
    
    /**
     * Click cart icon
     */
    public void clickCart() {
        safeClickWithWait(By.className("bag"));
    }
    
    /**
     * Check if product exists by name
     */
    public boolean isProductDisplayed(String productName) {
        By productLocator = By.xpath(String.format("//p[@class='shelf-item__title' and contains(text(), '%s')]", productName));
        return verifyElementDisplayed(productLocator);
    }
    
    /**
     * Get product price by name
     */
    public String getProductPrice(String productName) {
        By priceLocator = By.xpath(String.format("//p[@class='shelf-item__title' and contains(text(), '%s')]/following-sibling::div[@class='val']//b", productName));
        waitForElementDisplayed(priceLocator);
        return SeleniumUtils.getTextSafely(driver, priceLocator);
    }
    
    /**
     * Apply filter (if available)
     */
    public void applyFilter(String filterType) {
        By filterLocator = By.xpath(String.format("//span[contains(text(), '%s')]", filterType));
        if (verifyElementDisplayed(filterLocator)) {
            safeClickWithWait(filterLocator);
        }
    }
    
    /**
     * Search for products (if search functionality exists)
     */
    public void searchProducts(String searchTerm) {
        // Implementation depends on if search functionality is available
        // For now, this is a placeholder method
        System.out.println("Searching for: " + searchTerm);
    }
    
    /**
     * Get page title
     */
    public String getHomePageTitle() {
        return getPageTitle();
    }
    
    /**
     * Navigate to Offers page
     */
    public void navigateToOffers() {
        By offersLinkLocator = By.linkText("Offers");
        if (SeleniumUtils.isElementVisible(driver, offersLinkLocator)) {
            SeleniumUtils.safeClick(driver, offersLinkLocator);
        } else {
            System.out.println("Offers link not visible - user may not be logged in");
        }
    }
    
    /**
     * Navigate to Orders page
     */
    public void navigateToOrders() {
        By ordersLinkLocator = By.linkText("Orders");
        if (SeleniumUtils.isElementVisible(driver, ordersLinkLocator)) {
            SeleniumUtils.safeClick(driver, ordersLinkLocator);
        } else {
            System.out.println("Orders link not visible - user may not be logged in");
        }
    }
    
    /**
     * Navigate to Favourites page
     */
    public void navigateToFavourites() {
        By favouritesLinkLocator = By.linkText("Favourites");
        if (SeleniumUtils.isElementVisible(driver, favouritesLinkLocator)) {
            SeleniumUtils.safeClick(driver, favouritesLinkLocator);
        } else {
            System.out.println("Favourites link not visible - user may not be logged in");
        }
    }
    
    /**
     * Check if navigation links are visible after login
     */
    public boolean areNavigationLinksVisible() {
        return SeleniumUtils.isElementVisible(driver, By.linkText("Offers")) &&
               SeleniumUtils.isElementVisible(driver, By.linkText("Orders")) &&
               SeleniumUtils.isElementVisible(driver, By.linkText("Favourites"));
    }
}
