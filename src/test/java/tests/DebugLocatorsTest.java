package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;

/**
 * Debug test to inspect page elements and find correct locators
 */
public class DebugLocatorsTest extends BaseTest {
    
    @Test(groups = {"debug"})
    public void inspectPageElements() {
        try {
            System.out.println("🔍 Starting page inspection...");
            
            // Wait for page to load
            Thread.sleep(5000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentUrl);
            
            // Check for product count elements
            System.out.println("\n🔍 Looking for product count elements...");
            inspectProductCountElements();
            
            // Check for vendor filter elements
            System.out.println("\n🔍 Looking for vendor filter elements...");
            inspectVendorElements();
            
            // Get page source for manual inspection
            System.out.println("\n📄 Page title: " + driver.getTitle());
            
        } catch (Exception e) {
            System.out.println("❌ Error during inspection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void inspectProductCountElements() {
        try {
            // Try different variations of product count locators
            String[] productCountXPaths = {
                "//div[contains(text(), 'Product(s) found')]",
                "//div[contains(text(), 'Product')]",
                "//div[contains(text(), 'found')]",
                "//span[contains(text(), 'Product')]",
                "//p[contains(text(), 'Product')]",
                "//*[contains(text(), 'Product')]",
                "//*[contains(text(), 'found')]"
            };
            
            for (String xpath : productCountXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    if (!elements.isEmpty()) {
                        System.out.println("✅ Found " + elements.size() + " elements with xpath: " + xpath);
                        for (int i = 0; i < Math.min(3, elements.size()); i++) {
                            WebElement elem = elements.get(i);
                            System.out.println("   - Text: '" + elem.getText() + "'");
                            System.out.println("   - Tag: " + elem.getTagName());
                            System.out.println("   - Class: " + elem.getAttribute("class"));
                        }
                    } else {
                        System.out.println("❌ No elements found with xpath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error with xpath " + xpath + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error inspecting product count elements: " + e.getMessage());
        }
    }
    
    private void inspectVendorElements() {
        try {
            // Try different variations of vendor filter locators
            String[] vendorXPaths = {
                "//div[text()='Vendors:']",
                "//div[contains(text(), 'Vendor')]",
                "//span[text()='Vendors:']",
                "//label[contains(text(), 'Vendor')]",
                "//*[contains(text(), 'Vendor')]",
                "//h3[contains(text(), 'Vendor')]",
                "//h4[contains(text(), 'Vendor')]"
            };
            
            for (String xpath : vendorXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    if (!elements.isEmpty()) {
                        System.out.println("✅ Found " + elements.size() + " elements with xpath: " + xpath);
                        for (int i = 0; i < Math.min(3, elements.size()); i++) {
                            WebElement elem = elements.get(i);
                            System.out.println("   - Text: '" + elem.getText() + "'");
                            System.out.println("   - Tag: " + elem.getTagName());
                            System.out.println("   - Class: " + elem.getAttribute("class"));
                        }
                    } else {
                        System.out.println("❌ No elements found with xpath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error with xpath " + xpath + ": " + e.getMessage());
                }
            }
            
            // Also check for filter-related elements
            System.out.println("\n🔍 Looking for filter-related elements...");
            String[] filterXPaths = {
                "//div[contains(@class, 'filter')]",
                "//div[contains(@class, 'sidebar')]",
                "//input[@type='checkbox']",
                "//label[contains(text(), 'Apple')]",
                "//label[contains(text(), 'Samsung')]"
            };
            
            for (String xpath : filterXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    System.out.println("Filter elements found with " + xpath + ": " + elements.size());
                } catch (Exception e) {
                    System.out.println("⚠️ Error with filter xpath " + xpath + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error inspecting vendor elements: " + e.getMessage());
        }
    }
}
