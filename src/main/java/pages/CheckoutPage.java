package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

/**
 * Page Object for Checkout functionality
 */
public class CheckoutPage extends BasePage {
    
    // Personal Information Fields
    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;
    
    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;
    
    @FindBy(id = "addressLine1Input")
    private WebElement addressInput;
    
    @FindBy(id = "provinceInput")
    private WebElement stateInput;
    
    @FindBy(id = "postCodeInput")
    private WebElement postalCodeInput;
    
    @FindBy(id = "checkout-shipping-continue")
    private WebElement continueShippingButton;
    
    // Payment Information Fields
    @FindBy(id = "credit-card-number")
    private WebElement creditCardNumberInput;
    
    @FindBy(id = "credit-card-expiry-date")
    private WebElement expiryDateInput;
    
    @FindBy(id = "credit-card-cvv")
    private WebElement cvvInput;
    
    @FindBy(id = "checkout-payment-continue")
    private WebElement continuePaymentButton;
    
    // Confirmation
    @FindBy(id = "confirm-btn")
    private WebElement confirmOrderButton;
    
    @FindBy(className = "checkout-summary")
    private WebElement orderSummary;
    
    @FindBy(className = "success-message")
    private WebElement successMessage;
    
    // Locators
    private final By firstNameLocator = By.id("firstNameInput");
    private final By lastNameLocator = By.id("lastNameInput");
    private final By addressLocator = By.id("addressLine1Input");
    private final By stateLocator = By.id("provinceInput");
    private final By postalCodeLocator = By.id("postCodeInput");
    private final By continueShippingLocator = By.id("checkout-shipping-continue");
    
    private final By creditCardLocator = By.id("credit-card-number");
    private final By expiryDateLocator = By.id("credit-card-expiry-date");
    private final By cvvLocator = By.id("credit-card-cvv");
    private final By continuePaymentLocator = By.id("checkout-payment-continue");
    
    private final By confirmOrderLocator = By.id("confirm-btn");
    private final By successMessageLocator = By.className("success-message");
    
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        return SeleniumUtils.isElementVisible(driver, firstNameLocator) ||
               SeleniumUtils.isElementVisible(driver, creditCardLocator) ||
               SeleniumUtils.isElementVisible(driver, confirmOrderLocator);
    }
    
    /**
     * Fill shipping information
     */
    public void fillShippingInformation(String firstName, String lastName, String address, String state, String postalCode) {
        SeleniumUtils.safeSendKeys(driver, firstNameLocator, firstName);
        SeleniumUtils.safeSendKeys(driver, lastNameLocator, lastName);
        SeleniumUtils.safeSendKeys(driver, addressLocator, address);
        SeleniumUtils.safeSendKeys(driver, stateLocator, state);
        SeleniumUtils.safeSendKeys(driver, postalCodeLocator, postalCode);
    }
    
    /**
     * Continue to payment from shipping
     */
    public void continueToPayment() {
        SeleniumUtils.safeClick(driver, continueShippingLocator);
    }
    
    /**
     * Fill payment information
     */
    public void fillPaymentInformation(String cardNumber, String expiryDate, String cvv) {
        SeleniumUtils.safeSendKeys(driver, creditCardLocator, cardNumber);
        SeleniumUtils.safeSendKeys(driver, expiryDateLocator, expiryDate);
        SeleniumUtils.safeSendKeys(driver, cvvLocator, cvv);
    }
    
    /**
     * Continue to confirmation from payment
     */
    public void continueToConfirmation() {
        SeleniumUtils.safeClick(driver, continuePaymentLocator);
    }
    
    /**
     * Confirm order
     */
    public void confirmOrder() {
        SeleniumUtils.safeClick(driver, confirmOrderLocator);
    }
    
    /**
     * Complete checkout process
     */
    public void completeCheckout(String firstName, String lastName, String address, String state, String postalCode,
                                String cardNumber, String expiryDate, String cvv) {
        // Fill shipping information
        if (SeleniumUtils.isElementVisible(driver, firstNameLocator)) {
            fillShippingInformation(firstName, lastName, address, state, postalCode);
            continueToPayment();
        }
        
        // Fill payment information
        if (SeleniumUtils.isElementVisible(driver, creditCardLocator)) {
            fillPaymentInformation(cardNumber, expiryDate, cvv);
            continueToConfirmation();
        }
        
        // Confirm order
        if (SeleniumUtils.isElementVisible(driver, confirmOrderLocator)) {
            confirmOrder();
        }
    }
    
    /**
     * Check if order was successful
     */
    public boolean isOrderSuccessful() {
        return SeleniumUtils.isElementVisible(driver, successMessageLocator);
    }
    
    /**
     * Get success message
     */
    public String getSuccessMessage() {
        if (isOrderSuccessful()) {
            return SeleniumUtils.getTextSafely(driver, successMessageLocator);
        }
        return null;
    }
    
    /**
     * Get order summary
     */
    public String getOrderSummary() {
        if (SeleniumUtils.isElementVisible(driver, By.className("checkout-summary"))) {
            return SeleniumUtils.getTextSafely(driver, By.className("checkout-summary"));
        }
        return null;
    }
    
    /**
     * Check if on shipping step
     */
    public boolean isOnShippingStep() {
        return SeleniumUtils.isElementVisible(driver, firstNameLocator);
    }
    
    /**
     * Check if on payment step
     */
    public boolean isOnPaymentStep() {
        return SeleniumUtils.isElementVisible(driver, creditCardLocator);
    }
    
    /**
     * Check if on confirmation step
     */
    public boolean isOnConfirmationStep() {
        return SeleniumUtils.isElementVisible(driver, confirmOrderLocator);
    }
    
    /**
     * Get current checkout step
     */
    public String getCurrentStep() {
        if (isOnShippingStep()) {
            return "Shipping";
        } else if (isOnPaymentStep()) {
            return "Payment";
        } else if (isOnConfirmationStep()) {
            return "Confirmation";
        } else if (isOrderSuccessful()) {
            return "Success";
        }
        return "Unknown";
    }
}
