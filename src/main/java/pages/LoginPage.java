package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.SeleniumUtils;

/**
 * Page Object for Login functionality
 */
public class LoginPage extends BasePage {
    
    // Locators for testathon.live site - React Select components
    @FindBy(id = "login-btn")
    private WebElement loginButton;
    
    @FindBy(className = "api-error")
    private WebElement errorMessage;
    
    @FindBy(linkText = "Logout")
    private WebElement logoutButton;
    
    // Locators for testathon.live site using By selectors
    private final By usernameDropdownLocator = By.xpath("//div[contains(text(), 'Select Username')]/ancestor::div[contains(@class, 'css-')]");
    private final By passwordDropdownLocator = By.xpath("//div[contains(text(), 'Select Password')]/ancestor::div[contains(@class, 'css-')]");
    private final By loginButtonLocator = By.id("login-btn");
    private final By errorMessageLocator = By.className("api-error");
    private final By loggedInUserLocator = By.xpath("//div[text()='demouser' or text()='image_not_loading_user' or text()='existing_orders_user' or text()='fav_user' or text()='locked_user']");
    private final By logoutButtonLocator = By.linkText("Logout");
    
    // Username and password option locators
    private final By demouserOptionLocator = By.xpath("//div[text()='demouser']");
    private final By imageNotLoadingUserOptionLocator = By.xpath("//div[text()='image_not_loading_user']");
    private final By existingOrdersUserOptionLocator = By.xpath("//div[text()='existing_orders_user']");
    private final By favUserOptionLocator = By.xpath("//div[text()='fav_user']");
    private final By lockedUserOptionLocator = By.xpath("//div[text()='locked_user']");
    private final By passwordOptionLocator = By.xpath("//div[text()='testingisfun99']");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        return SeleniumUtils.isElementVisible(driver, usernameDropdownLocator) &&
               SeleniumUtils.isElementVisible(driver, passwordDropdownLocator) &&
               SeleniumUtils.isElementVisible(driver, loginButtonLocator);
    }
    
    /**
     * Click username dropdown to open options
     */
    public void clickUsernameDropdown() {
        SeleniumUtils.safeClick(driver, usernameDropdownLocator);
    }
    
    /**
     * Click password dropdown to open options  
     */
    public void clickPasswordDropdown() {
        SeleniumUtils.safeClick(driver, passwordDropdownLocator);
    }
    
    /**
     * Select username from dropdown
     */
    public void selectUsername(String username) {
        clickUsernameDropdown();
        SeleniumUtils.waitForElementVisible(driver, By.xpath("//div[text()='" + username + "']"));
        SeleniumUtils.safeClick(driver, By.xpath("//div[text()='" + username + "']"));
    }
    
    /**
     * Select password from dropdown
     */
    public void selectPassword() {
        clickPasswordDropdown();
        SeleniumUtils.waitForElementVisible(driver, passwordOptionLocator);
        SeleniumUtils.safeClick(driver, passwordOptionLocator);
    }
    
    /**
     * Enter username (now selects from dropdown)
     */
    public void enterUsername(String username) {
        selectUsername(username);
    }
    
    /**
     * Enter password (now selects from dropdown)  
     */
    public void enterPassword(String password) {
        selectPassword();
    }
    
    /**
     * Click login button
     */
    public void clickLogin() {
        SeleniumUtils.safeClick(driver, loginButtonLocator);
    }
    
    /**
     * Perform complete login
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        return SeleniumUtils.isElementVisible(driver, loggedInUserLocator) &&
               SeleniumUtils.isElementVisible(driver, logoutButtonLocator);
    }
    
    /**
     * Get logged in username
     */
    public String getLoggedInUsername() {
        if (isLoginSuccessful()) {
            return SeleniumUtils.getTextSafely(driver, loggedInUserLocator);
        }
        return null;
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return SeleniumUtils.isElementVisible(driver, errorMessageLocator);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return SeleniumUtils.getTextSafely(driver, errorMessageLocator);
        }
        return null;
    }
    
    /**
     * Logout if user is logged in
     */
    public void logout() {
        if (SeleniumUtils.isElementVisible(driver, logoutButtonLocator)) {
            SeleniumUtils.safeClick(driver, logoutButtonLocator);
        }
    }
    
    /**
     * Clear login form - not applicable for dropdown selects
     */
    public void clearLoginForm() {
        // For dropdown selects, we would need to reset to default state
        // This is typically handled by refreshing the page or navigating back to login
        System.out.println("Note: Clear form not applicable for dropdown selects. Use page refresh if needed.");
    }
    
    /**
     * Check if logout button is visible
     */
    public boolean isLogoutButtonVisible() {
        return SeleniumUtils.isElementVisible(driver, logoutButtonLocator);
    }
    
    /**
     * Get page title
     */
    public String getLoginPageTitle() {
        return getPageTitle();
    }
    
    /**
     * Get available username options
     */
    public java.util.List<String> getAvailableUsernames() {
        clickUsernameDropdown();
        java.util.List<String> usernames = new java.util.ArrayList<>();
        String[] expectedUsers = {"demouser", "image_not_loading_user", "existing_orders_user", "fav_user", "locked_user"};
        
        for (String user : expectedUsers) {
            if (SeleniumUtils.isElementVisible(driver, By.xpath("//div[text()='" + user + "']"))) {
                usernames.add(user);
            }
        }
        return usernames;
    }
    
    /**
     * Check if username dropdown is open
     */
    public boolean isUsernameDropdownOpen() {
        return SeleniumUtils.isElementVisible(driver, By.xpath("//div[text()='demouser']"));
    }
    
    /**
     * Check if password dropdown is open
     */
    public boolean isPasswordDropdownOpen() {
        return SeleniumUtils.isElementVisible(driver, By.xpath("//div[text()='testingisfun99']"));
    }
    
    /**
     * Get login button text
     */
    public String getLoginButtonText() {
        return SeleniumUtils.getTextSafely(driver, loginButtonLocator);
    }
    
    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        try {
            WebElement button = SeleniumUtils.waitForElementVisible(driver, loginButtonLocator);
            return button.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for login to complete
     */
    public void waitForLoginToComplete() {
        SeleniumUtils.waitForElementVisible(driver, loggedInUserLocator);
    }
}
