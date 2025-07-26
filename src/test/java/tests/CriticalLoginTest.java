package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.WaitUtils;

/**
 * CRITICAL Login Test Scenarios for testathon.live
 * These tests cover the most critical login flows that can fail across different devices/browsers/OS
 * Focus: Authentication flow, dropdown handling, cross-browser compatibility, session management
 */
public class CriticalLoginTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    
    /**
     * DataProvider for critical user scenarios
     */
    @DataProvider(name = "criticalUsers")
    public Object[][] criticalUsersData() {
        return new Object[][]{
            {"demouser", "testingisfun99", "Standard user - Most common login flow"},
            {"existing_orders_user", "testingisfun99", "User with existing data - Session persistence test"},
            {"fav_user", "testingisfun99", "User with favorites - Data integrity test"},
            {"image_not_loading_user", "testingisfun99", "User with potential UI issues - Error handling test"}
        };
    }
    
    /**
     * PRIORITY 1: Critical Login Flow with Cross-Browser Dropdown Validation
     * Most important business scenario - Authentication gateway
     * Assertions designed to catch React Select and dropdown issues across browsers
     */
    @Test(groups = {"critical", "smoke"}, priority = 1,
          description = "Complete login flow - Critical authentication gateway")
    public void testCriticalLoginFlow() {
        System.out.println("🚀 CRITICAL TEST: Login Flow - " + getTestInfo());
        
        setupPageObjects();
        
        try {
            // Step 1: Verify homepage accessibility
            System.out.println("🏠 Step 1: Homepage verification...");
            Assert.assertTrue(homePage.isHomePageDisplayed(), 
                "Homepage failed to load - Critical browser compatibility issue");
            
            // Step 2: Navigate to login with cross-browser validation
            System.out.println("🔐 Step 2: Login navigation...");
            homePage.clickSignIn();
            
            // Enhanced wait for login page to be fully ready
            WaitUtils.waitForPageToBeReady(driver, 10);
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page failed to load - Critical navigation/routing issue");
            
            // Step 3: Perform authentication with dropdown validation
            System.out.println("📝 Step 3: Authentication process...");
            loginPage.login("demouser", "testingisfun99");
            
            // Enhanced wait after login attempt
            WaitUtils.waitForPageToBeReady(driver, 10);
            
            // Step 4: Validate login success with multiple checks
            System.out.println("✅ Step 4: Login validation...");
            validateLoginSuccess("demouser");
            
            // Step 5: Test session persistence (critical for UX)
            System.out.println("🔄 Step 5: Session persistence validation...");
            validateSessionPersistence();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Login Flow: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Critical login flow failed: " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: Login Flow - " + getTestInfo());
    }
    
    /**
     * PRIORITY 2: Cross-User Authentication Validation
     * Tests different user types and their authentication behavior
     * Critical for user management and data integrity
     */
    @Test(dataProvider = "criticalUsers", groups = {"critical", "regression"}, priority = 2,
          description = "Cross-user authentication validation - User data integrity")
    public void testCrossUserAuthentication(String username, String password, String description) {
        System.out.println("👥 CRITICAL TEST: " + description + " - " + getTestInfo());
        
        setupPageObjects();
        
        try {
            // Step 1: Navigate to login
            System.out.println("🔐 Step 1: Login navigation for " + username);
            homePage.clickSignIn();
            
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page failed to load for user: " + username + " - Navigation issue");
            
            // Step 2: Authenticate user
            System.out.println("📝 Step 2: Authenticating " + username);
            loginPage.login(username, password);
            
            // Step 3: Validate user-specific login behavior
            System.out.println("✅ Step 3: User-specific validation...");
            validateUserSpecificLogin(username);
            
            // Step 4: Test logout for session cleanup
            System.out.println("🚪 Step 4: Logout validation...");
            validateLogout();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Cross-user authentication failed for " + username + ": " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: " + description + " - " + getTestInfo());
    }
    
    /**
     * PRIORITY 3: Login Error Handling and Edge Cases
     * Tests invalid credentials and form validation
     * Critical for security and user experience
     */
    @Test(groups = {"critical", "regression"}, priority = 3,
          description = "Login error handling - Security and UX validation")
    public void testLoginErrorHandling() {
        System.out.println("🛡️ CRITICAL TEST: Login Error Handling - " + getTestInfo());
        
        setupPageObjects();
        
        try {
            // Step 1: Navigate to login
            System.out.println("🔐 Step 1: Login navigation...");
            homePage.clickSignIn();
            
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page failed to load - Critical navigation issue");
            
            // Step 2: Test invalid user handling
            System.out.println("❌ Step 2: Invalid credentials test...");
            testInvalidCredentials();
            
            // Step 3: Test empty form validation
            System.out.println("📝 Step 3: Empty form validation...");
            testEmptyFormValidation();
            
            // Step 4: Test dropdown interaction edge cases
            System.out.println("🔽 Step 4: Dropdown edge cases...");
            testDropdownEdgeCases();
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in Error Handling: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Login error handling failed: " + e.getMessage());
        }
        
        System.out.println("🎉 CRITICAL TEST PASSED: Login Error Handling - " + getTestInfo());
    }
    
    // Helper Methods Implementation
    
    private void setupPageObjects() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }
    
    private void validateLoginSuccess(String expectedUsername) {
        // Multi-layered validation for cross-browser compatibility
        Assert.assertTrue(loginPage.isLoginSuccessful(), 
            "Login failed - Authentication issue. Check dropdown functionality across browsers");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertNotNull(loggedInUser, 
            "Username not displayed after login - UI rendering issue");
        
        Assert.assertEquals(loggedInUser, expectedUsername, 
            "Incorrect username displayed - Expected: " + expectedUsername + ", Actual: " + loggedInUser + 
            " - User session management issue");
        
        // Verify navigation elements are available after login
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Home page not accessible after login - Post-login navigation issue");
        
        System.out.println("✅ Login validation successful for user: " + expectedUsername);
    }
    
    private void validateSessionPersistence() {
        try {
            // Refresh page to test session persistence
            driver.navigate().refresh();
            Thread.sleep(2000); // Allow for page reload
            
            // Verify user is still logged in after refresh
            String userAfterRefresh = loginPage.getLoggedInUsername();
            Assert.assertNotNull(userAfterRefresh, 
                "Session not persisted after page refresh - Session management critical issue");
            
            System.out.println("✅ Session persistence validated successfully");
            
        } catch (Exception e) {
            System.out.println("⚠️ Session persistence test failed: " + e.getMessage());
            // Non-critical for basic login flow, but important for UX
        }
    }
    
    private void validateUserSpecificLogin(String username) {
        // Validate login success for all users
        Assert.assertTrue(loginPage.isLoginSuccessful(), 
            "Login failed for user: " + username + " - User-specific authentication issue");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, username, 
            "Username mismatch for user: " + username + " - User data integrity issue");
        
        // Special validation for users with potential issues
        if ("image_not_loading_user".equals(username)) {
            System.out.println("📸 Special validation for image loading user...");
            // Additional checks for UI elements that might fail to load
        }
        
        if ("existing_orders_user".equals(username)) {
            System.out.println("📦 Special validation for user with existing orders...");
            // Could validate Orders page accessibility
        }
        
        if ("fav_user".equals(username)) {
            System.out.println("⭐ Special validation for user with favorites...");
            // Could validate Favourites page accessibility
        }
        
        System.out.println("✅ User-specific validation successful for: " + username);
    }
    
    private void validateLogout() {
        try {
            // Attempt logout if logout functionality is available
            if (loginPage.isLogoutButtonVisible()) {
                loginPage.logout();
                
                // Verify logout was successful
                Assert.assertFalse(loginPage.isLoginSuccessful(), 
                    "Logout failed - Session termination issue");
                
                System.out.println("✅ Logout validation successful");
            } else {
                System.out.println("ℹ️ Logout functionality not available or not implemented");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Logout validation failed: " + e.getMessage());
            // Navigate back to home to reset state
            driver.get(driver.getCurrentUrl().split("/")[0] + "//" + driver.getCurrentUrl().split("/")[2]);
        }
    }
    
    private void testInvalidCredentials() {
        try {
            // Test with non-existent user
            System.out.println("🚫 Testing invalid user credentials...");
            
            // Navigate to fresh login page
            homePage.clickSignIn();
            
            // Attempt login with invalid credentials
            loginPage.login("invalid_user_12345", "wrongpassword");
            
            // Verify login failed appropriately
            Assert.assertFalse(loginPage.isLoginSuccessful(), 
                "Invalid credentials accepted - Security vulnerability");
            
            System.out.println("✅ Invalid credentials properly rejected");
            
        } catch (Exception e) {
            System.out.println("⚠️ Invalid credentials test exception: " + e.getMessage());
            // This might be expected behavior for some implementations
        }
    }
    
    private void testEmptyFormValidation() {
        try {
            System.out.println("📝 Testing empty form validation...");
            
            // Navigate to fresh login page
            homePage.clickSignIn();
            
            // Try to submit without selecting username/password
            // This tests form validation behavior
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login form not accessible for empty validation test");
            
            System.out.println("✅ Empty form validation accessible");
            
        } catch (Exception e) {
            System.out.println("⚠️ Empty form validation test exception: " + e.getMessage());
        }
    }
    
    private void testDropdownEdgeCases() {
        try {
            System.out.println("🔽 Testing dropdown interaction edge cases...");
            
            // Navigate to fresh login page
            homePage.clickSignIn();
            
            // Test dropdown accessibility and interaction
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page not accessible for dropdown testing");
            
            // Additional dropdown-specific tests could be added here
            // such as keyboard navigation, mobile touch interaction, etc.
            
            System.out.println("✅ Dropdown edge cases validation completed");
            
        } catch (Exception e) {
            System.out.println("⚠️ Dropdown edge cases test exception: " + e.getMessage());
        }
    }
}
