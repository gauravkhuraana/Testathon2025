# Test Data for Login Scenarios

## Valid User Accounts
- demouser (standard user)
- image_not_loading_user (user with image loading issues)
- existing_orders_user (user with existing orders)
- fav_user (user with favorites)
- locked_user (locked user account)

## Password
- testingisfun99 (password for all users)

## Test Scenarios Covered

### 1. Successful Login Test
- **Purpose**: Verify that users can successfully log in with valid credentials
- **User**: demouser
- **Expected Result**: User is logged in and redirected to the main page

### 2. Invalid Credentials Test
- **Purpose**: Verify that login fails with invalid credentials
- **User**: invaliduser (not in dropdown)
- **Expected Result**: Login fails with appropriate error message

### 3. Empty Credentials Test
- **Purpose**: Verify that login fails when no credentials are selected
- **User**: (none selected)
- **Expected Result**: Login button should not work or show validation error

### 4. Multiple Users Test
- **Purpose**: Verify that all valid users can log in successfully
- **Users**: All users except locked_user
- **Expected Result**: Each user can log in and log out successfully

### 5. Locked User Test
- **Purpose**: Verify behavior when attempting to log in with locked user
- **User**: locked_user
- **Expected Result**: Either login is blocked or restricted functionality is shown

### 6. Form Validation Test
- **Purpose**: Verify client-side form validation
- **Tests**: Submit with no selection, username only, etc.
- **Expected Result**: Form validation prevents invalid submissions

### 7. Navigation After Login Test
- **Purpose**: Verify that navigation works correctly after login
- **User**: demouser
- **Expected Result**: User can access Offers, Orders, Favourites pages

### 8. Session Persistence Test
- **Purpose**: Verify that login session persists after page refresh
- **User**: demouser
- **Expected Result**: User remains logged in after refresh

### 9. Logout Test
- **Purpose**: Verify that users can successfully log out
- **User**: demouser
- **Expected Result**: User is logged out and redirected appropriately

### 10. Login Page Elements Test
- **Purpose**: Verify that all login page elements are present and functional
- **Tests**: Check dropdowns, button, form structure
- **Expected Result**: All elements are visible and functional

### 11. Dropdown Functionality Test
- **Purpose**: Verify that username and password dropdowns work correctly
- **Tests**: Open dropdowns, verify options are available
- **Expected Result**: All expected options are present and selectable

## Notes
- The site uses React Select components for username and password selection
- All tests should handle the async nature of the dropdown selections
- Tests should verify both successful login and proper logout functionality
- Session persistence across page refreshes should be validated
