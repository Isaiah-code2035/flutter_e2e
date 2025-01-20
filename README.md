# Flutter TeamBuilder E2E Test Automation

This repository contains end-to-end tests for the Flutter TeamBuilder application using Sikuli for image-based automation. The framework is designed to work with Flutter applications where traditional DOM-based selectors are not accessible.

## Prerequisites

### Required Software
- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher
- Chrome browser
- Sikuli library
- Flutter SDK (for running the application)

### Installation Steps

1. **Install Java JDK:**
   ```bash
   # Check Java version
   java -version
   
   # Install Java if needed (using Homebrew on macOS)
   brew install openjdk@11
   ```

2. **Install Maven:**
   ```bash
   # Check Maven version
   mvn -version
   
   # Install Maven if needed
   brew install maven
   ```

3. **Install Chrome:**
   ```bash
   # Install Chrome browser if needed
   brew install --cask google-chrome
   ```

4. **Clone Repository:**
   ```bash
   git clone https://github.com/Isaiah-code2035/flutter_e2e.git
   cd flutter_e2e
   ```

5. **Install Dependencies:**
   ```bash
   mvn clean install
   ```

## Project Structure

```
flutter_e2e/
├── src/
│   └── test/
│       ├── java/com/teambuilder/
│       │   ├── pages/
│       │   │   ├── BasePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── OnboardingPage.java
│       │   │   ├── RegistrationPage.java
│       │   │   ├── SportSelectionPage.java
│       │   │   └── TeamCreationPage.java
│       │   └── tests/
│       └── resources/
│           └── images/
│               ├── login/
│               ├── onboarding/
│               ├── registration/
│               ├── sports/
│               └── team/
├── scripts/
│   └── run_tests.sh
├── docs/
│   ├── BUG_REPORT.md
│   └── TEST_EXECUTION.md
└── README.md
```

## Running Tests

### Quick Start
1. **Run Tests in Headless Mode:**
   ```bash
   ./scripts/run_tests.sh --headless
   ```

2. **Run Tests in GUI Mode:**
   ```bash
   ./scripts/run_tests.sh --gui
   ```

### Using Maven Directly

1. **Run All Tests:**
   ```bash
   mvn clean test
   ```

2. **Run Tests with Specific Profile:**
   ```bash
   # Run in headless mode
   mvn clean test -Dheadless=true

   # Run in GUI mode
   mvn clean test -Dheadless=false
   ```

3. **Run Specific Test Class:**
   ```bash
   # Run single test class
   mvn clean test -Dtest=OnboardingTest

   # Run multiple test classes
   mvn clean test -Dtest=OnboardingTest,LoginTest
   ```

4. **Run with Different Configurations:**
   ```bash
   # Run with retry on failure
   mvn clean test -Dsurefire.rerunFailingTestsCount=2


   # Run with all options combined
   mvn clean test -Dheadless=true -Dtest=OnboardingTest -Dsurefire.rerunFailingTestsCount=2 -DreportDirectory="custom-reports"
   ```

5. **Skip Tests:**
   ```bash
   mvn clean install -DskipTests
   ```

### Test Reports
- Reports are generated in `test-output/[timestamp]/`
- Each run creates a new directory with:
  - HTML test report
  - Detailed logs

## Test Coverage

### 1. Onboarding Flow
- Welcome screen verification
- Get Started navigation
- Multi-step onboarding process
- Skip functionality

### 2. Authentication
- Login with email/password
- Form validation
- Error handling

### 3. Sport Selection
- Multiple sport selection
- Selection validation
- Navigation flow

### 4. Team Creation
- Team size configuration
- Player number validation
- Success/error scenarios

## Framework Features

### 1. Image-Based Recognition
- Uses Sikuli for reliable UI element detection
- Adaptive similarity thresholds (0.3f - 0.5f)
- Automatic retry mechanism

### 2. Robust Error Handling
- Detailed logging
- Screenshot capture on failure
- Multiple similarity threshold attempts

### 3. Visual Debugging
- Element highlighting
- Match score logging
- Failure screenshots

## Known Issues and Solutions

For a detailed list of known issues and their solutions, see [BUG_REPORT.md](docs/BUG_REPORT.md).

### Common Issues:
1. **Pattern Matching Failures**
   - Update reference images
   - Adjust similarity thresholds
   - Check screen resolution

2. **Test Environment Issues**
   - Verify Flutter app is running
   - Check Chrome installation
   - Confirm Java/Maven setup

## Contributing

1. Update reference images when UI changes
2. Maintain consistent screen resolution
3. Follow the established page object pattern
4. Add detailed logging for new features

## Additional Documentation

- [Test Execution Guide](docs/TEST_EXECUTION.md)
- [Bug Report](docs/BUG_REPORT.md)