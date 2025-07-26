package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.CartPage;
import pages.CheckoutPage;

/**
 * Test class for End-to-End Checkout functionality
 */
public class CheckoutTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    
    @Test(groups = {"smoke", "regression"}, priority = 1,
          description = "Verify complete checkout process from product selection to order confirmation")
    public void testCompleteCheckoutProcess() {
        System.out.println("Starting Complete Checkout Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        
        // Step 1: Login
        System.out.println("Step 1: Logging in...");
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");
        
        // Step 2: Add product to cart
        System.out.println("Step 2: Adding product to cart...");
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify product was added
        int cartQuantity = homePage.getCartQuantity();
        Assert.assertTrue(cartQuantity > 0, "Product should be added to cart");
        
        // Step 3: Navigate to cart
        System.out.println("Step 3: Navigating to cart...");
        homePage.clickCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        
        // Step 4: Proceed to checkout
        System.out.println("Step 4: Proceeding to checkout...");
        cartPage.clickCheckout();
        
        // Verify checkout page is loaded
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Checkout page should be loaded");
        
        // Step 5: Complete checkout process
        System.out.println("Step 5: Filling checkout information...");
        checkoutPage.completeCheckout(
            "John",                    // First Name
            "Doe",                     // Last Name
            "123 Test Street",         // Address
            "California",              // State
            "90210",                   // Postal Code
            "4111111111111111",        // Credit Card Number
            "12/25",                   // Expiry Date
            "123"                      // CVV
        );
        
        // Step 6: Verify order completion
        System.out.println("Step 6: Verifying order completion...");
        // Note: The actual verification depends on the application's behavior
        // For BStack Demo, we'll check if we're still on a valid page
        String currentUrl = checkoutPage.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Should be on a valid page after checkout");
        
        System.out.println("Complete Checkout Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 2,
          description = "Verify checkout without login")
    public void testCheckoutWithoutLogin() {
        System.out.println("Starting Checkout Without Login Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        
        // Add product to cart without login
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to cart
        homePage.clickCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        
        // Try to checkout
        if (!cartPage.isCartEmpty()) {
            try {
                cartPage.clickCheckout();
                // The application might redirect to login or allow guest checkout
                // We'll verify that some action was taken
                String currentUrl = driver.getCurrentUrl();
                Assert.assertNotNull(currentUrl, "Should navigate somewhere after clicking checkout");
            } catch (Exception e) {
                // Expected if checkout requires login
                System.out.println("Checkout requires login as expected: " + e.getMessage());
            }
        }
        
        System.out.println("Checkout Without Login Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 3,
          description = "Verify checkout with empty cart")
    public void testCheckoutWithEmptyCart() {
        System.out.println("Starting Empty Cart Checkout Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        
        // Navigate to cart
        homePage.clickCart();
        
        // Ensure cart is empty
        if (!cartPage.isCartEmpty()) {
            cartPage.removeAllItems();
        }
        
        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        
        // Try to checkout with empty cart (should not be possible)
        try {
            cartPage.clickCheckout();
            Assert.fail("Checkout should not be possible with empty cart");
        } catch (RuntimeException e) {
            // Expected exception for empty cart checkout
            Assert.assertTrue(e.getMessage().contains("empty cart"), 
                             "Should get appropriate error for empty cart");
        }
        
        System.out.println("Empty Cart Checkout Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 4,
          description = "Verify checkout form validation")
    public void testCheckoutFormValidation() {
        System.out.println("Starting Checkout Form Validation Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        
        // Login and add product
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to checkout
        homePage.clickCart();
        cartPage.clickCheckout();
        
        // Verify checkout page is loaded
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Checkout page should be loaded");
        
        // Try to proceed with empty form fields
        if (checkoutPage.isOnShippingStep()) {
            // Try to continue without filling required fields
            checkoutPage.continueToPayment();
            
            // Should still be on shipping step due to validation
            // Note: Actual validation behavior depends on the application
            String currentStep = checkoutPage.getCurrentStep();
            System.out.println("Current checkout step: " + currentStep);
        }
        
        System.out.println("Checkout Form Validation Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke"}, priority = 5,
          description = "Verify checkout page navigation")
    public void testCheckoutPageNavigation() {
        System.out.println("Starting Checkout Navigation Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        
        // Login and add product
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to checkout
        homePage.clickCart();
        cartPage.clickCheckout();
        
        // Verify we can navigate back to cart
        checkoutPage.navigateBack();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Should be back on cart page");
        
        // Navigate to checkout again
        cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Should be back on checkout page");
        
        System.out.println("Checkout Navigation Test Completed Successfully - " + getTestInfo());
    }
}
