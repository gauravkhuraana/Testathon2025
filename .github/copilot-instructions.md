# Copilot Instructions for Java Selenium BrowserStack Framework

## Project Overview
This is a Java-based Selenium automation framework designed for cross-browser testing using BrowserStack SDK. The framework uses TestNG for test execution with parallel execution capabilities and Maven for dependency management.

## Architecture & Structure

### Expected Maven Project Structure
```
src/
├── main/java/
│   ├── config/          # Configuration classes (BrowserStack, TestNG)
│   ├── pages/           # Page Object Model classes
│   ├── utils/           # Utility classes (WebDriver factory, helpers)
│   └── base/            # Base test classes
├── test/java/
│   ├── tests/           # Test classes extending base test
│   └── suites/          # TestNG suite configurations
└── resources/
    ├── testng.xml       # TestNG configuration for parallel execution
    ├── browserstack.yml # BrowserStack configuration
    └── config.properties # Framework configuration
```

### Key Components to Implement

**BrowserStack Integration:**
- All capabilities must include observability, screenshot, and video recording enabled
- Use BrowserStack SDK for session management
- Configure capabilities through `browserstack.yml`
- Implement WebDriver factory pattern for cross-browser support

**TestNG Configuration:**
- Configure parallel execution at test/method level in `testng.xml`
- Use data providers for parameterized tests
- Implement listeners for reporting and screenshots
- Set up test groups for categorization

**Page Object Model:**
- Create page classes with WebElement annotations
- Use PageFactory for element initialization
- Implement BasePage class with common functionality
- Apply explicit waits and WebDriverWait patterns

## Critical Implementation Patterns

### WebDriver Factory Pattern
```java
// Expected pattern for browser initialization
WebDriver driver = BrowserStackDriverFactory.createDriver(capabilities);
```

### BrowserStack Capabilities
All test configurations must include:
- `bstack:options.debug: true` (screenshots)
- `bstack:options.video: true` (video recording)
- `bstack:options.networkLogs: true` (observability)

### TestNG Parallel Execution
Configure `testng.xml` with:
- `parallel="tests"` or `parallel="methods"`
- `thread-count` based on BrowserStack plan limits
- Data-driven tests using `@DataProvider`

## Development Workflow

### Maven Dependencies Required
- selenium-java (latest stable)
- testng
- browserstack-java-sdk
- extent-reports or allure-testng

### Test Execution Commands
```bash
# Run all tests
mvn clean test

# Run specific test suite
mvn clean test -DsuiteXmlFile=testng.xml

# Run with specific browser
mvn clean test -Dbrowser=chrome -Dos=Windows
```

### Configuration Management
- Environment-specific configs in `config.properties`
- BrowserStack credentials via environment variables or config files
- Browser/OS combinations defined in data providers

## Code Conventions

### Naming Patterns
- Test classes: `*Test.java` (e.g., `LoginTest.java`)
- Page classes: `*Page.java` (e.g., `LoginPage.java`)
- Utility classes: `*Utils.java` or `*Helper.java`

### Test Structure
Each test class should:
1. Extend `BaseTest` class
2. Use `@BeforeMethod` for driver initialization
3. Use `@AfterMethod` for cleanup
4. Include proper assertions with meaningful messages
5. Implement screenshots on failure

## Integration Points

### BrowserStack Integration
- Session management through BrowserStack SDK
- Real-time test status updates
- Automatic screenshot and video capture
- Network log collection for debugging

### Reporting Integration
- TestNG native reporting
- Consider Extent Reports or Allure for enhanced reporting
- BrowserStack dashboard integration for test results

## Example Test Structure
```java
@Test(groups = {"smoke"}, dataProvider = "browserData")
public void testLogin(String browser, String os) {
    // Test implementation with proper page object usage
}
```

## Key Files to Reference
- `whatisthisabout.md` - Project requirements and scope
- Future: `browserstack.yml` - BrowserStack configuration
- Future: `testng.xml` - TestNG parallel execution setup
- Future: `BasePage.java` - Common page functionality
- Future: `BaseTest.java` - Test initialization and teardown

## Framework Goals
1. Demonstrate cross-browser testing capabilities
2. Showcase parallel test execution
3. Provide 2-3 comprehensive test cases
4. Enable full BrowserStack observability features
5. Create maintainable, scalable test automation structure
