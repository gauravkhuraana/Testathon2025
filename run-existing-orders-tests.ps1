# PowerShell script to run existing_orders_user validation tests
# This script tests the orders page functionality and validates issues

Write-Host "🚀 Starting existing_orders_user Orders Page Validation Tests..." -ForegroundColor Green
Write-Host "📦 Testing orders page functionality and issue detection" -ForegroundColor Cyan
Write-Host "🔍 Looking for order total calculation and indentation issues" -ForegroundColor Yellow

# Load environment variables
if (Test-Path ".\load-env.ps1") {
    Write-Host "📋 Loading environment variables..." -ForegroundColor Blue
    . .\load-env.ps1
} else {
    Write-Host "⚠️ load-env.ps1 not found. Please ensure BrowserStack credentials are set." -ForegroundColor Yellow
}

Write-Host "⚡ Running existing_orders_user validation tests with BrowserStack..." -ForegroundColor Magenta

try {
    # Run the specific orders validation test suite
    mvn clean test -Penvironment=browserstack -DsuiteXmlFile=testng-existing-orders.xml
    
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Write-Host "✅ existing_orders_user orders validation tests completed successfully!" -ForegroundColor Green
        Write-Host "📊 Check the test reports for detailed issue analysis:" -ForegroundColor Cyan
        Write-Host "   - ExtentReport: test-output/extent-reports/" -ForegroundColor White
        Write-Host "   - Screenshots: test-output/screenshots/" -ForegroundColor White
        Write-Host "   - BrowserStack Dashboard: https://automate.browserstack.com/" -ForegroundColor White
    } else {
        Write-Host "❌ Tests completed with issues. Exit code: $exitCode" -ForegroundColor Red
        Write-Host "🔍 Check logs above for details about detected issues" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error running tests: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "📝 Test Summary:" -ForegroundColor Blue
Write-Host "   - User: existing_orders_user" -ForegroundColor White
Write-Host "   - Focus: Orders page validation" -ForegroundColor White  
Write-Host "   - Validation: Order totals calculation" -ForegroundColor White
Write-Host "   - Validation: CSS indentation and alignment" -ForegroundColor White
Write-Host "   - Platform: BrowserStack cross-browser testing" -ForegroundColor White
Write-Host ""
