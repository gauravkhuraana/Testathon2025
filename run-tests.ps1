# PowerShell script for BrowserStack Selenium Test Execution
# BrowserStack Selenium Test Framework

Write-Host "========================================" -ForegroundColor Green
Write-Host "Selenium BrowserStack Test Framework" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Check if Maven is installed
try {
    $null = mvn --version
    Write-Host "✅ Maven found" -ForegroundColor Green
} catch {
    Write-Host "❌ ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven and try again" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check for BrowserStack credentials
if (-not $env:BROWSERSTACK_USERNAME) {
    Write-Host "⚠️ WARNING: BROWSERSTACK_USERNAME not set" -ForegroundColor Yellow
    Write-Host "Please set your BrowserStack credentials:" -ForegroundColor Yellow
    Write-Host "`$env:BROWSERSTACK_USERNAME='your_username'" -ForegroundColor Cyan
    Write-Host "`$env:BROWSERSTACK_ACCESS_KEY='your_access_key'" -ForegroundColor Cyan
}

if (-not $env:BROWSERSTACK_ACCESS_KEY) {
    Write-Host "⚠️ WARNING: BROWSERSTACK_ACCESS_KEY not set" -ForegroundColor Yellow
    Write-Host "Please set your BrowserStack credentials:" -ForegroundColor Yellow
    Write-Host "`$env:BROWSERSTACK_USERNAME='your_username'" -ForegroundColor Cyan
    Write-Host "`$env:BROWSERSTACK_ACCESS_KEY='your_access_key'" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Available Test Execution Options:" -ForegroundColor Cyan
Write-Host "1. Run all tests on BrowserStack"
Write-Host "2. Run smoke tests only"
Write-Host "3. Run regression tests only"
Write-Host "4. Run tests locally (Chrome)"
Write-Host "5. Run specific test class"
Write-Host "6. Clean and compile"
Write-Host "7. Generate test report"
Write-Host "8. Exit"
Write-Host ""

$choice = Read-Host "Enter your choice (1-8)"

switch ($choice) {
    "1" {
        Write-Host "🚀 Running all tests on BrowserStack..." -ForegroundColor Green
        mvn clean test -Penvironment=browserstack
    }
    "2" {
        Write-Host "🚀 Running smoke tests on BrowserStack..." -ForegroundColor Green
        mvn clean test -Penvironment=browserstack -Dgroups=smoke
    }
    "3" {
        Write-Host "🚀 Running regression tests on BrowserStack..." -ForegroundColor Green
        mvn clean test -Penvironment=browserstack -Dgroups=regression
    }
    "4" {
        Write-Host "🚀 Running tests locally with Chrome..." -ForegroundColor Green
        mvn clean test -Penvironment=local -Dbrowser=chrome
    }
    "5" {
        Write-Host ""
        Write-Host "Available test classes:" -ForegroundColor Cyan
        Write-Host "1. LoginTest"
        Write-Host "2. ProductTest"
        Write-Host "3. CheckoutTest"
        Write-Host ""
        $testChoice = Read-Host "Enter test class number (1-3)"
        
        switch ($testChoice) {
            "1" {
                Write-Host "🚀 Running LoginTest..." -ForegroundColor Green
                mvn clean test -Penvironment=browserstack -Dtest=LoginTest
            }
            "2" {
                Write-Host "🚀 Running ProductTest..." -ForegroundColor Green
                mvn clean test -Penvironment=browserstack -Dtest=ProductTest
            }
            "3" {
                Write-Host "🚀 Running CheckoutTest..." -ForegroundColor Green
                mvn clean test -Penvironment=browserstack -Dtest=CheckoutTest
            }
            default {
                Write-Host "❌ Invalid choice" -ForegroundColor Red
            }
        }
    }
    "6" {
        Write-Host "🔧 Cleaning and compiling project..." -ForegroundColor Green
        mvn clean compile
    }
    "7" {
        Write-Host "📊 Generating test report..." -ForegroundColor Green
        mvn surefire-report:report
        Write-Host "Report generated in target/site/surefire-report.html" -ForegroundColor Cyan
    }
    "8" {
        Write-Host "👋 Exiting..." -ForegroundColor Yellow
        exit 0
    }
    default {
        Write-Host "❌ Invalid choice. Please select 1-8." -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "✅ Test execution completed!" -ForegroundColor Green
Write-Host "Check the following for results:" -ForegroundColor Cyan
Write-Host "- Console output above"
Write-Host "- test-output/extent-reports/ folder for HTML reports"
Write-Host "- BrowserStack dashboard for session details"
Write-Host "========================================" -ForegroundColor Green

Read-Host "Press Enter to exit"
