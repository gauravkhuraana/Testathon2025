package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

/**
 * Simple local test to verify framework works without BrowserStack SDK interference
 */
public class SimpleLocalTest {
    
    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    
    @BeforeMethod
    public void setUp() {
        System.out.println("=== Simple Local Test Setup ===");
        
        // Create Chrome driver directly
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        
        // Navigate to testathon.live
        driver.get("https://testathon.live/");
        
        // Initialize page objects
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        
        System.out.println("✓ Setup complete - navigated to: " + driver.getCurrentUrl());
    }
    
    @Test(groups = "smoke")
    public void testSimpleLogin() {
        System.out.println("=== Starting Simple Login Test ===");
        
        try {
            // Wait for page to load and click Sign In
            Thread.sleep(2000);
            System.out.println("Page title: " + driver.getTitle());
            
            // Click Sign In from home page
            homePage.clickSignIn();
            System.out.println("✓ Clicked Sign In");
            
            // Wait for login page to load
            Thread.sleep(2000);
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Perform login
            loginPage.selectUsername("demouser");
            System.out.println("✓ Selected username");
            
            loginPage.selectPassword();
            System.out.println("✓ Selected password");
            
            loginPage.clickLogin();
            System.out.println("✓ Clicked Login button");
            
            // Wait for login to complete
            Thread.sleep(3000);
            System.out.println("Final URL: " + driver.getCurrentUrl());
            System.out.println("Final title: " + driver.getTitle());
            
            // Verify login success
            boolean isLoggedIn = driver.getCurrentUrl().contains("inventory") || 
                               driver.getTitle().toLowerCase().contains("products") ||
                               homePage.isHomePageDisplayed();
            
            if (isLoggedIn) {
                System.out.println("✅ Login test PASSED - User successfully logged in");
            } else {
                System.out.println("❌ Login test FAILED - User not logged in");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("=== Closing browser ===");
            driver.quit();
        }
    }
}
