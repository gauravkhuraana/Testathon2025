package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.FavoritesPage;
import utils.SeleniumUtils;

/**
 * CRITICAL Favorites Test Scenario for fav_user
 * Tests the complete favorites functionality workflow for fav_user
 * Focus: End-to-end scenario, login validation, favorites page access, cross-browser compatibility
 */
public class FavoritesTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private FavoritesPage favoritesPage;
    
    /**
     * PRIORITY 1: Critical Favorites Functionality Test for fav_user
     * Complete end-to-end test covering login and favorites validation
     * Designed to catch device/browser/OS specific issues with favorites functionality
     */
    @Test(groups = {"critical", "smoke", "favorites"}, priority = 1,
          description = "Verify fav_user can login and access favorites functionality - End-to-end critical test")
    public void testFavUserLoginAndFavoritesAccess() {
        System.out.println("⭐ CRITICAL TEST: fav_user Login and Favorites Access - " + getTestInfo());
        
        try {
            // Step 1: Initialize page objects
            setupPageObjects();
            System.out.println("📝 Step 1: Page objects initialized");
            
            // Step 2: Navigate to login and verify page loads
            System.out.println("🔐 Step 2: Navigating to login page...");
            homePage.clickSignIn();
            
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "CRITICAL FAILURE: Login page failed to load - Navigation issue across browsers");
            
            // Step 3: Login with fav_user
            System.out.println("👤 Step 3: Authenticating fav_user...");
            loginPage.login("fav_user", "testingisfun99");
            
            // Step 4: Validate login success with comprehensive checks
            System.out.println("✅ Step 4: Validating login success...");
            validateLoginSuccess();
            
            // Step 5: Navigate to Favorites page
            System.out.println("⭐ Step 5: Navigating to Favorites page...");
            homePage.navigateToFavourites();
            
            // Step 6: Validate Favorites page accessibility and functionality
            System.out.println("🔍 Step 6: Validating favorites functionality...");
            validateFavoritesFunctionality();
            
            // Step 7: Comprehensive favorites validation specific to fav_user
            System.out.println("🎯 Step 7: fav_user specific validation...");
            validateFavUserSpecificFeatures();
            
            System.out.println("🎉 CRITICAL TEST PASSED: fav_user favorites functionality validated - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in fav_user favorites test: " + e.getMessage());
            
            // Take screenshot for debugging
            String screenshotPath = favoritesPage != null ? 
                favoritesPage.takeScreenshot("fav_user_test_failed") : 
                takeScreenshot("fav_user_test_failed");
            System.out.println("📸 Screenshot saved: " + screenshotPath);
            
            e.printStackTrace();
            Assert.fail("fav_user favorites functionality test failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to setup page objects
     */
    private void setupPageObjects() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        favoritesPage = new FavoritesPage(driver);
        
        // Verify home page is accessible
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Home page should be displayed initially - Application availability issue");
    }
    
    /**
     * Comprehensive login validation for fav_user
     */
    private void validateLoginSuccess() {
        // Multi-layered validation for cross-browser compatibility
        Assert.assertTrue(loginPage.isLoginSuccessful(), 
            "fav_user login failed - Authentication issue. Check dropdown functionality across browsers");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertNotNull(loggedInUser, 
            "Username not displayed after login - UI rendering issue for fav_user");
        
        Assert.assertEquals(loggedInUser, "fav_user", 
            "Incorrect username displayed - Expected: fav_user, Actual: " + loggedInUser + 
            " - User session management issue");
        
        // Verify navigation elements are available after login
        Assert.assertTrue(homePage.areNavigationLinksVisible(), 
            "Navigation links not available after fav_user login - Post-login navigation issue");
        
        System.out.println("✅ fav_user login validation successful");
    }
    
    /**
     * Validate Favorites page functionality
     */
    private void validateFavoritesFunctionality() {
        // Wait for Favorites page to load
        Assert.assertTrue(favoritesPage.waitForFavoritesToLoad(15), 
            "CRITICAL: Favorites page failed to load within timeout - Page loading issue across devices");
        
        // Verify page is loaded and accessible
        Assert.assertTrue(favoritesPage.isFavoritesPageDisplayed(), 
            "CRITICAL: Favorites page not displayed after navigation - Page accessibility issue");
        
        // Verify URL contains favorites/favourites
        String currentUrl = favoritesPage.getCurrentUrl().toLowerCase();
        Assert.assertTrue(currentUrl.contains("favourites") || currentUrl.contains("favorites") || currentUrl.contains("wishlist"), 
            "CRITICAL: URL does not indicate Favorites page - Navigation routing issue. Current URL: " + currentUrl);
        
        System.out.println("✅ Favorites page accessibility validated");
    }
    
    /**
     * Validate fav_user specific features and functionality
     */
    private void validateFavUserSpecificFeatures() {
        // Comprehensive fav_user functionality validation
        Assert.assertTrue(favoritesPage.validateFavUserFunctionality(), 
            "CRITICAL: fav_user specific favorites functionality validation failed - User permission or data access issue");
        
        // Additional assertions that can fail across different platforms
        Assert.assertTrue(favoritesPage.isFavoritesFunctionalityAccessible(), 
            "CRITICAL: Favorites functionality not accessible for fav_user - User role/permission issue");
        
        // Log detailed state for debugging
        String favoritesState = favoritesPage.validateFavoritesPageState();
        System.out.println("📊 Detailed Favorites State:\n" + favoritesState);
        
        // Validate either favorites exist OR empty state is properly handled
        boolean hasFavorites = favoritesPage.areFavoritesDisplayed();
        boolean hasEmptyMessage = favoritesPage.isNoFavoritesMessageDisplayed();
        
        Assert.assertTrue(hasFavorites || hasEmptyMessage, 
            "CRITICAL: Neither favorites nor empty state message displayed - UI state management issue");
        
        if (hasFavorites) {
            int favoritesCount = favoritesPage.getFavoritesCount();
            Assert.assertTrue(favoritesCount > 0, 
                "Favorites displayed but count is zero - Data consistency issue");
            System.out.println("✅ fav_user has " + favoritesCount + " favorite items");
        } else {
            System.out.println("✅ fav_user favorites list is empty (valid state)");
        }
        
        // Verify page title indicates favorites functionality
        String pageTitle = favoritesPage.getPageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        System.out.println("📄 Favorites page title: " + pageTitle);
        
        System.out.println("✅ fav_user specific features validation completed");
    }
    
    /**
     * Helper method to take screenshot
     */
    private String takeScreenshot(String testName) {
        try {
            return SeleniumUtils.takeScreenshot(driver, testName);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
}
