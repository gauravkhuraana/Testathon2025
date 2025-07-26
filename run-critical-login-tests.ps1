#!/usr/bin/env pwsh

<#
.SYNOPSIS
    Critical Login Test Execution Script for testathon.live
    
.DESCRIPTION
    Executes critical login test scenarios across multiple browsers and devices
    with comprehensive reporting and error handling
    
.PARAMETER Mode
    Execution mode: 'quick' (Chrome only), 'full' (all browsers), 'mobile' (mobile devices)
    
.PARAMETER Environment
    Target environment: 'browserstack' or 'local'
    
.PARAMETER Report
    Generate detailed HTML report: 'yes' or 'no'
    
.EXAMPLE
    .\run-critical-login-tests.ps1 -Mode quick -Environment browserstack -Report yes
#>

param(
    [Parameter(Position=0)]
    [ValidateSet("quick", "full", "mobile", "interactive")]
    [string]$Mode = "interactive",
    
    [Parameter(Position=1)]
    [ValidateSet("browserstack", "local")]
    [string]$Environment = "browserstack",
    
    [Parameter(Position=2)]
    [ValidateSet("yes", "no")]
    [string]$Report = "yes"
)

# Color functions for better output
function Write-ColorOutput([string]$Message, [string]$Color = "White") {
    switch ($Color) {
        "Red"     { Write-Host $Message -ForegroundColor Red }
        "Green"   { Write-Host $Message -ForegroundColor Green }
        "Yellow"  { Write-Host $Message -ForegroundColor Yellow }
        "Blue"    { Write-Host $Message -ForegroundColor Blue }
        "Magenta" { Write-Host $Message -ForegroundColor Magenta }
        "Cyan"    { Write-Host $Message -ForegroundColor Cyan }
        default   { Write-Host $Message }
    }
}

function Show-Banner {
    Write-ColorOutput "🔐============================================🔐" "Cyan"
    Write-ColorOutput "   CRITICAL LOGIN TESTS - testathon.live     " "Cyan"
    Write-ColorOutput "   Focus: Authentication, Cross-Browser,     " "Cyan"
    Write-ColorOutput "          Dropdown Validation, Session Mgmt  " "Cyan"
    Write-ColorOutput "🔐============================================🔐" "Cyan"
    Write-Host ""
}

function Show-TestScenarios {
    Write-ColorOutput "📋 CRITICAL LOGIN TEST SCENARIOS:" "Yellow"
    Write-ColorOutput "   1️⃣  Critical Login Flow (Priority 1)" "White"
    Write-ColorOutput "       • Authentication gateway testing" "Gray"
    Write-ColorOutput "       • React Select dropdown validation" "Gray"
    Write-ColorOutput "       • Session persistence verification" "Gray"
    Write-ColorOutput "" "White"
    Write-ColorOutput "   2️⃣  Cross-User Authentication (Priority 2)" "White"
    Write-ColorOutput "       • Multiple user types validation" "Gray"
    Write-ColorOutput "       • User data integrity testing" "Gray"
    Write-ColorOutput "       • User-specific behavior validation" "Gray"
    Write-ColorOutput "" "White"
    Write-ColorOutput "   3️⃣  Login Error Handling (Priority 3)" "White"
    Write-ColorOutput "       • Invalid credentials testing" "Gray"
    Write-ColorOutput "       • Form validation edge cases" "Gray"
    Write-ColorOutput "       • Security vulnerability checks" "Gray"
    Write-Host ""
}

function Get-InteractiveMode {
    Write-ColorOutput "🎯 SELECT TEST EXECUTION MODE:" "Yellow"
    Write-ColorOutput "1. Quick Tests    - Chrome Windows only (fastest)" "White"
    Write-ColorOutput "2. Full Tests     - All desktop browsers (comprehensive)" "White"
    Write-ColorOutput "3. Mobile Tests   - Mobile devices only (touch/responsive)" "White"
    Write-ColorOutput "4. Complete Suite - All browsers + mobile (thorough)" "White"
    Write-Host ""
    
    do {
        $choice = Read-Host "Enter your choice (1-4)"
        switch ($choice) {
            "1" { return "quick" }
            "2" { return "full" }
            "3" { return "mobile" }
            "4" { return "complete" }
            default { 
                Write-ColorOutput "❌ Invalid choice. Please enter 1, 2, 3, or 4." "Red"
            }
        }
    } while ($true)
}

