# Testathon 2025 - Login Tests Execution Script (PowerShell)
# This script runs comprehensive login test scenarios for testathon.live

param(
    [string]$Mode = "help",
    [string]$Environment = "browserstack", 
    [string]$Browser = "chrome"
)

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Testathon 2025 - Login Tests Execution" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Function to run specific test suite
function Run-TestSuite {
    param(
        [string]$SuiteName,
        [string]$SuiteFile
    )
    
    Write-Host "Running $SuiteName..." -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Gray
    
    $command = "mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -DsuiteXmlFile=$SuiteFile"
    Write-Host "Executing: $command" -ForegroundColor Gray
    
    $result = Invoke-Expression $command
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Write-Host "✅ $SuiteName - PASSED" -ForegroundColor Green
        return $true
    } else {
        Write-Host "❌ $SuiteName - FAILED" -ForegroundColor Red
        return $false
    }
}

# Function to run all login tests
function Run-AllLoginTests {
    Write-Host "Running All Login Test Scenarios..." -ForegroundColor Yellow
    Write-Host "==========================================" -ForegroundColor Gray
    
    $totalTests = 0
    $passedTests = 0
    
    # Test Suite 1: Login Smoke Tests
    $totalTests++
    if (Run-TestSuite "Login Smoke Tests" "testng-login-scenarios.xml") {
        $passedTests++
    }
    
    # Test Suite 2: Login Data-Driven Tests  
    $totalTests++
    Write-Host "Running Login Data-Driven Tests..." -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Gray
    
    $command = "mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dtest=LoginDataDrivenTest"
    $result = Invoke-Expression $command
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Login Data-Driven Tests - PASSED" -ForegroundColor Green
        $passedTests++
    } else {
        Write-Host "❌ Login Data-Driven Tests - FAILED" -ForegroundColor Red
    }
    Write-Host ""
    
    # Test Suite 3: Individual Login Test
    $totalTests++
    Write-Host "Running Individual Login Tests..." -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Gray
    
    $command = "mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dtest=LoginTest"
    $result = Invoke-Expression $command
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Individual Login Tests - PASSED" -ForegroundColor Green
        $passedTests++
    } else {
        Write-Host "❌ Individual Login Tests - FAILED" -ForegroundColor Red
    }
    Write-Host ""
    
    # Summary
    Write-Host "==========================================" -ForegroundColor Cyan
    Write-Host "Test Execution Summary" -ForegroundColor Cyan
    Write-Host "==========================================" -ForegroundColor Cyan
    Write-Host "Total Test Suites: $totalTests" -ForegroundColor White
    Write-Host "Passed: $passedTests" -ForegroundColor Green
    Write-Host "Failed: $($totalTests - $passedTests)" -ForegroundColor Red
    Write-Host ""
    
    if ($passedTests -eq $totalTests) {
        Write-Host "🎉 ALL LOGIN TESTS PASSED!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "⚠️  Some tests failed. Check logs for details." -ForegroundColor Yellow
        return $false
    }
}

# Function to run specific login test scenarios
function Run-SpecificScenarios {
    Write-Host "Available Login Test Scenarios:" -ForegroundColor Yellow
    Write-Host "1. Smoke Tests (Quick validation)" -ForegroundColor White
    Write-Host "2. Regression Tests (Comprehensive)" -ForegroundColor White
    Write-Host "3. Data-Driven Tests (Multiple users)" -ForegroundColor White
    Write-Host "4. All Tests" -ForegroundColor White
    Write-Host ""
    
    $choice = Read-Host "Select scenario (1-4)"
    
    switch ($choice) {
        1 {
            Write-Host "Running Smoke Tests..." -ForegroundColor Yellow
            mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dgroups=smoke -Dtest=LoginTest
        }
        2 {
            Write-Host "Running Regression Tests..." -ForegroundColor Yellow
            mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dgroups=regression -Dtest=LoginTest
        }
        3 {
            Write-Host "Running Data-Driven Tests..." -ForegroundColor Yellow
            mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dtest=LoginDataDrivenTest
        }
        4 {
            Run-AllLoginTests
        }
        default {
            Write-Host "Invalid choice. Running smoke tests by default." -ForegroundColor Yellow
            mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dgroups=smoke -Dtest=LoginTest
        }
    }
}

# Main execution logic
switch ($Mode.ToLower()) {
    "all" {
        Run-AllLoginTests
    }
    "interactive" {
        Run-SpecificScenarios
    }
    "smoke" {
        Write-Host "Running Login Smoke Tests..." -ForegroundColor Yellow
        mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dgroups=smoke -Dtest=LoginTest
    }
    "regression" {
        Write-Host "Running Login Regression Tests..." -ForegroundColor Yellow
        mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dgroups=regression -Dtest=LoginTest
    }
    "datadriven" {
        Write-Host "Running Data-Driven Login Tests..." -ForegroundColor Yellow
        mvn clean test -Penvironment=$Environment -Dbrowser=$Browser -Dtest=LoginDataDrivenTest
    }
    default {
        Write-Host "Usage Examples:" -ForegroundColor Yellow
        Write-Host "  .\run-login-tests.ps1 -Mode all            # Run all login test scenarios" -ForegroundColor White
        Write-Host "  .\run-login-tests.ps1 -Mode interactive    # Interactive mode to select scenarios" -ForegroundColor White
        Write-Host "  .\run-login-tests.ps1 -Mode smoke          # Run smoke tests only" -ForegroundColor White
        Write-Host "  .\run-login-tests.ps1 -Mode regression     # Run regression tests only" -ForegroundColor White
        Write-Host "  .\run-login-tests.ps1 -Mode datadriven     # Run data-driven tests only" -ForegroundColor White
        Write-Host ""
        Write-Host "Parameters:" -ForegroundColor Yellow
        Write-Host "  -Environment: $Environment (default: browserstack)" -ForegroundColor White
        Write-Host "  -Browser: $Browser (default: chrome)" -ForegroundColor White
        Write-Host ""
        Write-Host "Test Reports will be available in:" -ForegroundColor Yellow
        Write-Host "  - target\surefire-reports\" -ForegroundColor White
        Write-Host "  - test-output\extent-reports\" -ForegroundColor White
        Write-Host ""
        Write-Host "Login Test Scenarios Covered:" -ForegroundColor Yellow
        Write-Host "✓ Successful login with valid credentials" -ForegroundColor Green
        Write-Host "✓ Login with invalid credentials" -ForegroundColor Green
        Write-Host "✓ Login with empty credentials" -ForegroundColor Green
        Write-Host "✓ Login with different user types" -ForegroundColor Green
        Write-Host "✓ Login with locked user account" -ForegroundColor Green
        Write-Host "✓ Login form validation" -ForegroundColor Green
        Write-Host "✓ Navigation after successful login" -ForegroundColor Green
        Write-Host "✓ Session persistence after page refresh" -ForegroundColor Green
        Write-Host "✓ Logout functionality" -ForegroundColor Green
        Write-Host "✓ Login page elements verification" -ForegroundColor Green
        Write-Host "✓ Login dropdown functionality" -ForegroundColor Green
        Write-Host "✓ End-to-end login workflow" -ForegroundColor Green
    }
}
