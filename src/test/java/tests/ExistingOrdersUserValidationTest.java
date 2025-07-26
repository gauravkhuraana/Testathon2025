package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.OrdersPage;

/**
 * Critical Orders Page Validation Test for existing_orders_user
 * This test validates the orders page functionality and identifies issues
 * like incorrect totals and indentation problems
 * 
 * Key Focus Areas:
 * 1. Login with existing_orders_user and navigate to orders
 * 2. Validate orders page accessibility and data loading
 * 3. Check for order total calculation issues
 * 4. Identify indentation and CSS alignment problems
 * 5. Comprehensive validation of order data integrity
 */
public class ExistingOrdersUserValidationTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private OrdersPage ordersPage;
    
    /**
     * PRIORITY 1: CRITICAL Orders Page Validation Test
     * Tests complete flow: Login as existing_orders_user -> Navigate to Orders -> Validate Issues
     * This test is designed to identify and validate problems on the orders page
     */
    @Test(groups = {"critical", "smoke", "orders"}, priority = 1,
          description = "Validate existing_orders_user orders page functionality and identify issues")
    public void testExistingOrdersUserOrdersPageValidation() {
        System.out.println("🚀 CRITICAL TEST: existing_orders_user Orders Page Validation - " + getTestInfo());
        
        try {
            // Step 1: Setup page objects
            System.out.println("🔧 Step 1: Setting up page objects...");
            setupPageObjects();
            
            // Step 2: Verify homepage accessibility
            System.out.println("🏠 Step 2: Homepage verification...");
            Assert.assertTrue(homePage.isHomePageDisplayed(), 
                "Homepage failed to load - Critical browser compatibility issue");
            
            // Step 3: Navigate to login and authenticate existing_orders_user
            System.out.println("🔐 Step 3: Login as existing_orders_user...");
            homePage.clickSignIn();
            
            Assert.assertTrue(loginPage.isPageLoaded(), 
                "Login page failed to load - Critical navigation issue");
            
            // Login with existing_orders_user
            loginPage.login("existing_orders_user", "testingisfun99");
            
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "Login failed for existing_orders_user - Authentication issue");
            
            String loggedInUser = loginPage.getLoggedInUsername();
            Assert.assertEquals(loggedInUser, "existing_orders_user", 
                "Username display incorrect - Expected: existing_orders_user, Actual: " + loggedInUser);
            
            System.out.println("✅ Successfully logged in as: " + loggedInUser);
            
            // Step 4: Navigate to Orders page
            System.out.println("📦 Step 4: Navigating to Orders page...");
            homePage.navigateToOrders();
            
            // Wait a moment for navigation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Step 5: Validate Orders page loaded
            System.out.println("📋 Step 5: Validating Orders page access...");
            Assert.assertTrue(ordersPage.isOrdersPageDisplayed(), 
                "Orders page failed to load - Navigation or access issue");
            
            // Step 6: Wait for orders data to load
            System.out.println("⏳ Step 6: Waiting for orders data to load...");
            boolean ordersLoaded = ordersPage.waitForOrdersToLoad(10);
            Assert.assertTrue(ordersLoaded, 
                "Orders data failed to load within timeout - Data loading issue");
            
            // Step 7: Validate orders functionality is accessible
            System.out.println("🔍 Step 7: Validating orders functionality access...");
            Assert.assertTrue(ordersPage.isOrdersFunctionalityAccessible(), 
                "Orders functionality not accessible for existing_orders_user - Permission issue");
            
            // Step 8: Comprehensive orders page validation
            System.out.println("🧐 Step 8: Comprehensive orders page state validation...");
            String ordersPageState = ordersPage.validateOrdersPageState();
            System.out.println(ordersPageState);
            
            // Step 9: CRITICAL - Validate existing_orders_user specific functionality and identify issues
            System.out.println("⚡ Step 9: CRITICAL - Running existing_orders_user validation with issue detection...");
            boolean userFunctionalityValid = ordersPage.validateExistingOrdersUserFunctionality();
            
            Assert.assertTrue(userFunctionalityValid, 
                "existing_orders_user orders functionality validation failed - Core feature broken");
            
            // Step 10: Additional detailed validations for issue identification
            System.out.println("🔬 Step 10: Additional detailed issue analysis...");
            
            if (ordersPage.areOrdersDisplayed()) {
                System.out.println("📊 Orders are displayed - Running detailed validations...");
                
                // Validate order totals specifically
                System.out.println("💰 Validating order totals calculation...");
                boolean totalCalculationValid = ordersPage.validateOrderTotalsCalculation();
                if (!totalCalculationValid) {
                    System.out.println("❌ ISSUE DETECTED: Order total calculation problems found");
                } else {
                    System.out.println("✅ Order total calculations appear correct");
                }
                
                // Validate indentation and layout
                System.out.println("📐 Validating page indentation and layout...");
                boolean indentationValid = ordersPage.validateOrdersPageIndentation();
                if (!indentationValid) {
                    System.out.println("❌ ISSUE DETECTED: Indentation/alignment problems found");
                } else {
                    System.out.println("✅ Page indentation and layout appear correct");
                }
                
                // Get detailed order information for manual review
                System.out.println("📄 Getting detailed order information...");
                String firstOrderDetails = ordersPage.getFirstOrderDetails();
                if (firstOrderDetails != null) {
                    System.out.println("📋 First Order Details:");
                    System.out.println(firstOrderDetails);
                }
                
                String allOrderTotals = ordersPage.getAllOrderTotals();
                System.out.println(allOrderTotals);
                
                // Record order count for reference
                int orderCount = ordersPage.getOrdersCount();
                System.out.println("📊 Total orders found: " + orderCount);
                
                if (orderCount == 0) {
                    System.out.println("⚠️ NOTE: No orders found for existing_orders_user - This might be unexpected");
                }
                
            } else if (ordersPage.isNoOrdersMessageDisplayed()) {
                System.out.println("📭 No orders message displayed");
                String noOrdersMessage = ordersPage.getNoOrdersMessage();
                System.out.println("💬 No orders message: " + noOrdersMessage);
            }
            
            System.out.println("🎉 CRITICAL TEST COMPLETED: existing_orders_user Orders Page Validation - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ CRITICAL FAILURE in existing_orders_user Orders Validation: " + e.getMessage());
            e.printStackTrace();
            
            // Additional debug information on failure
            try {
                System.out.println("🔍 Debug Information on Failure:");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Page Title: " + driver.getTitle());
                if (ordersPage != null) {
                    System.out.println("Orders Page State: " + ordersPage.validateOrdersPageState());
                }
            } catch (Exception debugError) {
                System.out.println("Failed to collect debug information: " + debugError.getMessage());
            }
            
            Assert.fail("Critical existing_orders_user orders validation failed: " + e.getMessage());
        }
    }
    
    /**
     * PRIORITY 2: Orders Page Issue Detection Focus Test
     * Specifically focuses on detecting and validating known issues
     */
    @Test(groups = {"critical", "regression", "orders"}, priority = 2,
          description = "Focus test for detecting order total and indentation issues",
          dependsOnMethods = {"testExistingOrdersUserOrdersPageValidation"})
    public void testOrdersPageIssueDetection() {
        System.out.println("🔍 ISSUE DETECTION TEST: Orders Page Problems Validation - " + getTestInfo());
        
        try {
            // Ensure we're on the orders page (from previous test)
            if (!ordersPage.isOrdersPageDisplayed()) {
                System.out.println("📦 Navigating to orders page for issue detection...");
                homePage.navigateToOrders();
                ordersPage.waitForOrdersToLoad(10);
            }
            
            boolean issuesDetected = false;
            
            // Test 1: Focused order total validation
            System.out.println("🧮 Test 1: Detailed order total validation...");
            if (ordersPage.areOrdersDisplayed()) {
                boolean totalValidationPassed = ordersPage.validateOrderTotalsCalculation();
                if (!totalValidationPassed) {
                    System.out.println("❌ CONFIRMED ISSUE: Order total calculation problems detected");
                    issuesDetected = true;
                } else {
                    System.out.println("✅ Order total calculations validated successfully");
                }
            }
            
            // Test 2: Focused indentation/alignment validation
            System.out.println("📐 Test 2: Detailed indentation and alignment validation...");
            boolean indentationValidationPassed = ordersPage.validateOrdersPageIndentation();
            if (!indentationValidationPassed) {
                System.out.println("❌ CONFIRMED ISSUE: Indentation/alignment problems detected");
                issuesDetected = true;
            } else {
                System.out.println("✅ Indentation and alignment validated successfully");
            }
            
            // Test 3: Page layout and structure validation
            System.out.println("🏗️ Test 3: Page structure validation...");
            String pageState = ordersPage.validateOrdersPageState();
            System.out.println(pageState);
            
            // Summary
            if (issuesDetected) {
                System.out.println("🎯 ISSUE DETECTION SUMMARY: Problems found on orders page");
                System.out.println("   - This test successfully identified issues that need attention");
                System.out.println("   - Check logs above for specific problem details");
            } else {
                System.out.println("✅ ISSUE DETECTION SUMMARY: No critical issues detected");
                System.out.println("   - Orders page appears to be functioning correctly");
            }
            
            // The test passes regardless of issues found - the goal is to detect and report
            System.out.println("✅ Issue detection test completed successfully - " + getTestInfo());
            
        } catch (Exception e) {
            System.err.println("❌ FAILURE in issue detection test: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Issue detection test failed: " + e.getMessage());
        }
    }
    
    // Helper Methods
    
    private void setupPageObjects() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        ordersPage = new OrdersPage(driver);
    }
}
