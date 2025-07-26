package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

import java.util.List;

/**
 * Page Object for Shopping Cart functionality
 */
public class CartPage extends BasePage {
    
    // Locators using @FindBy annotations
    @FindBy(className = "shelf-container")
    private WebElement cartContainer;
    
    @FindBy(className = "shelf-item")
    private List<WebElement> cartItems;
    
    @FindBy(className = "sub-price__val")
    private WebElement subtotalAmount;
    
    @FindBy(className = "checkout-cta")
    private WebElement checkoutButton;
    
    @FindBy(className = "shelf-item__del")
    private List<WebElement> removeButtons;
    
    @FindBy(className = "shelf-empty")
    private WebElement emptyCartMessage;
    
    // Locators
    private final By cartContainerLocator = By.className("shelf-container");
    private final By checkoutButtonLocator = By.className("checkout-cta");
    private final By subtotalLocator = By.className("sub-price__val");
    private final By emptyCartLocator = By.className("shelf-empty");
    
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        // Wait for cart container or empty cart message to be displayed
        return waitForAnyElementDisplayed(cartContainerLocator, emptyCartLocator) != null;
    }
    
    /**
     * Get number of items in cart
     */
    public int getCartItemsCount() {
        if (isCartEmpty()) {
            return 0;
        }
        waitForElementDisplayed(cartContainerLocator);
        return cartItems.size();
    }
    
    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return verifyElementDisplayed(emptyCartLocator);
    }
    
    /**
     * Get cart empty message
     */
    public String getEmptyCartMessage() {
        if (isCartEmpty()) {
            return SeleniumUtils.getTextSafely(driver, emptyCartLocator);
        }
        return null;
    }
    
    /**
     * Get subtotal amount
     */
    public String getSubtotal() {
        if (!isCartEmpty()) {
            waitForElementDisplayed(subtotalLocator);
            return SeleniumUtils.getTextSafely(driver, subtotalLocator);
        }
        return "0";
    }
    
    /**
     * Click checkout button
     */
    public void clickCheckout() {
        if (!isCartEmpty()) {
            safeClickWithWait(checkoutButtonLocator);
        } else {
            throw new RuntimeException("Cannot checkout with empty cart");
        }
    }
    
    /**
     * Remove first item from cart
     */
    public void removeFirstItem() {
        if (!isCartEmpty() && !removeButtons.isEmpty()) {
            // Wait for remove button to be clickable
            SeleniumUtils.waitForElementClickable(driver, By.className("shelf-item__del"));
            removeButtons.get(0).click();
        }
    }
    
    /**
     * Remove all items from cart
     */
    public void removeAllItems() {
        while (!isCartEmpty() && getCartItemsCount() > 0) {
            removeFirstItem();
            // Wait a moment for item to be removed
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Get product name by index
     */
    public String getProductNameByIndex(int index) {
        if (!isCartEmpty() && index < cartItems.size()) {
            By productNameLocator = By.xpath(String.format("(//div[@class='shelf-item'])[%d]//p[@class='title']", index + 1));
            return SeleniumUtils.getTextSafely(driver, productNameLocator);
        }
        return null;
    }
    
    /**
     * Get product price by index
     */
    public String getProductPriceByIndex(int index) {
        if (!isCartEmpty() && index < cartItems.size()) {
            By productPriceLocator = By.xpath(String.format("(//div[@class='shelf-item'])[%d]//p[@class='price']", index + 1));
            return SeleniumUtils.getTextSafely(driver, productPriceLocator);
        }
        return null;
    }
    
    /**
     * Check if specific product exists in cart
     */
    public boolean isProductInCart(String productName) {
        if (isCartEmpty()) {
            return false;
        }
        
        By productLocator = By.xpath(String.format("//p[@class='title' and contains(text(), '%s')]", productName));
        return SeleniumUtils.isElementVisible(driver, productLocator);
    }
    
    /**
     * Get checkout button text
     */
    public String getCheckoutButtonText() {
        if (!isCartEmpty()) {
            return SeleniumUtils.getTextSafely(driver, checkoutButtonLocator);
        }
        return null;
    }
    
    /**
     * Verify cart page is displayed
     */
    public boolean isCartPageDisplayed() {
        return isPageLoaded();
    }
    
    /**
     * Continue shopping (navigate back)
     */
    public void continueShopping() {
        navigateBack();
    }
}
