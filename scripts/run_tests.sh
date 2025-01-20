#!/bin/bash

# Function to display script usage
show_usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  --headless    Run tests in headless mode"
    echo "  --gui         Run tests in GUI mode"
    echo "  --help        Display this help message"
}

# Function to run tests
run_tests() {
    local mode=$1
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local report_dir="test-output/${timestamp}"
    
    # Create report directory
    mkdir -p "$report_dir"
    
    # Set common Maven options
    local mvn_opts="-Dtest=com.teambuilder.tests.** -DfailIfNoTests=false"
    
    if [ "$mode" == "headless" ]; then
        echo "Running tests in headless mode..."
        mvn_opts="$mvn_opts -Dheadless=true"
    else
        echo "Running tests in GUI mode..."
        mvn_opts="$mvn_opts -Dheadless=false"
    fi
    
    # Add Sikuli options for both modes
    export SIKULI_ROBOT_MODE=true
    export SIKULI_IMAGE_PATH="src/test/resources/images"
    
    # Run the tests with specified options
    mvn clean test $mvn_opts -Dsurefire.rerunFailingTestsCount=2 \
        -DreportFormat=html -DreportDirectory="$report_dir"
    
    # Check if tests were successful
    if [ $? -eq 0 ]; then
        echo "Tests completed successfully!"
        echo "Test report available at: $report_dir"
    else
        echo "Some tests failed. Check the report for details at: $report_dir"
        exit 1
    fi
}

# Parse command line arguments
case "$1" in
    --headless)
        run_tests "headless"
        ;;
    --gui)
        run_tests "gui"
        ;;
    --help)
        show_usage
        ;;
    *)
        echo "Error: Invalid option"
        show_usage
        exit 1
        ;;
esac
