package tests;

import base.BaseTest;
import config.ConfigManager;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.SeleniumUtils;

/**
 * Test class for Login functionality
 */
public class LoginTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    
    @Test(groups = {"smoke", "regression"}, priority = 1,
          description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        System.out.println("Starting Login Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Click Sign In button
        homePage.clickSignIn();
        
        // Verify login page is loaded
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Perform login with valid credentials
        loginPage.login("demouser", "testingisfun99");
        
        // Verify login was successful
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        
        // Verify username is displayed
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertNotNull(loggedInUser, "Logged in username should be displayed");
        Assert.assertEquals(loggedInUser, "demouser", "Correct username should be displayed");
        
        System.out.println("Login Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 2,
          description = "Verify login fails with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        System.out.println("Starting Invalid Login Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Attempt login with invalid credentials
        loginPage.login("invaliduser", "wrongpassword");
        
        // Verify login failed
        Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should fail with invalid credentials");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertNotNull(errorMessage, "Error message should not be null");
        Assert.assertTrue(errorMessage.contains("Invalid") || errorMessage.contains("Wrong"), 
                         "Error message should indicate invalid credentials");
        
        System.out.println("Invalid Login Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 3,
          description = "Verify login with empty credentials")
    public void testLoginWithEmptyCredentials() {
        System.out.println("Starting Empty Credentials Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Attempt login with empty credentials
        loginPage.login("", "");
        
        // Verify login failed
        Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should fail with empty credentials");
        
        System.out.println("Empty Credentials Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke"}, priority = 4, dependsOnMethods = {"testSuccessfulLogin"},
          description = "Verify user can logout successfully")
    public void testLogout() {
        System.out.println("Starting Logout Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Login first
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");
        
        // Logout
        loginPage.logout();
        
        // Verify logout was successful
        Assert.assertFalse(loginPage.isLogoutButtonVisible(), "Logout button should not be visible after logout");
        
        System.out.println("Logout Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 5,
          description = "Verify login page elements are present")
    public void testLoginPageElements() {
        System.out.println("Starting Login Page Elements Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        
        // Verify login page elements
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded with all elements");
        
        // Verify page title
        String pageTitle = loginPage.getLoginPageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        
        System.out.println("Login Page Elements Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 6,
          description = "Verify login with different valid user accounts")
    public void testLoginWithDifferentUsers() {
        System.out.println("Starting Multiple Users Login Test - " + getTestInfo());
        
        String[] validUsers = {"demouser", "image_not_loading_user", "existing_orders_user", "fav_user"};
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        for (String username : validUsers) {
            System.out.println("Testing login with user: " + username);
            
            // Navigate to login page
            driver.get(ConfigManager.getAppUrl() + "/signin");
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
            
            // Perform login
            loginPage.login(username, "testingisfun99");
            
            // Verify login was successful
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful for user: " + username);
            
            // Verify correct username is displayed
            String loggedInUser = loginPage.getLoggedInUsername();
            Assert.assertEquals(loggedInUser, username, "Correct username should be displayed for: " + username);
            
            // Logout
            loginPage.logout();
            
            System.out.println("Successfully logged in and out with user: " + username);
        }
        
        System.out.println("Multiple Users Login Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 7,
          description = "Verify login with locked user account")
    public void testLoginWithLockedUser() {
        System.out.println("Starting Locked User Login Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Attempt login with locked user
        loginPage.login("locked_user", "testingisfun99");
        
        // Verify appropriate behavior for locked user (could be error message or restricted access)
        // Note: The actual behavior depends on how the application handles locked users
        try {
            if (loginPage.isLoginSuccessful()) {
                System.out.println("Locked user was able to login - checking for restricted functionality");
                // If login succeeds, verify if there are any restrictions
                String loggedInUser = loginPage.getLoggedInUsername();
                Assert.assertEquals(loggedInUser, "locked_user", "Locked user should be displayed if login is allowed");
            } else {
                System.out.println("Locked user login was blocked as expected");
                Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked user");
            }
        } catch (Exception e) {
            System.out.println("Exception during locked user test: " + e.getMessage());
        }
        
        System.out.println("Locked User Login Test Completed - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 8,
          description = "Verify login form validation")
    public void testLoginFormValidation() {
        System.out.println("Starting Login Form Validation Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Test case 1: Try to submit form without selecting any credentials
        try {
            loginPage.clickLogin();
            // Verify that form validation prevents submission or shows error
            Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should not succeed without credentials");
        } catch (Exception e) {
            System.out.println("Form validation test - no selection: " + e.getMessage());
        }
        
        // Test case 2: Select only username, no password
        driver.navigate().refresh();
        SeleniumUtils.waitForElementVisible(driver, By.linkText("Sign In"));
        homePage.clickSignIn();
        
        try {
            loginPage.selectUsername("demouser");
            loginPage.clickLogin();
            Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should not succeed with only username");
        } catch (Exception e) {
            System.out.println("Form validation test - username only: " + e.getMessage());
        }
        
        System.out.println("Login Form Validation Test Completed - " + getTestInfo());
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 9,
          description = "Verify navigation after successful login")
    public void testNavigationAfterLogin() {
        System.out.println("Starting Navigation After Login Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page and login
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");
        
        // Verify URL contains signin parameter or redirects appropriately
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("signin=true") || currentUrl.contains("testathon.live"), 
                         "URL should indicate successful login");
        
        // Verify navigation menu is accessible after login
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Offers")), 
                         "Offers link should be visible after login");
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Orders")), 
                         "Orders link should be visible after login");
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Favourites")), 
                         "Favourites link should be visible after login");
        
        // Verify logout option is available
        Assert.assertTrue(loginPage.isLogoutButtonVisible(), "Logout button should be visible after login");
        
        System.out.println("Navigation After Login Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 10,
          description = "Verify session persistence after page refresh")
    public void testSessionPersistence() {
        System.out.println("Starting Session Persistence Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Login first
        homePage.clickSignIn();
        loginPage.login("demouser", "testingisfun99");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");
        
        // Refresh the page
        driver.navigate().refresh();
        
        // Verify user is still logged in after refresh
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User should remain logged in after page refresh");
        
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, "demouser", "Correct username should be displayed after refresh");
        
        System.out.println("Session Persistence Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 11,
          description = "Verify login dropdown functionality")
    public void testLoginDropdownFunctionality() {
        System.out.println("Starting Login Dropdown Functionality Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Test username dropdown
        loginPage.clickUsernameDropdown();
        
        // Verify all username options are available
        String[] expectedUsers = {"demouser", "image_not_loading_user", "existing_orders_user", "fav_user", "locked_user"};
        for (String user : expectedUsers) {
            Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.xpath("//div[text()='" + user + "']")), 
                            "User option should be visible: " + user);
        }
        
        // Test password dropdown
        loginPage.clickPasswordDropdown();
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.xpath("//div[text()='testingisfun99']")), 
                         "Password option should be visible");
        
        System.out.println("Login Dropdown Functionality Test Completed Successfully - " + getTestInfo());
    }
}
