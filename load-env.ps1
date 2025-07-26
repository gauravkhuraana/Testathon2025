# Load BrowserStack credentials from .env file
# Run this script before executing tests to set environment variables

if (-not (Test-Path ".env")) {
    Write-Host ".env file not found!" -ForegroundColor Red
    Write-Host "Please create .env file with your BrowserStack credentials" -ForegroundColor Yellow
    Write-Host "Example:" -ForegroundColor Yellow
    Write-Host "BROWSERSTACK_USERNAME=your_username" -ForegroundColor Green
    Write-Host "BROWSERSTACK_ACCESS_KEY=your_access_key" -ForegroundColor Green
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Loading BrowserStack credentials from .env file..." -ForegroundColor Yellow

Get-Content ".env" | ForEach-Object {
    if ($_ -match "^([^#=]+)=(.*)$") {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [Environment]::SetEnvironmentVariable($name, $value, "Process")
        Write-Host "Set $name" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "Environment variables loaded successfully!" -ForegroundColor Green
Write-Host "You can now run your tests with:" -ForegroundColor Yellow
Write-Host "  mvn clean test -Penvironment=browserstack" -ForegroundColor Cyan
Write-Host "  or" -ForegroundColor Yellow
Write-Host "  mvn clean test -Penvironment=local" -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to continue"
