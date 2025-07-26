package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ImageValidationUtils;

/**
 * Test class for validating image loading issues
 * This test logs in with image_not_loading_user and validates images on various pages
 * Tests are designed to FAIL when images are not loading properly
 */
public class ImageLoadingValidationTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    
    @Test(groups = {"regression", "image_validation"}, priority = 1,
          description = "Validate image loading on Home page with image_not_loading_user - Expected to FAIL")
    public void testImageLoadingOnHomePage() {
        System.out.println("🖼️ Starting Image Loading Validation Test on Home Page - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Step 1: Login with image_not_loading_user
        System.out.println("🔐 Step 1: Logging in with image_not_loading_user...");
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        loginPage.login("image_not_loading_user", "testingisfun99");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, "image_not_loading_user", "Correct username should be displayed");
        
        System.out.println("✅ Successfully logged in as: " + loggedInUser);
        
        // Step 2: Navigate back to home page and validate images
        System.out.println("🏠 Step 2: Validating images on Home page...");
        driver.get(driver.getCurrentUrl().split("\\?")[0]); // Remove query parameters to go to home
        
        // Wait a moment for images to load
        ImageValidationUtils.waitForImagesToLoad(driver, 5);
        
        // Validate all images are loaded - THIS SHOULD FAIL for image_not_loading_user
        boolean allImagesLoaded = ImageValidationUtils.validateAllImagesLoaded(driver, "Home Page");
        
        // Additional detailed validation
        int brokenImagesCount = ImageValidationUtils.countBrokenImages(driver);
        System.out.println("📊 Image Loading Summary:");
        System.out.println("   - Total broken images found: " + brokenImagesCount);
        
        // Assert that images are loading properly - THIS WILL FAIL INTENTIONALLY
        Assert.assertTrue(allImagesLoaded, 
            "❌ EXPECTED FAILURE: Images are not loading properly on Home page for image_not_loading_user. " +
            "Found " + brokenImagesCount + " broken images. This test is designed to fail when images don't load.");
        
        System.out.println("Image Loading Validation Test Completed - " + getTestInfo());
    }
    
    @Test(groups = {"regression", "image_validation"}, priority = 2, dependsOnMethods = {"testImageLoadingOnHomePage"},
          description = "Validate image loading on Offers page with image_not_loading_user - Expected to FAIL")
    public void testImageLoadingOnOffersPage() {
        System.out.println("🛍️ Starting Image Loading Validation Test on Offers Page - " + getTestInfo());
        
        // Navigate to Offers page
        System.out.println("🎯 Step 1: Navigating to Offers page...");
        homePage.navigateToOffers();
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for images to load
        ImageValidationUtils.waitForImagesToLoad(driver, 5);
        
        // Step 2: Validate images on Offers page
        System.out.println("🖼️ Step 2: Validating images on Offers page...");
        boolean allImagesLoaded = ImageValidationUtils.validateAllImagesLoaded(driver, "Offers Page");
        
        int brokenImagesCount = ImageValidationUtils.countBrokenImages(driver);
        System.out.println("📊 Offers Page Image Summary:");
        System.out.println("   - Total broken images found: " + brokenImagesCount);
        
        // Assert that images are loading properly - THIS WILL FAIL INTENTIONALLY
        Assert.assertTrue(allImagesLoaded, 
            "❌ EXPECTED FAILURE: Images are not loading properly on Offers page for image_not_loading_user. " +
            "Found " + brokenImagesCount + " broken images. This test is designed to fail when images don't load.");
        
        System.out.println("Offers Page Image Loading Validation Completed - " + getTestInfo());
    }
    
    @Test(groups = {"regression", "image_validation"}, priority = 3, dependsOnMethods = {"testImageLoadingOnHomePage"},
          description = "Validate image loading on Orders page with image_not_loading_user - Expected to FAIL")
    public void testImageLoadingOnOrdersPage() {
        System.out.println("📦 Starting Image Loading Validation Test on Orders Page - " + getTestInfo());
        
        // Navigate to Orders page
        System.out.println("📋 Step 1: Navigating to Orders page...");
        homePage.navigateToOrders();
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for images to load
        ImageValidationUtils.waitForImagesToLoad(driver, 5);
        
        // Step 2: Validate images on Orders page
        System.out.println("🖼️ Step 2: Validating images on Orders page...");
        boolean allImagesLoaded = ImageValidationUtils.validateAllImagesLoaded(driver, "Orders Page");
        
        int brokenImagesCount = ImageValidationUtils.countBrokenImages(driver);
        System.out.println("📊 Orders Page Image Summary:");
        System.out.println("   - Total broken images found: " + brokenImagesCount);
        
        // Assert that images are loading properly - THIS WILL FAIL INTENTIONALLY
        Assert.assertTrue(allImagesLoaded, 
            "❌ EXPECTED FAILURE: Images are not loading properly on Orders page for image_not_loading_user. " +
            "Found " + brokenImagesCount + " broken images. This test is designed to fail when images don't load.");
        
        System.out.println("Orders Page Image Loading Validation Completed - " + getTestInfo());
    }
    
    @Test(groups = {"regression", "image_validation"}, priority = 4, dependsOnMethods = {"testImageLoadingOnHomePage"},
          description = "Validate image loading on Favourites page with image_not_loading_user - Expected to FAIL")
    public void testImageLoadingOnFavouritesPage() {
        System.out.println("⭐ Starting Image Loading Validation Test on Favourites Page - " + getTestInfo());
        
        // Navigate to Favourites page
        System.out.println("💖 Step 1: Navigating to Favourites page...");
        homePage.navigateToFavourites();
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for images to load
        ImageValidationUtils.waitForImagesToLoad(driver, 5);
        
        // Step 2: Validate images on Favourites page
        System.out.println("🖼️ Step 2: Validating images on Favourites page...");
        boolean allImagesLoaded = ImageValidationUtils.validateAllImagesLoaded(driver, "Favourites Page");
        
        int brokenImagesCount = ImageValidationUtils.countBrokenImages(driver);
        System.out.println("📊 Favourites Page Image Summary:");
        System.out.println("   - Total broken images found: " + brokenImagesCount);
        
        // Assert that images are loading properly - THIS WILL FAIL INTENTIONALLY
        Assert.assertTrue(allImagesLoaded, 
            "❌ EXPECTED FAILURE: Images are not loading properly on Favourites page for image_not_loading_user. " +
            "Found " + brokenImagesCount + " broken images. This test is designed to fail when images don't load.");
        
        System.out.println("Favourites Page Image Loading Validation Completed - " + getTestInfo());
    }
    
    @Test(groups = {"regression", "image_validation"}, priority = 5, dependsOnMethods = {"testImageLoadingOnHomePage"},
          description = "Validate image loading on Cart page with image_not_loading_user - Expected to FAIL")
    public void testImageLoadingOnCartPage() {
        System.out.println("🛒 Starting Image Loading Validation Test on Cart Page - " + getTestInfo());
        
        // Step 1: Add a product to cart and navigate to cart page
        System.out.println("🛍️ Step 1: Adding product to cart and navigating to cart page...");
        
        // Go back to home page first
        driver.get(driver.getCurrentUrl().split("\\?")[0]);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Add product to cart
        homePage.addFirstProductToCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate to cart
        homePage.clickCart();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for images to load
        ImageValidationUtils.waitForImagesToLoad(driver, 5);
        
        // Step 2: Validate images on Cart page
        System.out.println("🖼️ Step 2: Validating images on Cart page...");
        boolean allImagesLoaded = ImageValidationUtils.validateAllImagesLoaded(driver, "Cart Page");
        
        int brokenImagesCount = ImageValidationUtils.countBrokenImages(driver);
        System.out.println("📊 Cart Page Image Summary:");
        System.out.println("   - Total broken images found: " + brokenImagesCount);
        
        // Assert that images are loading properly - THIS WILL FAIL INTENTIONALLY
        Assert.assertTrue(allImagesLoaded, 
            "❌ EXPECTED FAILURE: Images are not loading properly on Cart page for image_not_loading_user. " +
            "Found " + brokenImagesCount + " broken images. This test is designed to fail when images don't load.");
        
        System.out.println("Cart Page Image Loading Validation Completed - " + getTestInfo());
    }
}
