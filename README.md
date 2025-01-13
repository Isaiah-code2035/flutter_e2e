# Flutter E2E Tests with Playwright

This repository contains end-to-end tests for a Flutter web application using Playwright. The tests are structured using the Page Object Model (POM) pattern for better maintainability and reusability.

## Prerequisites

- Node.js (v14 or higher)
- npm (Node Package Manager)
- A modern web browser (Chrome, Firefox, or Safari)
- Flutter web application running at https://team-building-balancer.web.app/

## Project Structure

```
flutter-e2e-tests/
├── tests/
│   ├── helpers/
│   │   ├── flutter.helper.js
│   │   └── coordinates.config.js
│   ├── pages/
│   │   ├── base.page.js
│   │   ├── onboarding.page.js
│   │   ├── auth.page.js
│   │   ├── sport-selection.page.js
│   │   └── team-creation.page.js
│   └── specs/
│       ├── onboarding.spec.js
│       ├── select-sport.spec.js
│       ├── create-team.spec.js
│       ├── create-team-no-skills.spec.js
│       └── create-team-pom.spec.js
├── package.json
├── playwright.config.js
└── README.md
```

## Installation

1. Clone the repository:
```bash
git clone https://github.com/Isaiah-code2035/flutter_e2e.git
cd flutter_e2e
```

2. Install dependencies:
```bash
# Clean install node_modules (recommended)
rm -rf node_modules package-lock.json
npm cache clean --force
npm install

# Install Playwright browsers
npx playwright install
# OR
npm run install:browsers
```

### Troubleshooting Installation

If you encounter any issues:

1. **Missing node_modules**
```bash
# Remove existing node_modules and lock file
rm -rf node_modules package-lock.json

# Clear npm cache
npm cache clean --force

# Reinstall dependencies
npm install
```

2. **Playwright Browser Installation Issues**
```bash
# Manual browser installation
npx playwright install chromium
npx playwright install firefox
npx playwright install webkit
```

3. **Permission Issues**
```bash
# If you encounter permission errors
sudo npm install -g npm@latest
npm install
```

4. **Verify Installation**
```bash
# Check if Playwright is installed correctly
npx playwright --version

# Test browser installation
npx playwright test --browser=all about:blank
```

## Running Tests

The project includes several npm scripts for running tests:

1. Run all tests in headed mode (with browser visible):
```bash
npm run test:headed
```

2. Run all tests in headless mode:
```bash
npm run test
```

3. Run tests with UI mode (Playwright's test runner UI):
```bash
npm run test:ui
```

4. View test report:
```bash
npm run report
```

5. Run only spec files:
```bash
# Run specs in headed mode
npm run test:specs

# Run specs in headless mode
npm run test:specs:headless
```

### Running Specific Test Files

To run a specific test file:
```bash
npm run test:headed tests/specs/onboarding.spec.js
```

To run multiple specific test files:
```bash
npm run test:headed tests/specs/onboarding.spec.js tests/specs/select-sport.spec.js
```

## Test Scenarios

The test suite covers the following scenarios:

1. **Onboarding Flow**
   - Welcome page verification
   - Get Started navigation

2. **Authentication**
   - Registration process
   - Form validation

3. **Sport Selection**
   - Sport selection process
   - Navigation validation

4. **Team Creation**
   - Creating team with and without skills
   - Adding players to team
   - Form validation
   - Success verification

## Page Objects

The tests use Page Object Model pattern with the following page objects:

1. **BasePage**: Common functionality for all pages
2. **OnboardingPage**: Welcome and Get Started functionality
3. **AuthPage**: Registration and authentication
4. **SportSelectionPage**: Sport selection process
5. **TeamCreationPage**: Team and player management

## Configuration

The test configuration is in `playwright.config.js`:
- Base URL: https://team-building-balancer.web.app/
- Viewport: 1440x900
- Screenshots: Only on failure
- Trace: On first retry
- Retries: 1
- Workers: 1

## Debugging

For debugging tests:

1. Use UI mode:
```bash
npm run test:ui
```

2. Run specific test with headed mode:
```bash
npm run test:headed tests/specs/create-team.spec.js
```

3. Check test reports:
```bash
npm run report
```

## Common Issues and Solutions

1. **Test Timeouts**
   - Increase timeout in `playwright.config.js`
   - Check network connectivity
   - Verify application is running

2. **Element Not Found**
   - Verify coordinates in page objects
   - Check if application layout changed
   - Ensure proper wait times

3. **Authentication Issues**
   - Clear browser cache/cookies
   - Verify test user credentials
   - Check application authentication state
