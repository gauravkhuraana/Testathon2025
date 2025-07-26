# Selenium BrowserStack Automation Framework

## Overview
This is a comprehensive Java-based Selenium automation framework designed for cross-browser testing using BrowserStack SDK. The framework uses TestNG for test execution with parallel execution capabilities and Maven for dependency management.

## Framework Architecture

### Project Structure
```
src/
├── main/java/
│   ├── config/          # Configuration management
│   │   └── ConfigManager.java
│   ├── pages/           # Page Object Model classes
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── CartPage.java
│   │   └── CheckoutPage.java
│   ├── utils/           # Utility classes
│   │   ├── WebDriverFactory.java
│   │   ├── SeleniumUtils.java
│   │   ├── TestListener.java
│   │   └── ExtentReportListener.java
│   └── base/            # Base classes
│       ├── BasePage.java
│       └── BaseTest.java
├── test/java/
│   └── tests/           # Test classes
│       ├── LoginTest.java
│       ├── ProductTest.java
│       └── CheckoutTest.java
└── resources/
    ├── testng.xml       # TestNG configuration
    ├── browserstack.yml # BrowserStack configuration
    ├── config.properties # Framework configuration
    └── testdata/        # Test data files
```

## Key Features

### 1. BrowserStack Integration
- **Cross-browser testing** on real devices and browsers
- **Parallel execution** across multiple browser combinations
- **Complete observability** with screenshots, videos, and network logs
- **Session management** through BrowserStack SDK

### 2. Page Object Model (POM)
- **Maintainable code structure** with separate page classes
- **Reusable components** and common functionality
- **WebElement management** using PageFactory
- **Explicit waits** and robust element handling

### 3. TestNG Integration
- **Parallel test execution** at test and method levels
- **Test grouping** for smoke and regression testing
- **Data-driven testing** capabilities
- **Comprehensive reporting** with ExtentReports

### 4. Configuration Management
- **Environment-specific configurations**
- **BrowserStack credentials management**
- **Flexible property-based settings**
- **Runtime parameter support**

## BrowserStack Capabilities

All tests are configured with the following BrowserStack capabilities:

```yaml
# Observability Features
debug: true              # Enable screenshots
video: true             # Enable video recording
networkLogs: true       # Enable network logs
consoleLogs: verbose    # Enable console logs
seleniumLogs: true      # Enable Selenium logs

# Test Configuration
buildName: "Testathon 2025 - Selenium Framework"
projectName: "Java Selenium BrowserStack Framework"
```

## Supported Browser Combinations

### Windows 11
- **Chrome** (latest version)
- **Firefox** (latest version)

### macOS Monterey
- **Safari** (latest version)

Additional browsers can be easily added through the `browserstack.yml` configuration.

## Test Cases Implemented

### 1. Login Tests (`LoginTest.java`)
- ✅ **Successful login** with valid credentials
- ✅ **Failed login** with invalid credentials
- ✅ **Empty credentials** validation
- ✅ **Logout functionality**
- ✅ **Login page elements** verification

### 2. Product Tests (`ProductTest.java`)
- ✅ **Product display** verification
- ✅ **Add single product** to cart
- ✅ **Add multiple products** to cart
- ✅ **Cart functionality** testing
- ✅ **Remove products** from cart
- ✅ **Empty cart** handling

### 3. Checkout Tests (`CheckoutTest.java`)
- ✅ **Complete checkout process** end-to-end
- ✅ **Checkout without login** validation
- ✅ **Empty cart checkout** prevention
- ✅ **Form validation** testing
- ✅ **Navigation flow** verification

## Quick Start Guide

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- BrowserStack account with valid credentials

### Environment Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Testathon2025
   ```

2. **Set BrowserStack credentials**
   ```bash
   # Windows
   set BROWSERSTACK_USERNAME=your_username
   set BROWSERSTACK_ACCESS_KEY=your_access_key
   
   # macOS/Linux
   export BROWSERSTACK_USERNAME=your_username
   export BROWSERSTACK_ACCESS_KEY=your_access_key
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

### Running Tests

#### Run All Tests on BrowserStack
```bash
mvn clean test -Penvironment=browserstack
```

#### Run Specific Test Suite
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

#### Run Tests Locally
```bash
mvn clean test -Penvironment=local -Dbrowser=chrome
```

#### Run Specific Test Groups
```bash
# Smoke tests only
mvn clean test -Dgroups=smoke

# Regression tests only
mvn clean test -Dgroups=regression
```

## Test Configuration

### TestNG Configuration (`testng.xml`)
- **Parallel execution** configured for `tests` level
- **Thread count**: 3 (configurable based on BrowserStack plan)
- **Test grouping**: smoke, regression
- **Multiple browser combinations** in separate test nodes

### BrowserStack Configuration (`browserstack.yml`)
- **Platform definitions** for different OS/browser combinations
- **Observability features** enabled for all tests
- **Build and project naming** for better organization
- **Parallel execution** limits configured

## Reporting

### ExtentReports Integration
- **Rich HTML reports** with screenshots and logs
- **Test execution timeline** and duration tracking
- **Browser and OS information** for each test
- **Failure analysis** with stack traces and screenshots

### Report Location
```
test-output/extent-reports/ExtentReport_[timestamp].html
```

### Screenshot Capture
- **Automatic screenshots** on test failures
- **Manual screenshot** capability in tests
- **Screenshots attached** to ExtentReports

## Best Practices Implemented

### 1. Code Organization
- **Clear separation** of concerns (pages, tests, utilities)
- **Consistent naming** conventions
- **Proper exception handling**
- **Comprehensive logging**

### 2. Test Design
- **Independent test cases** that can run in any order
- **Proper setup and teardown** methods
- **Meaningful assertions** with descriptive messages
- **Data-driven approach** where applicable

### 3. Maintenance
- **Centralized configuration** management
- **Reusable utility methods**
- **Page Object Model** for UI element management
- **Version-controlled dependencies**

## Troubleshooting

### Common Issues

1. **BrowserStack Connection Issues**
   - Verify credentials are set correctly
   - Check internet connectivity
   - Ensure BrowserStack plan has available sessions

2. **Test Failures**
   - Check application availability
   - Verify element locators are current
   - Review BrowserStack session logs

3. **Local Execution Issues**
   - Ensure browsers are installed and up to date
   - Selenium 4+ automatically manages driver binaries
   - Verify Java and Maven versions

### Debug Mode
Enable debug logging by setting:
```bash
mvn clean test -Ddebug=true
```

## Contributing

### Code Standards
- Follow Java naming conventions
- Add appropriate JavaDoc comments
- Include unit tests for utility methods
- Update documentation for new features

### Adding New Tests
1. Create page objects for new pages in `pages/` package
2. Add test methods in appropriate test classes
3. Update TestNG suite configuration if needed
4. Add test data to `testdata/` directory

## Contact and Support

For questions or issues:
- Review the framework documentation
- Check BrowserStack documentation for platform-specific issues
- Refer to TestNG and Selenium documentation for advanced configurations

---

**Framework Version**: 1.0.0  
**Last Updated**: January 2025  
**Java Version**: 17+  
**Selenium Version**: 4.27.0  
**TestNG Version**: 7.10.2
