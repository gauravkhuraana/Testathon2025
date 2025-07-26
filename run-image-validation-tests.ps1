# PowerShell script to run Image Loading Validation Tests
# This script runs tests that are designed to FAIL when images don't load properly

Write-Host "🖼️ Starting Image Loading Validation Tests" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

# Check if BrowserStack credentials are set
if (-not $env:BROWSERSTACK_USERNAME -or -not $env:BROWSERSTACK_ACCESS_KEY) {
    Write-Host "❌ BrowserStack credentials not found!" -ForegroundColor Red
    Write-Host "Please set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY environment variables" -ForegroundColor Red
    Write-Host ""
    Write-Host "Run the following commands:" -ForegroundColor Yellow
    Write-Host "Set-BrowserStackCredentials.ps1" -ForegroundColor Cyan
    Write-Host "OR" -ForegroundColor Yellow
    Write-Host "`$env:BROWSERSTACK_USERNAME = 'your_username'" -ForegroundColor Cyan
    Write-Host "`$env:BROWSERSTACK_ACCESS_KEY = 'your_access_key'" -ForegroundColor Cyan
    exit 1
}

Write-Host "✅ BrowserStack credentials found" -ForegroundColor Green
Write-Host "Username: $env:BROWSERSTACK_USERNAME" -ForegroundColor Gray
Write-Host ""

# Display test information
Write-Host "🧪 Test Information:" -ForegroundColor Cyan
Write-Host "   - User: image_not_loading_user" -ForegroundColor Gray
Write-Host "   - Purpose: Validate image loading on various pages" -ForegroundColor Gray
Write-Host "   - Expected Result: TESTS WILL FAIL if images don't load" -ForegroundColor Red
Write-Host "   - Framework: Selenium + TestNG + BrowserStack" -ForegroundColor Gray
Write-Host ""

# Run the image validation tests
Write-Host "🚀 Running Image Loading Validation Tests..." -ForegroundColor Yellow

try {
    # Run Maven test with specific TestNG suite
    mvn clean test -Penvironment=browserstack -DsuiteXmlFile=testng-image-validation.xml
    
    $exitCode = $LASTEXITCODE
    
    Write-Host ""
    Write-Host "📊 Test Execution Summary:" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    
    if ($exitCode -eq 0) {
        Write-Host "✅ All tests passed - Images are loading correctly" -ForegroundColor Green
        Write-Host "   This means image_not_loading_user does NOT have image loading issues" -ForegroundColor Green
    } else {
        Write-Host "❌ Some tests failed - Image loading issues detected" -ForegroundColor Red
        Write-Host "   This is EXPECTED behavior for image_not_loading_user" -ForegroundColor Yellow
        Write-Host "   Check the test reports for detailed information about broken images" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "❌ Error running tests: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "📁 Test Reports Available:" -ForegroundColor Cyan
Write-Host "   - ExtentReports: test-output/extent-reports/" -ForegroundColor Gray
Write-Host "   - Screenshots: test-output/screenshots/" -ForegroundColor Gray
Write-Host "   - TestNG Reports: test-output/" -ForegroundColor Gray
Write-Host ""

Write-Host "🌐 BrowserStack Dashboard:" -ForegroundColor Cyan
Write-Host "   https://automate.browserstack.com/dashboard" -ForegroundColor Blue
Write-Host ""

Write-Host "✨ Image Loading Validation Test Execution Complete!" -ForegroundColor Green
