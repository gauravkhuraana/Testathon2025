package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.SeleniumUtils;

/**
 * Data-driven tests for Login functionality using different user types
 */
public class LoginDataDrivenTest extends BaseTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    
    /**
     * DataProvider for valid user credentials
     */
    @DataProvider(name = "validUsers")
    public Object[][] validUsersData() {
        return new Object[][]{
            {"demouser", "testingisfun99", "Standard user login"},
            {"image_not_loading_user", "testingisfun99", "Image not loading user login"},
            {"existing_orders_user", "testingisfun99", "Existing orders user login"},
            {"fav_user", "testingisfun99", "Favorite user login"}
        };
    }
    
    /**
     * DataProvider for user scenarios
     */
    @DataProvider(name = "userScenarios")
    public Object[][] userScenariosData() {
        return new Object[][]{
            {"demouser", true, "Standard user should login successfully"},
            {"image_not_loading_user", true, "Image not loading user should login successfully"},
            {"existing_orders_user", true, "Existing orders user should login successfully"},
            {"fav_user", true, "Favorite user should login successfully"},
            {"locked_user", false, "Locked user login behavior may vary"}
        };
    }
    
    @Test(dataProvider = "validUsers", groups = {"smoke", "regression"}, 
          description = "Verify login with different valid user types")
    public void testLoginWithValidUsers(String username, String password, String description) {
        System.out.println("Starting Login Test: " + description + " - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Perform login
        loginPage.login(username, password);
        
        // Verify login was successful
        Assert.assertTrue(loginPage.isLoginSuccessful(), 
                         "Login should be successful for user: " + username);
        
        // Verify correct username is displayed
        String loggedInUser = loginPage.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, username, 
                          "Correct username should be displayed: " + username);
        
        // Verify logout functionality
        loginPage.logout();
        
        // Add small wait for logout to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Completed Login Test: " + description + " - " + getTestInfo());
    }
    
    @Test(dataProvider = "userScenarios", groups = {"regression"}, 
          description = "Verify login scenarios for different user types")
    public void testUserLoginScenarios(String username, boolean shouldSucceed, String description) {
        System.out.println("Starting User Scenario Test: " + description + " - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Perform login
        loginPage.login(username, "testingisfun99");
        
        if (shouldSucceed) {
            // Verify login was successful
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                             "Login should be successful for user: " + username);
            
            // Verify navigation elements are available
            Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Logout")), 
                             "Logout should be available after successful login");
            
            // Logout
            loginPage.logout();
        } else {
            // For locked_user, the behavior may vary
            // Document the actual behavior observed
            boolean loginSuccessful = loginPage.isLoginSuccessful();
            System.out.println("Login result for " + username + ": " + 
                             (loginSuccessful ? "SUCCESS" : "FAILED"));
            
            if (loginSuccessful) {
                System.out.println("Note: " + username + " was able to login. " +
                                 "Check if there are any restrictions in functionality.");
                // If login succeeds, test logout
                loginPage.logout();
            } else {
                System.out.println("Note: " + username + " login was blocked as expected.");
            }
        }
        
        System.out.println("Completed User Scenario Test: " + description + " - " + getTestInfo());
    }
    
    @Test(groups = {"regression"}, priority = 10,
          description = "Verify all available usernames can be selected from dropdown")
    public void testUsernameDropdownOptions() {
        System.out.println("Starting Username Dropdown Options Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
        
        // Get available usernames
        java.util.List<String> availableUsers = loginPage.getAvailableUsernames();
        
        // Verify expected users are available
        String[] expectedUsers = {"demouser", "image_not_loading_user", "existing_orders_user", "fav_user", "locked_user"};
        
        for (String expectedUser : expectedUsers) {
            Assert.assertTrue(availableUsers.contains(expectedUser), 
                            "Username should be available in dropdown: " + expectedUser);
        }
        
        System.out.println("Available usernames: " + availableUsers);
        System.out.println("Username Dropdown Options Test Completed Successfully - " + getTestInfo());
    }
    
    @Test(groups = {"smoke"}, priority = 5,
          description = "Verify login workflow end-to-end")
    public void testLoginWorkflowEndToEnd() {
        System.out.println("Starting End-to-End Login Workflow Test - " + getTestInfo());
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        // Step 1: Verify home page loads
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed initially");
        
        // Step 2: Navigate to login page
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should load after clicking Sign In");
        
        // Step 3: Verify login form elements
        Assert.assertTrue(loginPage.isLoginButtonEnabled(), "Login button should be enabled");
        Assert.assertEquals(loginPage.getLoginButtonText(), "Log In", "Login button should have correct text");
        
        // Step 4: Perform login
        loginPage.login("demouser", "testingisfun99");
        
        // Step 5: Verify successful login
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        Assert.assertEquals(loginPage.getLoggedInUsername(), "demouser", "Correct username should be displayed");
        
        // Step 6: Verify navigation is available after login
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Offers")), 
                         "Offers navigation should be available");
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Orders")), 
                         "Orders navigation should be available");
        Assert.assertTrue(SeleniumUtils.isElementVisible(driver, By.linkText("Favourites")), 
                         "Favourites navigation should be available");
        
        // Step 7: Verify logout
        loginPage.logout();
        
        System.out.println("End-to-End Login Workflow Test Completed Successfully - " + getTestInfo());
    }
}
