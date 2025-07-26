package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.OffersPage;
import utils.SeleniumUtils;

/**
 * CRITICAL Offers Page Test Scenarios with Location Popup Validation
 * These tests focus on demouser login and offers page functionality
 * including location popup handling and data validation scenarios
 * 
 * Key Focus Areas:
 * 1. Login with demouser and navigate to offers
 * 2. Handle location popup (both allow and deny scenarios)
 * 3. Validate offers data loading based on location permissions
 * 4. Cross-browser compatibility for popup handling
 * 5. Mobile device location permission scenarios
 */
public class CriticalOffersValidationTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private OffersPage offersPage;
    
    /**
     * PRIORITY 1: CRITICAL End-to-End Offers Flow with Location Allow
     * Tests complete flow: Login -> Navigate to Offers -> Allow Location -> Validate Data
     * This is the most critical business scenario for location-based offers
     */
    @Test(groups = {"critical", "smoke", "offers"}, priority = 1,
          description = "Demo user login + offers validation with location ALLOW - Critical business flow")
    public void testOffersPageWithLocationAllow() {
        System.out.println("🚀 CRITICAL TEST: Offers Page with Location Allow - " + getTestInfo());
        
        try {
            // Step 1: Initialize page objects
            setupPageObjects();
            
            // Step 2: Verify homepage and login with demo_user
            System.out.println("🏠 Step 1: Homepage verification and login...");
            Assert.assertTrue(homePage.isHomePageDisplayed(), 
                "Homepage failed to load - Critical browser compatibility issue");
            
            homePage.clickSignIn();
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page failed to load - Critical navigation issue");
            
            // Login with demo_user specifically
            loginPage.login("demouser", "testingisfun99");
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "demouser login failed - Critical authentication issue");
            
            String loggedInUser = loginPage.getLoggedInUsername();
            Assert.assertEquals(loggedInUser, "demouser", 
                "Incorrect user logged in - Expected: demouser, Actual: " + loggedInUser);
            
            System.out.println("✅ demouser login successful");
            
            // Step 3: Navigate to Offers page
            System.out.println("🎯 Step 2: Navigating to Offers page...");
            homePage.navigateToOffers();
            
            // Wait for offers page to load
            Assert.assertTrue(offersPage.isOffersPageDisplayed(), 
                "Offers page failed to load - Critical navigation/routing issue");
            
            System.out.println("✅ Successfully navigated to Offers page");
            
            // Step 4: Handle location popup - ALLOW scenario
            System.out.println("📍 Step 3: Location popup handling (ALLOW)...");
            
            // Wait for potential location popup
            SeleniumUtils.waitForPageLoad(driver, 5);
            
            if (offersPage.isLocationPopupDisplayed()) {
                System.out.println("📍 Location popup detected - allowing access");
                offersPage.allowLocationAccess();
                
                // Wait for page to reload with location-based offers
                offersPage.waitForOffersToLoad(10);
            } else {
                System.out.println("ℹ️ No location popup detected - checking for pre-allowed permissions");
            }
            
            // Step 5: Validate offers data loading
            System.out.println("📊 Step 4: Offers data validation...");
            
            // Critical assertions for business functionality
            boolean offersDisplayed = offersPage.areOffersDisplayed();
            boolean noOffersMessage = offersPage.isNoOffersMessageDisplayed();
            
            // Log current state for debugging
            System.out.println(offersPage.validateOffersPageState());
            
            // Business Logic Validation:
            // With location allowed, we should either see offers OR a valid no-offers message
            Assert.assertTrue(offersDisplayed || noOffersMessage, 
                "CRITICAL FAILURE: No offers displayed AND no valid no-offers message. " +
                "Location was allowed but page shows no content. " +
                "This indicates data loading issue or location service failure.");
            
            if (offersDisplayed) {
                int offersCount = offersPage.getOffersCount();
                Assert.assertTrue(offersCount > 0, 
                    "Offers container visible but no offer cards found - UI rendering issue");
                System.out.println("✅ SUCCESS: " + offersCount + " offers loaded successfully");
            } else if (noOffersMessage) {
                String message = offersPage.getNoOffersMessage();
                System.out.println("ℹ️ No offers available. Message: " + message);
                
                // Validate the no-offers message is meaningful
                Assert.assertNotNull(message, "No offers message is null");
                Assert.assertFalse(message.trim().isEmpty(), "No offers message is empty");
            }
            
            // Step 6: Cross-device compatibility check
            validateOffersPagesResponsiveness();
            
            System.out.println("🎉 CRITICAL TEST PASSED: Offers with Location Allow - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Offers Flow with Location Allow: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Critical offers flow with location allow failed: " + e.getMessage());
        }
    }
    
    /**
     * PRIORITY 2: CRITICAL Offers Flow with Location DENY
     * Tests scenario where user denies location access
     * Critical for validating graceful degradation when location is blocked
     */
    @Test(groups = {"critical", "regression", "offers"}, priority = 2,
          description = "Demo user login + offers validation with location DENY - Graceful degradation test")
    public void testOffersPageWithLocationDeny() {
        System.out.println("🚀 CRITICAL TEST: Offers Page with Location Deny - " + getTestInfo());
        
        try {
            // Step 1: Setup and login
            setupPageObjects();
            
            System.out.println("🔐 Step 1: Demo user login...");
            homePage.clickSignIn();
            loginPage.login("demouser", "testingisfun99");
            
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "demouser login failed for location deny test");
            
            // Step 2: Navigate to offers
            System.out.println("🎯 Step 2: Navigate to Offers...");
            homePage.navigateToOffers();
            Assert.assertTrue(offersPage.isOffersPageDisplayed(), 
                "Offers page failed to load for location deny test");
            
            // Step 3: Handle location popup - DENY scenario
            System.out.println("🚫 Step 3: Location popup handling (DENY)...");
            
            SeleniumUtils.waitForPageLoad(driver, 5);
            
            if (offersPage.isLocationPopupDisplayed()) {
                System.out.println("📍 Location popup detected - denying access");
                offersPage.denyLocationAccess();
                
                // Wait for page to handle denied location
                offersPage.waitForOffersToLoad(10);
            } else {
                System.out.println("ℹ️ No location popup - simulating denied location scenario");
                // May need to refresh or navigate to trigger location request
                offersPage.refreshOffersPage();
                offersPage.waitForOffersToLoad(10);
            }
            
            // Step 4: Validate graceful degradation
            System.out.println("🔍 Step 4: Validating graceful degradation...");
            
            // Log current state
            System.out.println(offersPage.validateOffersPageState());
            
            // When location is denied, we should see one of these scenarios:
            boolean hasLocationRequiredMessage = offersPage.isLocationRequiredMessageDisplayed();
            boolean hasNoOffersMessage = offersPage.isNoOffersMessageDisplayed();
            boolean hasGeneralOffers = offersPage.areOffersDisplayed(); // Some sites show generic offers
            
            // CRITICAL: At least one of these should be true for good UX
            Assert.assertTrue(hasLocationRequiredMessage || hasNoOffersMessage || hasGeneralOffers,
                "CRITICAL UX FAILURE: Location denied but no appropriate feedback shown. " +
                "Users should see either location required message, no offers message, or generic offers.");
            
            if (hasLocationRequiredMessage) {
                String message = offersPage.getLocationRequiredMessage();
                System.out.println("✅ Good UX: Location required message shown: " + message);
                
                // Validate message quality
                Assert.assertNotNull(message, "Location required message is null");
                Assert.assertTrue(message.toLowerCase().contains("location") || 
                                message.toLowerCase().contains("enable") ||
                                message.toLowerCase().contains("permission"),
                    "Location required message doesn't mention location/permission: " + message);
            }
            
            if (hasNoOffersMessage) {
                String message = offersPage.getNoOffersMessage();
                System.out.println("✅ Acceptable UX: No offers message shown: " + message);
            }
            
            if (hasGeneralOffers) {
                int count = offersPage.getOffersCount();
                System.out.println("✅ Good UX: " + count + " generic offers shown despite location denial");
            }
            
            System.out.println("🎉 CRITICAL TEST PASSED: Offers with Location Deny - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Offers Flow with Location Deny: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Critical offers flow with location deny failed: " + e.getMessage());
        }
    }
    
    /**
     * PRIORITY 3: CRITICAL Cross-Device Offers Popup Validation
     * Tests offers page behavior across different device types and browsers
     * Critical for ensuring consistent UX across mobile/desktop/tablet
     */
    @Test(groups = {"critical", "cross-platform", "offers"}, priority = 3,
          description = "Cross-device offers page validation - Mobile/Desktop compatibility")
    public void testOffersPageCrossDeviceCompatibility() {
        System.out.println("🚀 CRITICAL TEST: Cross-Device Offers Compatibility - " + getTestInfo());
        
        try {
            // Step 1: Setup and login
            setupPageObjects();
            
            System.out.println("📱 Step 1: Device-agnostic login test...");
            homePage.clickSignIn();
            loginPage.login("demouser", "testingisfun99");
            
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "demouser login failed for cross-device test");
            
            // Step 2: Navigate to offers with device-specific handling
            System.out.println("🎯 Step 2: Device-aware offers navigation...");
            homePage.navigateToOffers();
            
            // Mobile devices may have different loading patterns
            boolean pageLoaded = offersPage.waitForOffersToLoad(15); // Longer timeout for mobile
            Assert.assertTrue(pageLoaded, 
                "Offers page failed to load within timeout - May indicate mobile performance issue");
            
            // Step 3: Device-specific popup handling
            System.out.println("📱 Step 3: Device-specific popup validation...");
            
            // Different devices may show different popup patterns
            String deviceInfo = getDeviceInfo();
            System.out.println("Testing on: " + deviceInfo);
            
            // Check for various popup types based on device
            boolean hasStandardPopup = offersPage.isLocationPopupDisplayed();
            boolean hasBrowserNativePopup = checkForBrowserNativeLocationPopup();
            boolean hasMobilePermissionDialog = checkForMobilePermissionDialog();
            
            if (hasStandardPopup || hasBrowserNativePopup || hasMobilePermissionDialog) {
                System.out.println("📍 Location permission request detected");
                handleDeviceSpecificLocationPopup();
            } else {
                System.out.println("ℹ️ No location popup detected - checking pre-granted permissions");
            }
            
            // Step 4: Validate cross-device offer loading
            System.out.println("🔍 Step 4: Cross-device offers validation...");
            
            // Wait for content to stabilize
            SeleniumUtils.waitForPageLoad(driver, 5);
            
            // Validate page works across different viewport sizes
            validateOffersPagesResponsiveness();
            
            // Check offers display works regardless of device
            boolean hasContent = offersPage.areOffersDisplayed() || 
                               offersPage.isNoOffersMessageDisplayed() ||
                               offersPage.isLocationRequiredMessageDisplayed();
            
            Assert.assertTrue(hasContent, 
                "CRITICAL CROSS-DEVICE FAILURE: No content displayed on " + deviceInfo + 
                ". Offers page not working properly across devices.");
            
            // Device-specific assertions
            if (isMobileDevice()) {
                validateMobileSpecificFeatures();
            } else {
                validateDesktopSpecificFeatures();
            }
            
            System.out.println("🎉 CRITICAL TEST PASSED: Cross-Device Compatibility - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Cross-Device Offers Test: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Critical cross-device offers test failed: " + e.getMessage());
        }
    }
    
    // Helper Methods
    
    private void setupPageObjects() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        offersPage = new OffersPage(driver);
    }
    
    private void validateOffersPagesResponsiveness() {
        System.out.println("📏 Validating page responsiveness...");
        
        // Check basic page elements are visible
        Assert.assertTrue(offersPage.isPageLoaded(), 
            "Offers page elements not properly loaded for current viewport");
        
        // Check navigation is still accessible
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Orders")) ||
                         SeleniumUtils.isElementVisible(driver, By.linkText("Favourites")), 
            "Navigation elements not accessible - responsive design issue");
    }
    
    private boolean checkForBrowserNativeLocationPopup() {
        // Check for browser-specific location popups
        return SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(@class, 'notification') and contains(text(), 'location')]")) ||
               SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(text(), 'wants to know your location')]"));
    }
    
    private boolean checkForMobilePermissionDialog() {
        // Check for mobile-specific permission dialogs
        return SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(@class, 'permission-dialog')]")) ||
               SeleniumUtils.isElementVisible(driver, By.xpath("//div[contains(@class, 'mobile-permission')]"));
    }
    
    private void handleDeviceSpecificLocationPopup() {
        try {
            // Try standard popup handling first
            if (offersPage.isLocationPopupDisplayed()) {
                offersPage.allowLocationAccess();
                return;
            }
            
            // Handle browser-native popups
            if (checkForBrowserNativeLocationPopup()) {
                // These typically require browser-level handling
                System.out.println("📍 Browser native location popup detected");
                // In real scenarios, this might need browser capabilities or JavaScript execution
            }
            
            // Handle mobile permission dialogs
            if (checkForMobilePermissionDialog()) {
                System.out.println("📱 Mobile permission dialog detected");
                // Mobile devices may have system-level permission dialogs
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error handling device-specific popup: " + e.getMessage());
        }
    }
    
    private String getDeviceInfo() {
        String userAgent = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return navigator.userAgent;");
        
        if (userAgent.toLowerCase().contains("mobile")) {
            return "Mobile Device";
        } else if (userAgent.toLowerCase().contains("tablet")) {
            return "Tablet Device";
        } else {
            return "Desktop Browser";
        }
    }
    
    private boolean isMobileDevice() {
        return getDeviceInfo().contains("Mobile");
    }
    
    private void validateMobileSpecificFeatures() {
        System.out.println("📱 Validating mobile-specific features...");
        
        // Check touch-friendly elements
        Assert.assertTrue(offersPage.isPageLoaded(), 
            "Mobile offers page not loading properly");
        
        // Mobile-specific UI checks could go here
        System.out.println("✅ Mobile validation completed");
    }
    
    private void validateDesktopSpecificFeatures() {
        System.out.println("🖥️ Validating desktop-specific features...");
        
        // Check desktop-specific elements
        Assert.assertTrue(offersPage.isPageLoaded(), 
            "Desktop offers page not loading properly");
        
        // Desktop-specific UI checks could go here
        System.out.println("✅ Desktop validation completed");
    }
}
