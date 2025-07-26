#!/bin/bash
# BrowserStack Selenium Test Execution Script for macOS/Linux

echo "========================================"
echo "Selenium BrowserStack Test Framework"
echo "========================================"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven and try again"
    exit 1
fi

# Check for BrowserStack credentials
if [[ -z "${BROWSERSTACK_USERNAME}" ]]; then
    echo "WARNING: BROWSERSTACK_USERNAME not set"
    echo "Please set your BrowserStack credentials:"
    echo "export BROWSERSTACK_USERNAME=your_username"
    echo "export BROWSERSTACK_ACCESS_KEY=your_access_key"
fi

if [[ -z "${BROWSERSTACK_ACCESS_KEY}" ]]; then
    echo "WARNING: BROWSERSTACK_ACCESS_KEY not set"
    echo "Please set your BrowserStack credentials:"
    echo "export BROWSERSTACK_USERNAME=your_username"
    echo "export BROWSERSTACK_ACCESS_KEY=your_access_key"
fi

echo ""
echo "Available Test Execution Options:"
echo "1. Run all tests on BrowserStack"
echo "2. Run smoke tests only"
echo "3. Run regression tests only"
echo "4. Run tests locally (Chrome)"
echo "5. Run specific test class"
echo "6. Clean and compile"
echo "7. Exit"
echo ""

read -p "Enter your choice (1-7): " choice

case $choice in
    1)
        echo "Running all tests on BrowserStack..."
        mvn clean test -Penvironment=browserstack
        ;;
    2)
        echo "Running smoke tests on BrowserStack..."
        mvn clean test -Penvironment=browserstack -Dgroups=smoke
        ;;
    3)
        echo "Running regression tests on BrowserStack..."
        mvn clean test -Penvironment=browserstack -Dgroups=regression
        ;;
    4)
        echo "Running tests locally with Chrome..."
        mvn clean test -Penvironment=local -Dbrowser=chrome
        ;;
    5)
        echo ""
        echo "Available test classes:"
        echo "1. LoginTest"
        echo "2. ProductTest"
        echo "3. CheckoutTest"
        echo ""
        read -p "Enter test class number (1-3): " test_choice
        
        case $test_choice in
            1)
                echo "Running LoginTest..."
                mvn clean test -Penvironment=browserstack -Dtest=LoginTest
                ;;
            2)
                echo "Running ProductTest..."
                mvn clean test -Penvironment=browserstack -Dtest=ProductTest
                ;;
            3)
                echo "Running CheckoutTest..."
                mvn clean test -Penvironment=browserstack -Dtest=CheckoutTest
                ;;
            *)
                echo "Invalid choice"
                ;;
        esac
        ;;
    6)
        echo "Cleaning and compiling project..."
        mvn clean compile
        ;;
    7)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid choice. Please select 1-7."
        ;;
esac

echo ""
echo "========================================"
echo "Test execution completed!"
echo "Check the following for results:"
echo "- Console output above"
echo "- test-output/extent-reports/ folder for HTML reports"
echo "- BrowserStack dashboard for session details"
echo "========================================"
