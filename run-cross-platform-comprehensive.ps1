# Comprehensive BrowserStack Cross-Platform Test Execution Script
# This script ensures credentials are properly set and runs the tests

param(
    [string]$TestSuite = "cross-platform-limited",
    [string]$Username = "gauravkhurana_yShwDZ",
    [string]$AccessKey = "VGzb8uFGpPF7XWjXzcsG"
)

Write-Host "========== BrowserStack Cross-Platform Test Execution ==========" -ForegroundColor Green

# Step 1: Set environment variables
Write-Host "`n1. Setting BrowserStack credentials..." -ForegroundColor Yellow
$env:BROWSERSTACK_USERNAME = $Username
$env:BROWSERSTACK_ACCESS_KEY = $AccessKey

# Step 2: Set Maven options with system properties
Write-Host "2. Configuring Maven options..." -ForegroundColor Yellow
$env:MAVEN_OPTS = "-Dbrowserstack.username=$Username -Dbrowserstack.accesskey=$AccessKey"

# Step 3: Verify credentials
Write-Host "3. Verifying BrowserStack API connection..." -ForegroundColor Yellow
try {
    $base64Auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${Username}:${AccessKey}"))
    $headers = @{ Authorization = "Basic $base64Auth" }
    $response = Invoke-RestMethod -Uri "https://api.browserstack.com/automate/plan.json" -Headers $headers -Method GET
    
    Write-Host "   ✓ API Connection successful!" -ForegroundColor Green
    Write-Host "   ✓ Plan: $($response.parallel_sessions_max_allowed) parallel sessions allowed" -ForegroundColor Cyan
    Write-Host "   ✓ Currently running: $($response.parallel_sessions_running) sessions" -ForegroundColor Cyan
    Write-Host "   ✓ Currently queued: $($response.queued_sessions) sessions" -ForegroundColor Cyan
    
    if ($response.parallel_sessions_running -ge $response.parallel_sessions_max_allowed) {
        Write-Host "   ⚠ Warning: All parallel sessions are in use. Tests may queue." -ForegroundColor Yellow
    }
} catch {
    Write-Host "   ✗ API Connection failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Please verify your credentials and try again." -ForegroundColor Yellow
    exit 1
}

# Step 4: Determine test suite file
$suiteFile = switch ($TestSuite) {
    "cross-platform-limited" { "src/test/resources/testng-cross-platform-limited.xml" }
    "mobile" { "src/test/resources/testng-mobile-devices.xml" }
    "tablet" { "src/test/resources/testng-tablet-devices.xml" }
    "browser-compatibility" { "src/test/resources/testng-browser-compatibility.xml" }
    default { "src/test/resources/testng-cross-platform-limited.xml" }
}

Write-Host "`n4. Selected test suite: $TestSuite" -ForegroundColor Yellow
Write-Host "   Suite file: $suiteFile" -ForegroundColor Cyan

# Step 5: Compile project first
Write-Host "`n5. Compiling project..." -ForegroundColor Yellow
$compileResult = & mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ✗ Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "   ✓ Compilation successful!" -ForegroundColor Green

# Step 6: Run tests
Write-Host "`n6. Executing cross-platform tests..." -ForegroundColor Yellow
Write-Host "   Command: mvn test -Pbrowserstack `"-Dtestng.suite.file=$suiteFile`"" -ForegroundColor Cyan
Write-Host "   This may take several minutes as tests run on multiple platforms..." -ForegroundColor Yellow

# Execute the tests
& mvn test -Pbrowserstack "-Dtestng.suite.file=$suiteFile"

# Step 7: Check results
if ($LASTEXITCODE -eq 0) {
    Write-Host "`n========== Test Execution Completed Successfully! ==========" -ForegroundColor Green
    Write-Host "✓ All cross-platform tests have been executed" -ForegroundColor Green
    Write-Host "✓ Check the extent reports in test-output/extent-reports/ for detailed results" -ForegroundColor Green
    Write-Host "✓ Screenshots for failed tests are in test-output/screenshots/" -ForegroundColor Green
} else {
    Write-Host "`n========== Test Execution Completed with Issues ==========" -ForegroundColor Yellow
    Write-Host "! Some tests may have failed or had issues" -ForegroundColor Yellow
    Write-Host "! Check the extent reports in test-output/extent-reports/ for detailed analysis" -ForegroundColor Yellow
    Write-Host "! Review console output above for specific error details" -ForegroundColor Yellow
}

Write-Host "`nTo run different test suites, use:" -ForegroundColor Cyan
Write-Host "  .\run-cross-platform-comprehensive.ps1 -TestSuite mobile" -ForegroundColor White
Write-Host "  .\run-cross-platform-comprehensive.ps1 -TestSuite tablet" -ForegroundColor White
Write-Host "  .\run-cross-platform-comprehensive.ps1 -TestSuite browser-compatibility" -ForegroundColor White
Write-Host "=================================================================" -ForegroundColor Green