function Get-InteractiveEnvironment {
    Write-ColorOutput "🌐 SELECT TARGET ENVIRONMENT:" "Yellow"
    Write-ColorOutput "1. BrowserStack  - Cross-browser cloud testing (recommended)" "White"
    Write-ColorOutput "2. Local         - Local Chrome testing only" "White"
    Write-Host ""
    
    do {
        $choice = Read-Host "Enter your choice (1-2)"
        switch ($choice) {
            "1" { return "browserstack" }
            "2" { return "local" }
            default { 
                Write-ColorOutput "❌ Invalid choice. Please enter 1 or 2." "Red"
            }
        }
    } while ($true)
}

function Test-Prerequisites {
    Write-ColorOutput "🔍 Checking prerequisites..." "Blue"
    
    # Check if Maven is available
    try {
        $mavenVersion = mvn -version 2>$null
        if ($mavenVersion) {
            Write-ColorOutput "✅ Maven is available" "Green"
        } else {
            throw "Maven not found"
        }
    } catch {
        Write-ColorOutput "❌ Maven is not available. Please install Maven." "Red"
        return $false
    }
    
    # Check if project can compile
    Write-ColorOutput "🔧 Testing project compilation..." "Blue"
    try {
        $compileResult = mvn clean compile -q 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-ColorOutput "✅ Project compiles successfully" "Green"
        } else {
            Write-ColorOutput "❌ Project compilation failed:" "Red"
            Write-ColorOutput $compileResult "Red"
            return $false
        }
    } catch {
        Write-ColorOutput "❌ Failed to test compilation" "Red"
        return $false
    }
    
    # Check for BrowserStack credentials if needed
    if ($Environment -eq "browserstack") {
        if (-not ($env:BROWSERSTACK_USERNAME -and $env:BROWSERSTACK_ACCESS_KEY)) {
            Write-ColorOutput "⚠️  BrowserStack credentials not found in environment variables" "Yellow"
            Write-ColorOutput "   Please run: .\set-browserstack-credentials.ps1" "Yellow"
            Write-ColorOutput "   Or set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY" "Yellow"
            
            $continue = Read-Host "Continue anyway? (y/n)"
            if ($continue -ne "y") {
                return $false
            }
        } else {
            Write-ColorOutput "✅ BrowserStack credentials found" "Green"
        }
    }
    
    return $true
}

function Get-TestNGFile($TestMode) {
    switch ($TestMode) {
        "quick"    { return "testng-critical-login.xml" }
        "full"     { return "testng-critical-login.xml" }
        "mobile"   { return "testng-critical-login.xml" }
        "complete" { return "testng-critical-login.xml" }
        default    { return "testng-critical-login.xml" }
    }
}

function Execute-Tests($TestMode, $Environment, $GenerateReport) {
    $testngFile = Get-TestNGFile $TestMode
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    
    Write-ColorOutput "🚀 STARTING CRITICAL LOGIN TESTS..." "Green"
    Write-ColorOutput "   Mode: $TestMode" "White"
    Write-ColorOutput "   Environment: $Environment" "White"
    Write-ColorOutput "   TestNG Config: $testngFile" "White"
    Write-ColorOutput "   Timestamp: $timestamp" "White"
    Write-Host ""
    
    # Build Maven command
    $mavenArgs = @(
        "clean", "test",
        "-Penvironment=$Environment",
        "-DsuiteXmlFile=src/test/resources/$testngFile"
    )
    
    # Add mode-specific parameters
    switch ($TestMode) {
        "quick" {
            $mavenArgs += "-Dgroups=critical,smoke"
            $mavenArgs += "-DparallelMode=methods"
            $mavenArgs += "-DthreadCount=2"
        }
        "full" {
            $mavenArgs += "-Dgroups=critical"
            $mavenArgs += "-DparallelMode=tests"
            $mavenArgs += "-DthreadCount=4"
        }
        "mobile" {
            $mavenArgs += "-Dgroups=critical"
            $mavenArgs += "-DparallelMode=tests"
            $mavenArgs += "-DthreadCount=2"
        }
        "complete" {
            $mavenArgs += "-Dgroups=critical,smoke,regression"
            $mavenArgs += "-DparallelMode=tests"
            $mavenArgs += "-DthreadCount=6"
        }
    }
    
    # Execute tests
    Write-ColorOutput "📝 Maven Command: mvn $($mavenArgs -join ' ')" "Blue"
    Write-Host ""
    
    $startTime = Get-Date
    try {
        & mvn @mavenArgs
        $exitCode = $LASTEXITCODE
    } catch {
        Write-ColorOutput "❌ Test execution failed with exception: $_" "Red"
        return $false
    }
    $endTime = Get-Date
    $duration = $endTime - $startTime
    
    Write-Host ""
    Write-ColorOutput "⏱️  Test execution completed in: $($duration.ToString('mm\:ss'))" "Blue"
    
    if ($exitCode -eq 0) {
        Write-ColorOutput "🎉 ALL CRITICAL LOGIN TESTS PASSED!" "Green"
        Show-SuccessMessage $TestMode $duration
    } else {
        Write-ColorOutput "❌ Some tests failed or had issues" "Red"
        Show-FailureMessage $exitCode
    }
    
    if ($GenerateReport -eq "yes") {
        Show-ReportLocations
    }
    
    return ($exitCode -eq 0)
}

