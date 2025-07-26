# Testathon 2025 - Login Test Scenarios

This document describes the comprehensive login test scenarios implemented for the testathon.live website using Selenium Java and BrowserStack.

## 🎯 Test Overview

The login functionality has been thoroughly tested with multiple scenarios covering:
- Valid and invalid login attempts
- Different user types and permissions
- Form validation and error handling
- Session management and persistence
- User interface elements and dropdowns
- End-to-end workflow validation

## 🌐 Website Under Test

**URL**: https://testathon.live/
**Application**: StackDemo (E-commerce demo site)
**Login URL**: https://testathon.live/signin

### Login Mechanism
The site uses React Select components for username and password selection:
- **Username Options**: 5 different user types available in dropdown
- **Password**: Single password (`testingisfun99`) for all users
- **Authentication**: Dropdown-based selection rather than text input

## 👥 Test User Accounts

| Username | Description | Expected Behavior |
|----------|-------------|-------------------|
| `demouser` | Standard user account | Full access, all features available |
| `image_not_loading_user` | User with image loading issues | Login successful, may have visual issues |
| `existing_orders_user` | User with existing order history | Login successful, has order data |
| `fav_user` | User with favorite items | Login successful, has favorites |
| `locked_user` | Locked user account | Behavior varies - may login with restrictions |

**Password for all users**: `testingisfun99`

## 📋 Test Scenarios Implemented

### 1. Basic Login Tests (`LoginTest.java`)

#### 1.1 Successful Login Test
- **Purpose**: Verify successful login with valid credentials
- **User**: `demouser`
- **Validation**: 
  - Login success verification
  - Username display validation
  - Navigation elements availability

#### 1.2 Invalid Credentials Test  
- **Purpose**: Verify login failure with invalid credentials
- **Test Data**: Non-existent username
- **Validation**: Login failure and error message display

#### 1.3 Empty Credentials Test
- **Purpose**: Verify form validation with empty selections
- **Test Data**: No username/password selected
- **Validation**: Login prevention or validation error

#### 1.4 Logout Test
- **Purpose**: Verify successful logout functionality
- **Prerequisites**: User must be logged in
- **Validation**: Logout button functionality and session termination

#### 1.5 Login Page Elements Test
- **Purpose**: Verify all login page elements are present
- **Validation**: Form elements, buttons, dropdowns visibility

### 2. Advanced Login Tests

#### 2.1 Multiple Users Test
- **Purpose**: Test login with different user types
- **Users**: All valid users except `locked_user`
- **Validation**: Each user can login and logout successfully

#### 2.2 Locked User Test
- **Purpose**: Test behavior with locked user account
- **User**: `locked_user`
- **Validation**: Document actual behavior (blocked or restricted access)

#### 2.3 Form Validation Test
- **Purpose**: Test client-side form validation
- **Scenarios**: 
  - No selection
  - Username only (no password)
- **Validation**: Form validation prevents invalid submissions

#### 2.4 Navigation After Login Test
- **Purpose**: Verify navigation functionality post-login
- **Validation**: Access to Offers, Orders, Favourites pages

#### 2.5 Session Persistence Test
- **Purpose**: Verify session survives page refresh
- **Test Steps**: Login → Refresh → Verify still logged in

#### 2.6 Dropdown Functionality Test
- **Purpose**: Verify dropdown controls work correctly
- **Validation**: Username and password dropdowns open and show options

### 3. Data-Driven Tests (`LoginDataDrivenTest.java`)

#### 3.1 Valid Users Data Provider Test
- **Purpose**: Test all valid users using TestNG DataProvider
- **Data**: Multiple username/password combinations
- **Validation**: Login success for each valid user

#### 3.2 User Scenarios Test
- **Purpose**: Test different user scenarios with expected outcomes
- **Data**: Users with success/failure expectations
- **Validation**: Actual vs expected behavior for each user type

#### 3.3 Username Dropdown Options Test
- **Purpose**: Verify all expected usernames appear in dropdown
- **Validation**: All 5 users are available for selection

#### 3.4 End-to-End Workflow Test
- **Purpose**: Complete login workflow from start to finish
- **Steps**: Home → Login → Authenticate → Navigate → Logout
- **Validation**: Full workflow functions correctly

## 🛠️ Technical Implementation

### Page Object Model
- **HomePage.java**: Main page interactions and navigation
- **LoginPage.java**: Login form interactions and validations
- **BaseTest.java**: Common test setup and utilities

