package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.CartPage;

/**
 * Test class for Product browsing and cart functionality
 */
public class ProductTest extends BaseTest {
    
    private HomePage homePage;
    private CartPage cartPage;
    
    @Test(groups = {"smoke", "regression"}, priority = 1,
          description = "Verify products are displayed on home page")
    public void testProductsDisplayed() {
        System.out.println("Starting Products Display Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Verify products are displayed
        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0, "Products should be displayed on home page");
        
        System.out.println("Found " + productCount + " products on home page");
        System.out.println("Products Display Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 2,
          description = "Verify adding product to cart")
    public void testAddProductToCart() {
        System.out.println("Starting Add Product to Cart Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Get initial cart quantity
        int initialCartQuantity = homePage.getCartQuantity();
        System.out.println("Initial cart quantity: " + initialCartQuantity);
        
        // Add first product to cart
        homePage.addFirstProductToCart();
        
        // Wait a moment for cart to update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify cart quantity increased
        int updatedCartQuantity = homePage.getCartQuantity();
        System.out.println("Updated cart quantity: " + updatedCartQuantity);
        Assert.assertTrue(updatedCartQuantity > initialCartQuantity, 
                         "Cart quantity should increase after adding product");
        
        System.out.println("Add Product to Cart Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 3,
          description = "Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        System.out.println("Starting Add Multiple Products Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Get initial cart quantity
        int initialCartQuantity = homePage.getCartQuantity();
        
        // Add multiple products to cart
        homePage.addProductToCartBySku("1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        homePage.addProductToCartBySku("2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify cart quantity increased by 2
        int finalCartQuantity = homePage.getCartQuantity();
        Assert.assertEquals(finalCartQuantity, initialCartQuantity + 2, 
                           "Cart should contain 2 more items");
        
        System.out.println("Add Multiple Products Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 4,
          description = "Verify cart page functionality")
    public void testCartPageFunctionality() {
        System.out.println("Starting Cart Page Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        
        // Add product to cart first
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to cart page
        homePage.clickCart();
        
        // Verify cart page is loaded
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        
        // Verify cart is not empty
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        
        // Verify cart items count
        int cartItemsCount = cartPage.getCartItemsCount();
        Assert.assertTrue(cartItemsCount > 0, "Cart should contain items");
        
        // Verify subtotal is displayed
        String subtotal = cartPage.getSubtotal();
        Assert.assertNotNull(subtotal, "Subtotal should be displayed");
        Assert.assertFalse(subtotal.equals("0"), "Subtotal should be greater than 0");
        
        System.out.println("Cart contains " + cartItemsCount + " items with subtotal: " + subtotal);
        System.out.println("Cart Page Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 5,
          description = "Verify removing product from cart")
    public void testRemoveProductFromCart() {
        System.out.println("Starting Remove Product from Cart Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        
        // Add product to cart first
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to cart page
        homePage.clickCart();
        
        // Get initial cart items count
        int initialItemsCount = cartPage.getCartItemsCount();
        Assert.assertTrue(initialItemsCount > 0, "Cart should have items before removal");
        
        // Remove first item
        cartPage.removeFirstItem();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify item was removed
        int updatedItemsCount = cartPage.getCartItemsCount();
        Assert.assertTrue(updatedItemsCount < initialItemsCount, 
                         "Cart items count should decrease after removal");
        
        System.out.println("Remove Product from Cart Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 6,
          description = "Verify empty cart functionality")
    public void testEmptyCartFunctionality() {
        System.out.println("Starting Empty Cart Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        
        // Navigate to cart page (assuming cart might be empty)
        homePage.clickCart();
        
        // If cart has items, remove all
        if (!cartPage.isCartEmpty()) {
            cartPage.removeAllItems();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        
        // Verify empty cart message
        String emptyMessage = cartPage.getEmptyCartMessage();
        Assert.assertNotNull(emptyMessage, "Empty cart message should be displayed");
        
        System.out.println("Empty cart message: " + emptyMessage);
        System.out.println("Empty Cart Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke"}, priority = 7,
          description = "Verify home page title and URL")
    public void testHomePageMetadata() {
        System.out.println("Starting Home Page Metadata Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        
        // Verify page title
        String pageTitle = homePage.getHomePageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        
        // Verify current URL
        String currentUrl = homePage.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Current URL should not be null");
        Assert.assertTrue(currentUrl.contains("testathon.live"), "URL should contain testathon.live");
        
        System.out.println("Page Title: " + pageTitle);
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Home Page Metadata Test Completed Successfully - " + getTestInfo());
    }
}
