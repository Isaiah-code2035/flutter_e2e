# Running TeamBuilder E2E Tests

This document explains how to run the TeamBuilder E2E tests in both headless and GUI modes.

## Prerequisites

1. Java 11 or higher
2. Maven
3. Chrome browser installed
4. Flutter application running locally

## Test Execution Modes

### GUI Mode
GUI mode runs the tests with visible browser windows, useful for:
- Test development
- Debugging
- Visual verification

To run tests in GUI mode:
```bash
./scripts/run_tests.sh --gui
```

### Headless Mode
Headless mode runs tests without visible browser windows, ideal for:
- CI/CD pipelines
- Faster execution
- Server environments

To run tests in headless mode:
```bash
./scripts/run_tests.sh --help
```

## Test Reports

Test reports are automatically generated in the `test-output` directory with the following structure:
```
test-output/
└── YYYYMMDD_HHMMSS/
    ├── index.html
    ├── screenshots/
    └── logs/
```

Each test run creates a new timestamped directory containing:
- HTML test report
- Screenshots of failures
- Detailed test logs

## Common Issues and Solutions

### Sikuli Pattern Matching Issues
If tests fail due to pattern matching:
1. Verify the screen resolution matches the test environment
2. Check if the application UI has changed
3. Update the reference images in `src/test/resources/images`

### Browser Driver Issues
If browser initialization fails:
1. Ensure Chrome is installed
2. Update ChromeDriver if needed
3. Check system PATH configuration

## Configuration

The test execution can be customized through various parameters:

### Maven Properties
- `headless`: Controls browser mode (true/false)
- `test`: Specifies test classes to run
- `failIfNoTests`: Fails build if no tests found
- `surefire.rerunFailingTestsCount`: Number of retries for failed tests

### Environment Variables
- `SIKULI_ROBOT_MODE`: Enables Sikuli robot mode
- `SIKULI_IMAGE_PATH`: Path to reference images

## Best Practices

1. **Reference Images**
   - Keep reference images up to date
   - Use consistent screen resolution
   - Maintain separate images for different environments

2. **Test Execution**
   - Use GUI mode for development
   - Use headless mode for CI/CD
   - Always check test reports for failures

3. **Maintenance**
   - Regularly update reference images
   - Clean up old test reports
   - Monitor test execution times

## Getting Help

For issues or questions:
1. Check the test reports in `test-output`
2. Review logs in the test report directory
3. Check the [BUG_REPORT.md](./BUG_REPORT.md) for known issues

## Command Reference

```bash
# Show help
./scripts/run_tests.sh --help

# Run in GUI mode
./scripts/run_tests.sh --gui

# Run in headless mode
./scripts/run_tests.sh --headless
```
