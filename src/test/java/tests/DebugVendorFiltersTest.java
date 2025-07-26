package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Debug test to find vendor filter checkbox elements
 */
public class DebugVendorFiltersTest extends BaseTest {
    
    @Test(groups = {"debug"})
    public void findVendorFilterElements() {
        try {
            System.out.println("🔍 Starting vendor filter inspection...");
            
            // Wait for page to load completely
            Thread.sleep(5000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentUrl);
            
            // Look for vendor section
            System.out.println("\n🔍 Looking for vendor section...");
            findVendorSection();
            
            // Look for checkboxes
            System.out.println("\n🔍 Looking for checkbox elements...");
            findCheckboxElements();
            
            // Look for brand names
            System.out.println("\n🔍 Looking for brand names...");
            findBrandElements();
            
        } catch (Exception e) {
            System.out.println("❌ Error during inspection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void findVendorSection() {
        try {
            String[] vendorSectionXPaths = {
                "//h4[contains(text(), 'Vendor')]",
                "//h4[@class='title']",
                "//*[contains(text(), 'Vendor')]",
                "//div[contains(@class, 'filter')]//h4",
                "//aside//h4",
                "//sidebar//h4"
            };
            
            for (String xpath : vendorSectionXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    if (!elements.isEmpty()) {
                        System.out.println("✅ Found " + elements.size() + " vendor section elements with xpath: " + xpath);
                        for (WebElement elem : elements) {
                            System.out.println("   - Text: '" + elem.getText() + "'");
                            System.out.println("   - Tag: " + elem.getTagName());
                            System.out.println("   - Class: " + elem.getAttribute("class"));
                            
                            // Check next sibling elements for filters
                            try {
                                WebElement parent = elem.findElement(By.xpath("./.."));
                                List<WebElement> siblings = parent.findElements(By.xpath("./*"));
                                System.out.println("   - Parent has " + siblings.size() + " child elements");
                            } catch (Exception e) {
                                System.out.println("   - Could not get siblings: " + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error with xpath " + xpath + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error finding vendor section: " + e.getMessage());
        }
    }
    
    private void findCheckboxElements() {
        try {
            List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
            System.out.println("Found " + checkboxes.size() + " checkbox elements:");
            
            for (int i = 0; i < checkboxes.size(); i++) {
                WebElement checkbox = checkboxes.get(i);
                System.out.println("   Checkbox " + i + ":");
                System.out.println("     - ID: " + checkbox.getAttribute("id"));
                System.out.println("     - Name: " + checkbox.getAttribute("name"));
                System.out.println("     - Value: " + checkbox.getAttribute("value"));
                System.out.println("     - Class: " + checkbox.getAttribute("class"));
                
                try {
                    // Check for associated label
                    WebElement parent = checkbox.findElement(By.xpath("./.."));
                    System.out.println("     - Parent text: '" + parent.getText() + "'");
                    System.out.println("     - Parent class: " + parent.getAttribute("class"));
                } catch (Exception e) {
                    System.out.println("     - Could not get parent info");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error finding checkboxes: " + e.getMessage());
        }
    }
    
    private void findBrandElements() {
        try {
            String[] brands = {"Apple", "Samsung", "Google", "OnePlus"};
            
            for (String brand : brands) {
                System.out.println("Looking for brand: " + brand);
                
                String[] brandXPaths = {
                    "//div[text()='" + brand + "']",
                    "//span[text()='" + brand + "']",
                    "//label[text()='" + brand + "']",
                    "//*[text()='" + brand + "']",
                    "//div[contains(text(), '" + brand + "')]",
                    "//span[contains(text(), '" + brand + "')]",
                    "//label[contains(text(), '" + brand + "')]"
                };
                
                for (String xpath : brandXPaths) {
                    try {
                        List<WebElement> elements = driver.findElements(By.xpath(xpath));
                        if (!elements.isEmpty()) {
                            System.out.println("  ✅ Found " + elements.size() + " elements with xpath: " + xpath);
                            for (WebElement elem : elements) {
                                System.out.println("     - Text: '" + elem.getText() + "'");
                                System.out.println("     - Tag: " + elem.getTagName());
                                System.out.println("     - Class: " + elem.getAttribute("class"));
                                
                                // Look for associated checkbox
                                try {
                                    driver.findElement(By.xpath(".//input[@type='checkbox'] | ./preceding-sibling::input[@type='checkbox'] | ./following-sibling::input[@type='checkbox']"));
                                    System.out.println("     - Associated checkbox found!");
                                } catch (Exception e) {
                                    System.out.println("     - No associated checkbox found");
                                }
                            }
                            break; // Found elements for this brand, move to next
                        }
                    } catch (Exception e) {
                        // Continue to next xpath
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error finding brand elements: " + e.getMessage());
        }
    }
}
