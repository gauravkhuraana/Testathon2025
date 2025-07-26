# PowerShell script to set BrowserStack credentials for the current session
# This ensures credentials are available to Maven and Java processes

param(
    [string]$Username = "gauravkhurana_yShwDZ",
    [string]$AccessKey = "VGzb8uFGpPF7XWjXzcsG"
)

Write-Host "========== Setting BrowserStack Credentials ==========" -ForegroundColor Green

# Set environment variables for current PowerShell session
$env:BROWSERSTACK_USERNAME = $Username
$env:BROWSERSTACK_ACCESS_KEY = $AccessKey

# Also set as system properties for Maven
$env:MAVEN_OPTS = "-Dbrowserstack.username=$Username -Dbrowserstack.accesskey=$AccessKey"

Write-Host "✓ BROWSERSTACK_USERNAME set to: $($Username.Substring(0, [Math]::Min(5, $Username.Length)))..." -ForegroundColor Green
Write-Host "✓ BROWSERSTACK_ACCESS_KEY set to: $($AccessKey.Substring(0, [Math]::Min(5, $AccessKey.Length)))..." -ForegroundColor Green
Write-Host "✓ MAVEN_OPTS configured with system properties" -ForegroundColor Green

# Verify credentials by testing API connection
Write-Host "`nTesting BrowserStack API connection..." -ForegroundColor Yellow
try {
    $base64Auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${Username}:${AccessKey}"))
    $headers = @{ Authorization = "Basic $base64Auth" }
    $response = Invoke-RestMethod -Uri "https://api.browserstack.com/automate/plan.json" -Headers $headers -Method GET
    
    Write-Host "✓ API Connection successful!" -ForegroundColor Green
    Write-Host "  Plan: $($response.parallel_sessions_max_allowed) parallel sessions" -ForegroundColor Cyan
    Write-Host "  Running: $($response.parallel_sessions_running) sessions" -ForegroundColor Cyan
    Write-Host "  Queued: $($response.queued_sessions) sessions" -ForegroundColor Cyan
} catch {
    Write-Host "✗ API Connection failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Please verify your credentials are correct" -ForegroundColor Yellow
}

Write-Host "`n=====================================================" -ForegroundColor Green
Write-Host "Credentials are now set for this PowerShell session." -ForegroundColor Green
Write-Host "You can now run Maven tests with:" -ForegroundColor Yellow
Write-Host "  mvn clean test -Pbrowserstack" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Green
