#!/bin/bash

# Testathon 2025 - Login Tests Execution Script
# This script runs comprehensive login test scenarios for testathon.live

echo "=========================================="
echo "Testathon 2025 - Login Tests Execution"
echo "=========================================="
echo ""

# Set environment variables
export ENVIRONMENT="browserstack"
export BROWSER="chrome"

# Function to run specific test suite
run_test_suite() {
    local suite_name=$1
    local suite_file=$2
    
    echo "Running $suite_name..."
    echo "----------------------------------------"
    
    mvn clean test \
        -Penvironment=$ENVIRONMENT \
        -Dbrowser=$BROWSER \
        -DsuiteXmlFile=$suite_file \
        -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn
    
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        echo "✅ $suite_name - PASSED"
    else
        echo "❌ $suite_name - FAILED"
    fi
    
    echo ""
    return $exit_code
}

# Function to run all login tests
run_all_login_tests() {
    echo "Running All Login Test Scenarios..."
    echo "=========================================="
    
    local total_tests=0
    local passed_tests=0
    
    # Test Suite 1: Login Smoke Tests
    ((total_tests++))
    if run_test_suite "Login Smoke Tests" "testng-login-scenarios.xml"; then
        ((passed_tests++))
    fi
    
    # Test Suite 2: Login Data-Driven Tests  
    ((total_tests++))
    echo "Running Login Data-Driven Tests..."
    echo "----------------------------------------"
    if mvn clean test \
        -Penvironment=$ENVIRONMENT \
        -Dbrowser=$BROWSER \
        -Dtest=LoginDataDrivenTest \
        -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn; then
        echo "✅ Login Data-Driven Tests - PASSED"
        ((passed_tests++))
    else
        echo "❌ Login Data-Driven Tests - FAILED"
    fi
    echo ""
    
    # Test Suite 3: Individual Login Test
    ((total_tests++))
    echo "Running Individual Login Tests..."
    echo "----------------------------------------"
    if mvn clean test \
        -Penvironment=$ENVIRONMENT \
        -Dbrowser=$BROWSER \
        -Dtest=LoginTest \
        -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn; then
        echo "✅ Individual Login Tests - PASSED"
        ((passed_tests++))
    else
        echo "❌ Individual Login Tests - FAILED"
    fi
    echo ""
    
    # Summary
    echo "=========================================="
    echo "Test Execution Summary"
    echo "=========================================="
    echo "Total Test Suites: $total_tests"
    echo "Passed: $passed_tests"
    echo "Failed: $((total_tests - passed_tests))"
    echo ""
    
    if [ $passed_tests -eq $total_tests ]; then
        echo "🎉 ALL LOGIN TESTS PASSED!"
        return 0
    else
        echo "⚠️  Some tests failed. Check logs for details."
        return 1
    fi
}

# Function to run specific login test scenarios
run_specific_scenarios() {
    echo "Available Login Test Scenarios:"
    echo "1. Smoke Tests (Quick validation)"
    echo "2. Regression Tests (Comprehensive)"
    echo "3. Data-Driven Tests (Multiple users)"
    echo "4. All Tests"
    echo ""
    
    read -p "Select scenario (1-4): " choice
    
    case $choice in
        1)
            mvn clean test \
                -Penvironment=$ENVIRONMENT \
                -Dbrowser=$BROWSER \
                -Dgroups=smoke \
                -Dtest=LoginTest
            ;;
        2)
            mvn clean test \
                -Penvironment=$ENVIRONMENT \
                -Dbrowser=$BROWSER \
                -Dgroups=regression \
                -Dtest=LoginTest
            ;;
        3)
            mvn clean test \
                -Penvironment=$ENVIRONMENT \
                -Dbrowser=$BROWSER \
                -Dtest=LoginDataDrivenTest
            ;;
        4)
            run_all_login_tests
            ;;
        *)
            echo "Invalid choice. Running smoke tests by default."
            mvn clean test \
                -Penvironment=$ENVIRONMENT \
                -Dbrowser=$BROWSER \
                -Dgroups=smoke \
                -Dtest=LoginTest
            ;;
    esac
}

# Main execution
if [ "$1" == "--all" ]; then
    run_all_login_tests
elif [ "$1" == "--interactive" ]; then
    run_specific_scenarios
else
    echo "Usage:"
    echo "  $0 --all          # Run all login test scenarios"
    echo "  $0 --interactive  # Interactive mode to select scenarios"
    echo "  $0                # Show this help"
    echo ""
    echo "Environment Variables:"
    echo "  ENVIRONMENT=$ENVIRONMENT"
    echo "  BROWSER=$BROWSER"
    echo ""
    echo "Test Reports will be available in:"
    echo "  - target/surefire-reports/"
    echo "  - test-output/extent-reports/"
fi