function Show-SuccessMessage($TestMode, $Duration) {
    Write-Host ""
    Write-ColorOutput "🔐============================================🔐" "Green"
    Write-ColorOutput "          CRITICAL LOGIN TESTS PASSED!        " "Green"
    Write-ColorOutput "🔐============================================🔐" "Green"
    Write-ColorOutput "✅ Authentication flows validated" "Green"
    Write-ColorOutput "✅ Cross-browser compatibility confirmed" "Green"
    Write-ColorOutput "✅ Dropdown interactions working" "Green"
    Write-ColorOutput "✅ Session management verified" "Green"
    Write-ColorOutput "✅ User data integrity maintained" "Green"
    Write-ColorOutput "✅ Error handling functional" "Green"
    Write-Host ""
    Write-ColorOutput "📊 Mode: $TestMode | Duration: $($Duration.ToString('mm\:ss'))" "Cyan"
}

function Show-FailureMessage($ExitCode) {
    Write-Host ""
    Write-ColorOutput "🔐============================================🔐" "Red"
    Write-ColorOutput "          CRITICAL LOGIN TESTS FAILED         " "Red"
    Write-ColorOutput "🔐============================================🔐" "Red"
    Write-ColorOutput "❌ Some critical authentication flows failed" "Red"
    Write-ColorOutput "❌ Check logs for dropdown/browser issues" "Red"
    Write-ColorOutput "❌ Verify BrowserStack connectivity" "Red"
    Write-ColorOutput "❌ Review error messages for fixes needed" "Red"
    Write-Host ""
    Write-ColorOutput "🔧 Exit Code: $ExitCode" "Yellow"
    Write-ColorOutput "📋 Check TestNG reports for detailed results" "Yellow"
}

function Show-ReportLocations {
    Write-Host ""
    Write-ColorOutput "📊 TEST REPORTS GENERATED:" "Blue"
    Write-ColorOutput "   • TestNG Reports: target/surefire-reports/" "White"
    Write-ColorOutput "   • Extent Reports: test-output/extent-reports/" "White"
    Write-ColorOutput "   • Screenshots:    test-output/screenshots/" "White"
    Write-ColorOutput "   • BrowserStack:   https://app.browserstack.com/dashboard" "White"
    Write-Host ""
}

# Main execution
try {
    Show-Banner
    Show-TestScenarios
    
    # Get execution parameters
    if ($Mode -eq "interactive") {
        $Mode = Get-InteractiveMode
        $Environment = Get-InteractiveEnvironment
    }
    
    # Check prerequisites
    if (-not (Test-Prerequisites)) {
        Write-ColorOutput "❌ Prerequisites check failed. Exiting." "Red"
        exit 1
    }
    
    # Execute tests
    $success = Execute-Tests $Mode $Environment $Report
    
    if ($success) {
        exit 0
    } else {
        exit 1
    }
    
} catch {
    Write-ColorOutput "❌ Script execution failed: $_" "Red"
    Write-ColorOutput "🔧 Please check the error details above" "Yellow"
    exit 1
}
