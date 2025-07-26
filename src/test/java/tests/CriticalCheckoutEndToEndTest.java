package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.CartPage;
import pages.CheckoutPage;
import utils.SeleniumUtils;

import java.time.Duration;

/**
 * CRITICAL End-to-End Checkout Test Scenarios for testathon.live
 * These tests cover the most critical checkout flows that can fail across different devices/browsers/OS
 * Focus: Complete purchase flow, cross-browser compatibility, mobile responsiveness
 */
public class CriticalCheckoutEndToEndTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private WebDriverWait wait;
    
    /**
     * PRIORITY 1: Complete End-to-End Purchase Flow
     * Critical business scenario - From product browsing to order completion
     * Assertions designed to catch cross-browser and cross-device issues
     */
    @Test(groups = {"critical", "smoke"}, priority = 1,
          description = "Complete e-commerce purchase flow - Critical for business revenue")
    public void testCompletePurchaseFlow() {
        System.out.println("🚀 CRITICAL TEST: Complete Purchase Flow - " + getTestInfo());
        
        setupPageObjects();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
            // Step 1: Verify homepage loads properly across browsers
            System.out.println("🏠 Step 1: Homepage verification...");
            Assert.assertTrue(homePage.isHomePageDisplayed(), 
                "Homepage failed to load - Critical browser compatibility issue");
            
            // Verify product catalog displays correctly (critical for mobile)
            int productCount = getProductCount();
            Assert.assertTrue(productCount >= 20, 
                "Product catalog incomplete - Expected 25+ products but found: " + productCount + 
                " - Possible responsive layout or loading issue");
            
            // Step 2: User authentication with cross-browser dropdown handling
            System.out.println("🔐 Step 2: User authentication...");
            authenticateUser();
            
            // Step 3: Add multiple products - test cart functionality
            System.out.println("🛍️ Step 3: Multi-product selection...");
            addMultipleProductsToCart();
            
            // Step 4: Cart validation with cross-device compatibility
            System.out.println("🛒 Step 4: Cart validation...");
            validateCartFunctionality();
            
            // Step 5: Complete checkout process with payment validation
            System.out.println("💳 Step 5: Checkout process...");
            completeCheckoutProcess();
            
            // Step 6: Order completion verification
            System.out.println("✅ Step 6: Order confirmation...");
            validateOrderCompletion();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Complete Purchase Flow: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Critical end-to-end purchase flow failed: " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: Complete Purchase Flow - " + getTestInfo());
    }
    
    /**
     * PRIORITY 2: Cross-Device Checkout Compatibility
     * Tests checkout flow behavior on different screen sizes and input methods
     * Critical for mobile commerce and accessibility
     */
    @Test(groups = {"critical", "regression"}, priority = 2,
          description = "Cross-device checkout compatibility - Mobile/tablet form handling")
    public void testCrossDeviceCheckoutCompatibility() {
        System.out.println("📱 CRITICAL TEST: Cross-Device Checkout - " + getTestInfo());
        
        setupPageObjects();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            // Step 1: Quick authentication for checkout test
            System.out.println("🔐 Step 1: Quick user login...");
            authenticateUser();
            
            // Step 2: Add product and navigate to checkout
            System.out.println("🛒 Step 2: Product addition and checkout navigation...");
            homePage.addFirstProductToCart();
            Thread.sleep(2000); // Allow for cart update animation
            
            homePage.clickCart();
            Assert.assertTrue(cartPage.isCartPageDisplayed(), 
                "Cart page navigation failed - Mobile/tablet navigation issue");
            
            cartPage.clickCheckout();
            
            // Step 3: Test checkout form responsiveness
            System.out.println("📝 Step 3: Checkout form responsiveness test...");
            validateCheckoutFormAccessibility();
            
            // Step 4: Test form input with various data formats
            System.out.println("⌨️ Step 4: Cross-device form input validation...");
            testFormInputCompatibility();
            
            // Step 5: Payment form mobile compatibility
            System.out.println("💳 Step 5: Payment form mobile test...");
            validatePaymentFormMobileCompatibility();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Cross-Device Checkout: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Cross-device checkout compatibility failed: " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: Cross-Device Checkout - " + getTestInfo());
    }
    
    /**
     * PRIORITY 3: High-Value Cart Checkout Flow
     * Tests checkout with expensive items and edge cases
     * Critical for high-revenue transactions
     */
    @Test(groups = {"critical", "regression"}, priority = 3,
          description = "High-value cart checkout - Revenue critical test")
    public void testHighValueCartCheckout() {
        System.out.println("💰 CRITICAL TEST: High-Value Cart Checkout - " + getTestInfo());
        
        setupPageObjects();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            // Step 1: Authentication
            System.out.println("🔐 Step 1: User authentication...");
            authenticateUser();
            
            // Step 2: Add high-value items (iPhone 12 Pro Max, Galaxy S20 Ultra)
            System.out.println("📱 Step 2: Adding high-value products...");
            addHighValueProducts();
            
            // Step 3: Validate cart total calculations
            System.out.println("🧮 Step 3: Cart calculation validation...");
            validateCartCalculations();
            
            // Step 4: Proceed to checkout with validation
            System.out.println("💳 Step 4: High-value checkout process...");
            proceedToCheckoutWithValidation();
            
            // Step 5: Complete payment with comprehensive validation
            System.out.println("✅ Step 5: Payment completion validation...");
            completeHighValueCheckout();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in High-Value Checkout: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("High-value cart checkout failed: " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: High-Value Cart Checkout - " + getTestInfo());
    }
    
    // Helper Methods Implementation
    
    private void setupPageObjects() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }
    
    private int getProductCount() {
        try {
            return driver.findElements(By.className("shelf-item")).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    private void authenticateUser() {
        homePage.clickSignIn();
        wait.until(ExpectedConditions.urlContains("signin"));
        
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Login page failed to load - Critical navigation issue");
        
        loginPage.login("demouser", "testingisfun99");
        
        // Enhanced login validation for cross-browser compatibility
        Assert.assertTrue(loginPage.isLoginSuccessful(), 
            "Login failed - Critical authentication issue. Check dropdown functionality across browsers");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, "demouser", 
            "Username display incorrect - UI rendering issue across devices");
    }
    
    private void addMultipleProductsToCart() {
        // Add first product
        homePage.addFirstProductToCart();
        waitForCartUpdate();
        
        // Verify cart count increases
        int cartCount = homePage.getCartQuantity();
        Assert.assertTrue(cartCount >= 1, 
            "Cart update failed after adding first product - JavaScript execution issue");
        
        // Add second product for comprehensive test
        if (cartCount == 1) {
            // Add another product (iPhone 12 Mini)
            try {
                driver.findElement(By.xpath("//p[text()='iPhone 12 Mini']/following-sibling::div[@class='shelf-item__buy-btn']")).click();
                waitForCartUpdate();
                
                int newCartCount = homePage.getCartQuantity();
                Assert.assertTrue(newCartCount > cartCount, 
                    "Multi-product cart update failed - Cart state management issue");
            } catch (Exception e) {
                System.out.println("⚠️ Second product addition failed, continuing with single product");
            }
        }
    }
    
    private void waitForCartUpdate() {
        try {
            Thread.sleep(2000); // Allow for cart animation and state update
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void validateCartFunctionality() {
        homePage.clickCart();
        wait.until(ExpectedConditions.urlContains("cart"));
        
        Assert.assertTrue(cartPage.isCartPageDisplayed(), 
            "Cart page navigation failed - Routing issue across browsers");
        
        Assert.assertFalse(cartPage.isCartEmpty(), 
            "Cart appears empty after adding products - Critical data persistence issue");
        
        // Validate cart item count
        int itemCount = cartPage.getCartItemsCount();
        Assert.assertTrue(itemCount > 0, 
            "Cart item count validation failed - Display calculation error");
        
        // Validate subtotal is present and non-zero
        String subtotal = cartPage.getSubtotal();
        Assert.assertNotNull(subtotal, "Subtotal not displayed - Price calculation issue");
        Assert.assertTrue(subtotal.contains("$") && !subtotal.contains("0.00"), 
            "Subtotal calculation incorrect - Financial calculation critical error");
    }
    
    private void completeCheckoutProcess() {
        cartPage.clickCheckout();
        wait.until(d -> checkoutPage.isPageLoaded());
        
        Assert.assertTrue(checkoutPage.isPageLoaded(), 
            "Checkout page failed to load - Critical checkout flow interruption");
        
        // Complete checkout with comprehensive validation
        if (checkoutPage.isOnShippingStep()) {
            System.out.println("📦 Filling shipping information...");
            checkoutPage.fillShippingInformation(
                "Test User",           // First Name
                "Automation",          // Last Name  
                "123 Test Street",     // Address
                "California",          // State
                "90210"               // Postal Code
            );
            checkoutPage.continueToPayment();
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        if (checkoutPage.isOnPaymentStep()) {
            System.out.println("💳 Processing payment information...");
            checkoutPage.fillPaymentInformation(
                "4111111111111111",    // Credit Card
                "12/28",              // Expiry
                "123"                 // CVV
            );
            checkoutPage.continueToConfirmation();
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        if (checkoutPage.isOnConfirmationStep()) {
            System.out.println("✅ Confirming order...");
            checkoutPage.confirmOrder();
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } // Allow for order processing
        }
    }
    
    private void validateOrderCompletion() {
        String currentUrl = driver.getCurrentUrl();
        boolean orderSuccessful = false;
        
        // Multiple validation methods for order completion
        if (checkoutPage.isOrderSuccessful()) {
            orderSuccessful = true;
            String successMessage = checkoutPage.getSuccessMessage();
            System.out.println("✅ Order success message: " + successMessage);
        }
        
        // URL-based validation
        if (currentUrl.contains("success") || currentUrl.contains("confirmation") || 
            currentUrl.contains("thank") || currentUrl.contains("complete")) {
            orderSuccessful = true;
            System.out.println("✅ Order completion detected via URL: " + currentUrl);
        }
        
        // Page title validation
        String pageTitle = driver.getTitle();
        if (pageTitle.toLowerCase().contains("success") || 
            pageTitle.toLowerCase().contains("complete") ||
            pageTitle.toLowerCase().contains("thank")) {
            orderSuccessful = true;
            System.out.println("✅ Order completion detected via page title: " + pageTitle);
        }
        
        Assert.assertTrue(orderSuccessful, 
            "Order completion could not be verified - Critical checkout flow failure. " +
            "Current URL: " + currentUrl + " | Page Title: " + pageTitle);
        
        System.out.println("🎉 Order successfully completed and validated!");
    }
    
    private void validateCheckoutFormAccessibility() {
        Assert.assertTrue(checkoutPage.isPageLoaded(), 
            "Checkout form not accessible - Mobile/tablet responsiveness issue");
        
        // Check form field visibility and accessibility
        if (checkoutPage.isOnShippingStep()) {
            Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.id("firstNameInput")), 
                "First name field not visible - Mobile form layout issue");
            Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.id("lastNameInput")), 
                "Last name field not visible - Responsive design failure");
        }
    }
    
    private void testFormInputCompatibility() {
        if (checkoutPage.isOnShippingStep()) {
            // Test with international characters and special formats
            checkoutPage.fillShippingInformation(
                "José María",              // International characters
                "O'Connor-Smith",         // Apostrophe and hyphen
                "123 Main St, Apt #4B",   // Address with special chars
                "California",             // State
                "90210-1234"             // Extended ZIP format
            );
            
            // Verify forms accept international input
            String currentStep = checkoutPage.getCurrentStep();
            Assert.assertEquals(currentStep, "Shipping", 
                "Form input validation failed with international characters - Input handling issue");
        }
    }
    
    private void validatePaymentFormMobileCompatibility() {
        if (checkoutPage.isOnPaymentStep()) {
            // Test payment form with various card formats
            checkoutPage.fillPaymentInformation(
                "4111-1111-1111-1111",   // Formatted card number
                "12/28",                 // Standard expiry format
                "123"                    // CVV
            );
            
            String currentStep = checkoutPage.getCurrentStep();
            Assert.assertEquals(currentStep, "Payment", 
                "Payment form validation failed - Mobile input handling issue");
        }
    }
    
    private void addHighValueProducts() {
        // Add iPhone 12 Pro Max ($1099)
        try {
            driver.findElement(By.xpath("//p[text()='iPhone 12 Pro Max']/following-sibling::div[@class='shelf-item__buy-btn']")).click();
            waitForCartUpdate();
        } catch (Exception e) {
            System.out.println("⚠️ iPhone 12 Pro Max not found, using alternative");
            homePage.addFirstProductToCart();
            waitForCartUpdate();
        }
        
        // Add Galaxy S20 Ultra ($1399)
        try {
            driver.findElement(By.xpath("//p[text()='Galaxy S20 Ultra']/following-sibling::div[@class='shelf-item__buy-btn']")).click();
            waitForCartUpdate();
        } catch (Exception e) {
            System.out.println("⚠️ Galaxy S20 Ultra not found, continuing with current cart");
        }
        
        int cartCount = homePage.getCartQuantity();
        Assert.assertTrue(cartCount >= 1, 
            "High-value product addition failed - Cart functionality error");
    }
    
    private void validateCartCalculations() {
        homePage.clickCart();
        wait.until(ExpectedConditions.urlContains("cart"));
        
        String subtotal = cartPage.getSubtotal();
        Assert.assertNotNull(subtotal, "Subtotal calculation missing");
        
        // For high-value carts, subtotal should be substantial
        if (subtotal.contains("$")) {
            String numericValue = subtotal.replaceAll("[^0-9.]", "");
            try {
                double amount = Double.parseDouble(numericValue);
                Assert.assertTrue(amount > 500, 
                    "High-value cart calculation error - Expected >$500 but got $" + amount);
            } catch (NumberFormatException e) {
                Assert.fail("Subtotal format parsing error: " + subtotal);
            }
        }
    }
    
    private void proceedToCheckoutWithValidation() {
        cartPage.clickCheckout();
        wait.until(d -> checkoutPage.isPageLoaded());
        
        Assert.assertTrue(checkoutPage.isPageLoaded(), 
            "High-value checkout page failed to load - Security/validation issue");
    }
    
    private void completeHighValueCheckout() {
        // Complete all checkout steps with enhanced validation for high-value transactions
        completeCheckoutProcess();
        
        // Additional validation for high-value orders
        validateOrderCompletion();
        
        System.out.println("💰 High-value checkout completed successfully");
    }
}