### Key Features
- **React Select Handling**: Custom methods for dropdown interactions
- **Dynamic Waiting**: Robust wait strategies for async elements  
- **Error Handling**: Comprehensive exception handling
- **Reporting**: ExtentReports integration for detailed test reports
- **Cross-browser Support**: BrowserStack integration for multiple browsers

### Locator Strategies
```java
// Username dropdown
private final By usernameDropdownLocator = By.xpath("//div[contains(text(), 'Select Username')]/ancestor::div[contains(@class, 'css-')]");

// Password dropdown  
private final By passwordDropdownLocator = By.xpath("//div[contains(text(), 'Select Password')]/ancestor::div[contains(@class, 'css-')]");

// User options
private final By demouserOptionLocator = By.xpath("//div[text()='demouser']");

// Login button
private final By loginButtonLocator = By.id("login-btn");
```

## 🚀 Execution Instructions

### Quick Start
```bash
# Run all login tests
.\run-login-tests.ps1 -Mode all

# Run smoke tests only
.\run-login-tests.ps1 -Mode smoke

# Interactive mode
.\run-login-tests.ps1 -Mode interactive
```

### Maven Commands
```bash
# Run specific test class
mvn clean test -Dtest=LoginTest -Penvironment=browserstack

# Run by test groups
mvn clean test -Dgroups=smoke -Penvironment=browserstack

# Run data-driven tests
mvn clean test -Dtest=LoginDataDrivenTest -Penvironment=browserstack
```

### TestNG Suite Execution
```bash
# Run login test suite
mvn clean test -DsuiteXmlFile=testng-login-scenarios.xml -Penvironment=browserstack
```

## 📊 Test Reports

### Report Locations
- **Surefire Reports**: `target/surefire-reports/`
- **ExtentReports**: `test-output/extent-reports/`
- **TestNG Reports**: `test-output/`

### Report Features
- ✅ Test execution status
- 📸 Screenshots on failure
- 📝 Detailed step descriptions
- ⏱️ Execution timings
- 🔍 Error details and stack traces

## 🧪 Test Environment Configuration

### BrowserStack Integration
```properties
# browserstack.yml configuration
browserstack.username=${BROWSERSTACK_USERNAME}
browserstack.accessKey=${BROWSERSTACK_ACCESS_KEY}
```

### Local Execution
```properties
browser=chrome
environment=local
app.url.local=https://testathon.live
```

## 📈 Test Coverage

### Functional Coverage
- ✅ Authentication workflows
- ✅ User permission validation  
- ✅ Form input validation
- ✅ Session management
- ✅ Navigation post-login
- ✅ Error handling
- ✅ UI element verification

### Browser Coverage
- ✅ Chrome (Latest)
- ✅ Firefox (Latest)  
- ✅ Safari (Latest)
- ✅ Edge (Latest)
- ✅ Mobile browsers (iOS/Android)

### User Type Coverage
- ✅ Standard users
- ✅ Users with existing data
- ✅ Users with restrictions
- ✅ Invalid users
- ✅ Empty/null inputs

## 🔧 Troubleshooting

### Common Issues

#### 1. Dropdown Not Opening
**Solution**: Ensure proper wait for element visibility before clicking

#### 2. User Options Not Visible
**Solution**: Verify dropdown is opened before selecting options

#### 3. Login Button Not Clickable
**Solution**: Check if both username and password are selected

#### 4. Session Not Persisting
**Solution**: Verify cookies are enabled and not cleared

### Debug Mode
```bash
# Enable debug logging
mvn clean test -Dtest=LoginTest -Penvironment=local -Ddebug=true
```

## 📋 Test Execution Checklist

- [ ] Environment variables configured (BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY)
- [ ] Maven dependencies resolved
- [ ] Test data files present
- [ ] BrowserStack connection verified
- [ ] Test reports directory accessible
- [ ] All test classes compile successfully
- [ ] TestNG configuration files present

## 🎯 Success Criteria

A successful test execution should demonstrate:
1. All valid users can login successfully
2. Invalid users are properly rejected
3. Form validation works correctly
4. Session management functions properly
5. Navigation works after login
6. Logout functionality operates correctly
7. All UI elements are present and functional

## 📞 Support

For issues or questions regarding the login test scenarios:
1. Check the troubleshooting section above
2. Review test execution logs
3. Verify environment configuration
4. Ensure latest dependencies are installed

---

*Last Updated: July 26, 2025*
*Test Framework: Selenium WebDriver + TestNG*
*Reporting: ExtentReports*
*Cloud Platform: BrowserStack*
