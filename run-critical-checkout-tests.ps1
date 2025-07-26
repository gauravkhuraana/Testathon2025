#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Run Critical Checkout End-to-End Tests for testathon.live
.DESCRIPTION
    Executes the most critical checkout test scenarios across multiple browsers and devices.
    These tests cover complete purchase flows that are critical for business revenue.
.PARAMETER Mode
    Execution mode: 'all', 'smoke', 'critical', 'mobile', 'desktop'
.PARAMETER Browser
    Specific browser: 'chrome', 'firefox', 'safari', 'edge'
.PARAMETER Environment
    Test environment: 'browserstack', 'local'
.EXAMPLE
    .\run-critical-checkout-tests.ps1 -Mode all
.EXAMPLE
    .\run-critical-checkout-tests.ps1 -Mode smoke -Browser chrome
#>

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("all", "smoke", "critical", "mobile", "desktop", "interactive")]
    [string]$Mode = "all",
    
    [Parameter(Mandatory=$false)]
    [ValidateSet("chrome", "firefox", "safari", "edge")]
    [string]$Browser,
    
    [Parameter(Mandatory=$false)]
    [ValidateSet("browserstack", "local")]
    [string]$Environment = "browserstack"
)

# Color functions for enhanced output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    } else {
        $input | Write-Output
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Success { Write-ColorOutput Green $args }
function Write-Warning { Write-ColorOutput Yellow $args }
function Write-Error { Write-ColorOutput Red $args }
function Write-Info { Write-ColorOutput Cyan $args }

# Banner
Write-Host ""
Write-Success "🚀 CRITICAL CHECKOUT END-TO-END TESTS - testathon.live"
Write-Success "=================================================="
Write-Info "Environment: $Environment"
Write-Info "Mode: $Mode"
if ($Browser) { Write-Info "Browser: $Browser" }
Write-Host ""

# Load environment variables if using BrowserStack
if ($Environment -eq "browserstack") {
    Write-Info "🔧 Loading BrowserStack credentials..."
    if (Test-Path ".\load-env.ps1") {
        . .\load-env.ps1
    }
    
    if (-not $env:BROWSERSTACK_USERNAME -or -not $env:BROWSERSTACK_ACCESS_KEY) {
        Write-Error "❌ BrowserStack credentials not found!"
        Write-Error "Please run: .\set-browserstack-credentials.ps1"
        exit 1
    }
    Write-Success "✅ BrowserStack credentials loaded"
}

# Test execution based on mode
switch ($Mode) {
    "all" {
        Write-Info "🎯 Running ALL Critical Checkout Tests..."
        $testFile = "testng-critical-checkout-endtoend.xml"
    }
    "smoke" {
        Write-Info "💨 Running SMOKE Critical Checkout Tests..."
        $groups = "critical,smoke"
    }
    "critical" {
        Write-Info "🔥 Running CRITICAL Priority Tests..."
        $groups = "critical"
    }
    "mobile" {
        Write-Info "📱 Running MOBILE Checkout Tests..."
        $testClass = "tests.CriticalCheckoutEndToEndTest"
        $testMethods = "testCrossDeviceCheckoutCompatibility"
    }
    "desktop" {
        Write-Info "🖥️ Running DESKTOP Checkout Tests..."
        $testClass = "tests.CriticalCheckoutEndToEndTest" 
        $testMethods = "testCompletePurchaseFlow,testHighValueCartCheckout"
    }
    "interactive" {
        Write-Info "🤔 Interactive Mode - Choose your test:"
        Write-Host "1. Complete Purchase Flow (Priority 1)"
        Write-Host "2. Cross-Device Checkout (Priority 2)" 
        Write-Host "3. High-Value Cart Checkout (Priority 3)"
        Write-Host "4. All Critical Tests"
        
        $choice = Read-Host "Enter choice (1-4)"
        switch ($choice) {
            "1" { $testMethods = "testCompletePurchaseFlow" }
            "2" { $testMethods = "testCrossDeviceCheckoutCompatibility" }
            "3" { $testMethods = "testHighValueCartCheckout" }
            "4" { $testFile = "testng-critical-checkout-endtoend.xml" }
            default { Write-Error "Invalid choice"; exit 1 }
        }
        $testClass = "tests.CriticalCheckoutEndToEndTest"
    }
}

# Build Maven command
$mvnCmd = "mvn clean test"

if ($Environment -eq "browserstack") {
    $mvnCmd += " -Penvironment=browserstack"
} else {
    $mvnCmd += " -Penvironment=local"
    if ($Browser) {
        $mvnCmd += " -Dbrowser=$Browser"
    } else {
        $mvnCmd += " -Dbrowser=chrome"
    }
}

# Add test specification
if ($testFile) {
    $mvnCmd += " -DsuiteXmlFile=$testFile"
} elseif ($groups) {
    $mvnCmd += " -Dgroups=$groups"
    if ($testClass) {
        $mvnCmd += " -Dtest=$testClass"
    }
} elseif ($testClass -and $testMethods) {
    $mvnCmd += " -Dtest=${testClass}#$testMethods"
}

# Execute tests
Write-Info "🚀 Executing: $mvnCmd"
Write-Host ""

$startTime = Get-Date

try {
    Invoke-Expression $mvnCmd
    $exitCode = $LASTEXITCODE
} catch {
    Write-Error "❌ Test execution failed: $_"
    exit 1
}

$endTime = Get-Date
$duration = $endTime - $startTime

Write-Host ""
Write-Host "=============================================="

if ($exitCode -eq 0) {
    Write-Success "🎉 CRITICAL CHECKOUT TESTS COMPLETED SUCCESSFULLY!"
    Write-Success "✅ All critical business flows validated"
    Write-Success "✅ Cross-browser compatibility confirmed"
    Write-Success "✅ Mobile responsiveness verified"
} else {
    Write-Error "❌ CRITICAL CHECKOUT TESTS FAILED!"
    Write-Error "⚠️  Business-critical issues detected"
    Write-Error "⚠️  Immediate attention required"
}

Write-Info "⏱️  Total execution time: $($duration.ToString('mm\:ss'))"
Write-Info "📊 Test reports available in:"
Write-Info "   - target/surefire-reports/"
Write-Info "   - test-output/extent-reports/"

if ($Environment -eq "browserstack") {
    Write-Info "🌐 BrowserStack Dashboard: https://automate.browserstack.com/"
}

Write-Host ""

# Open reports if requested
$openReports = Read-Host "📋 Open test reports? (y/N)"
if ($openReports -eq "y" -or $openReports -eq "Y") {
    if (Test-Path "test-output\extent-reports") {
        $latestReport = Get-ChildItem "test-output\extent-reports\*.html" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
        if ($latestReport) {
            Start-Process $latestReport.FullName
        }
    }
    
    if (Test-Path "target\surefire-reports\index.html") {
        Start-Process "target\surefire-reports\index.html"
    }
}

exit $exitCode
