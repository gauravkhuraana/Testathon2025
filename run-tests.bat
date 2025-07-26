@echo off
REM BrowserStack Selenium Test Execution Script for Windows
REM This script helps execute tests with different configurations

echo ========================================
echo Selenium BrowserStack Test Framework
echo ========================================

REM Check if Maven is installed
mvn --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Check for BrowserStack credentials
if "%BROWSERSTACK_USERNAME%"=="" (
    echo WARNING: BROWSERSTACK_USERNAME not set
    echo Please set your BrowserStack credentials:
    echo set BROWSERSTACK_USERNAME=your_username
    echo set BROWSERSTACK_ACCESS_KEY=your_access_key
)

if "%BROWSERSTACK_ACCESS_KEY%"=="" (
    echo WARNING: BROWSERSTACK_ACCESS_KEY not set
    echo Please set your BrowserStack credentials:
    echo set BROWSERSTACK_USERNAME=your_username
    echo set BROWSERSTACK_ACCESS_KEY=your_access_key
)

echo.
echo Available Test Execution Options:
echo 1. Run all tests on BrowserStack
echo 2. Run smoke tests only
echo 3. Run regression tests only
echo 4. Run tests locally (Chrome)
echo 5. Run specific test class
echo 6. Clean and compile
echo 7. Exit
echo.

set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" goto run_all_browserstack
if "%choice%"=="2" goto run_smoke
if "%choice%"=="3" goto run_regression
if "%choice%"=="4" goto run_local
if "%choice%"=="5" goto run_specific
if "%choice%"=="6" goto clean_compile
if "%choice%"=="7" goto exit
goto invalid_choice

:run_all_browserstack
echo Running all tests on BrowserStack...
mvn clean test -Penvironment=browserstack
goto end

:run_smoke
echo Running smoke tests on BrowserStack...
mvn clean test -Penvironment=browserstack -Dgroups=smoke
goto end

:run_regression
echo Running regression tests on BrowserStack...
mvn clean test -Penvironment=browserstack -Dgroups=regression
goto end

:run_local
echo Running tests locally with Chrome...
mvn clean test -Penvironment=local -Dbrowser=chrome
goto end

:run_specific
echo.
echo Available test classes:
echo 1. LoginTest
echo 2. ProductTest
echo 3. CheckoutTest
echo.
set /p test_choice="Enter test class number (1-3): "

if "%test_choice%"=="1" (
    echo Running LoginTest...
    mvn clean test -Penvironment=browserstack -Dtest=LoginTest
) else if "%test_choice%"=="2" (
    echo Running ProductTest...
    mvn clean test -Penvironment=browserstack -Dtest=ProductTest
) else if "%test_choice%"=="3" (
    echo Running CheckoutTest...
    mvn clean test -Penvironment=browserstack -Dtest=CheckoutTest
) else (
    echo Invalid choice
)
goto end

:clean_compile
echo Cleaning and compiling project...
mvn clean compile
goto end

:invalid_choice
echo Invalid choice. Please select 1-7.
goto end

:exit
echo Exiting...
exit /b 0

:end
echo.
echo ========================================
echo Test execution completed!
echo Check the following for results:
echo - Console output above
echo - test-output/extent-reports/ folder for HTML reports
echo - BrowserStack dashboard for session details
echo ========================================
pause
