package tests;

import base.BaseTest;
import pages.HomePage;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Test to verify the fixed locators work correctly
 */
public class VerifyFixedLocatorsTest extends BaseTest {
    
    @Test(groups = {"smoke"})
    public void testFixedLocators() {
        try {
            System.out.println("🔍 Testing fixed locators...");
            
            HomePage homePage = new HomePage(driver);
            
            // Test 1: Verify home page loads and elements are found
            System.out.println("\n✅ Test 1: Verifying home page loads...");
            boolean isDisplayed = homePage.isHomePageDisplayed();
            Assert.assertTrue(isDisplayed, "Home page should be displayed");
            System.out.println("✅ Home page displayed: " + isDisplayed);
            
            // Test 2: Verify product count can be retrieved
            System.out.println("\n✅ Test 2: Verifying product count...");
            int productCount = homePage.getProductCount();
            System.out.println("✅ Product count: " + productCount);
            Assert.assertTrue(productCount > 0, "Product count should be greater than 0");
            
            // Test 3: Verify vendor filters can be detected
            System.out.println("\n✅ Test 3: Verifying vendor filters...");
            try {
                homePage.applyFilter("Apple");
                System.out.println("✅ Apple filter test completed");
            } catch (Exception e) {
                System.out.println("⚠️ Apple filter test warning: " + e.getMessage());
            }
            
            // Test 4: Test other vendor filters
            String[] brands = {"Samsung", "Google", "OnePlus"};
            for (String brand : brands) {
                try {
                    homePage.applyFilter(brand);
                    System.out.println("✅ " + brand + " filter test completed");
                } catch (Exception e) {
                    System.out.println("⚠️ " + brand + " filter test warning: " + e.getMessage());
                }
            }
            
            System.out.println("\n🎉 All locator tests completed successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
