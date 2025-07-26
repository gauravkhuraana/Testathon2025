# BrowserStack Credentials Setup

## Quick Setup Instructions

### 1. Configure BrowserStack Credentials

1. Edit the `.env` file in the root directory
2. Replace the placeholder values with your actual BrowserStack credentials:
   ```
   BROWSERSTACK_USERNAME=your_actual_username
   BROWSERSTACK_ACCESS_KEY=your_actual_access_key
   ```

### 2. Set Execution Environment

In `src/test/resources/config.properties`, change the execution environment:
- For local execution: `execution.environment=local`
- For BrowserStack execution: `execution.environment=browserstack`

### 3. Load Environment Variables (Before Running Tests)

#### Option A: Using Batch File (Windows)
```cmd
load-env.bat
```

#### Option B: Using PowerShell (Windows)
```powershell
.\load-env.ps1
```

#### Option C: Manual Environment Variables
```cmd
set BROWSERSTACK_USERNAME=your_username
set BROWSERSTACK_ACCESS_KEY=your_access_key
```

### 4. Run Tests

#### Run All Tests on BrowserStack
```cmd
mvn clean test -Penvironment=browserstack
```

#### Run All Tests Locally
```cmd
mvn clean test -Penvironment=local
```

#### Run Specific Test Groups
```cmd
mvn clean test -Penvironment=browserstack -Dgroups=smoke
```

## Security Notes

- ⚠️ **NEVER** commit the `.env` file to version control
- The `.env` file is already included in `.gitignore`
- Always use environment variables for sensitive credentials
- Consider using your CI/CD system's secret management for production

## Finding Your BrowserStack Credentials

1. Log in to your BrowserStack account
2. Go to Account Settings
3. Find your Username and Access Key in the "Automate" section
4. Copy these values to your `.env` file
