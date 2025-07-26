package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;

/**
 * Debug test to find product count and listing elements
 */
public class DebugProductCountTest extends BaseTest {
    
    @Test(groups = {"debug"})
    public void findProductElements() {
        try {
            System.out.println("🔍 Starting product elements inspection...");
            
            // Wait for page to load completely
            Thread.sleep(5000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("📍 Current URL: " + currentUrl);
            
            // Look for product listing container
            System.out.println("\n🔍 Looking for product listing elements...");
            findProductListingElements();
            
            // Look for any text containing numbers
            System.out.println("\n🔍 Looking for elements with numbers...");
            findElementsWithNumbers();
            
            // Look for product cards/items
            System.out.println("\n🔍 Looking for product cards/items...");
            findProductItems();
            
        } catch (Exception e) {
            System.out.println("❌ Error during inspection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void findProductListingElements() {
        try {
            String[] productListingXPaths = {
                "//div[contains(@class, 'products')]",
                "//div[contains(@class, 'product-list')]",
                "//div[contains(@class, 'grid')]",
                "//div[contains(@class, 'catalog')]",
                "//div[contains(@class, 'items')]",
                "//div[contains(@class, 'content')]"
            };
            
            for (String xpath : productListingXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    if (!elements.isEmpty()) {
                        System.out.println("✅ Found " + elements.size() + " elements with xpath: " + xpath);
                        
                        // Get all text from the first few elements to see if we can find product count
                        for (int i = 0; i < Math.min(2, elements.size()); i++) {
                            WebElement elem = elements.get(i);
                            String text = elem.getText();
                            if (text != null && !text.trim().isEmpty()) {
                                System.out.println("   - Element " + i + " text: '" + text.replaceAll("\\n", " | ") + "'");
                                System.out.println("   - Class: " + elem.getAttribute("class"));
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error with xpath " + xpath + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error finding product listing elements: " + e.getMessage());
        }
    }
    
    private void findElementsWithNumbers() {
        try {
            // Use JavaScript to find all elements containing numbers
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script = """
                const allElements = document.querySelectorAll('*');
                const elementsWithNumbers = [];
                
                allElements.forEach(el => {
                    const text = el.textContent;
                    if (text && /\\d+/.test(text) && text.length < 100) {
                        elementsWithNumbers.push({
                            tag: el.tagName,
                            text: text.trim(),
                            className: el.className,
                            id: el.id
                        });
                    }
                });
                
                return elementsWithNumbers.slice(0, 10); // Return first 10
                """;
            
            @SuppressWarnings("unchecked")
            List<Object> results = (List<Object>) js.executeScript(script);
            
            System.out.println("Found " + results.size() + " elements with numbers:");
            for (Object result : results) {
                System.out.println("   - " + result.toString());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error finding elements with numbers: " + e.getMessage());
        }
    }
    
    private void findProductItems() {
        try {
            String[] productItemXPaths = {
                "//div[contains(@class, 'product')]",
                "//div[contains(@class, 'item')]",
                "//div[contains(@class, 'card')]",
                "//article",
                "//*[contains(@class, 'product')]"
            };
            
            for (String xpath : productItemXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    System.out.println("Product items found with " + xpath + ": " + elements.size());
                    
                    if (elements.size() > 0 && elements.size() < 50) {
                        System.out.println("   First few classes: ");
                        for (int i = 0; i < Math.min(3, elements.size()); i++) {
                            System.out.println("     - " + elements.get(i).getAttribute("class"));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error with product item xpath " + xpath + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error finding product items: " + e.getMessage());
        }
    }
}
