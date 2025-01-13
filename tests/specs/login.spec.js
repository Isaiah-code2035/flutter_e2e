const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');
const config = require('../helpers/coordinates.config');

test.describe('Team Builder Balancer Login Tests', () => {
  let flutterHelper;

  test.beforeEach(async ({ page }, testInfo) => {
    console.log(`Starting test: ${testInfo.title}`);
    
    // Increase default timeout
    test.setTimeout(60000);
    
    // Initialize helper
    flutterHelper = new FlutterHelper(page);

    // Navigate to the application
    await page.goto(config.url, {
      waitUntil: 'networkidle',
      timeout: 30000
    });
    
    try {
      // Wait for Flutter to initialize
      await flutterHelper.waitForFlutter();
      console.log('Flutter initialized successfully');
    } catch (error) {
      console.error('Error waiting for Flutter:', error);
      // Take a screenshot on initialization failure
      await page.screenshot({ path: `flutter-init-error-${Date.now()}.png` });
      throw error;
    }
  });

  test('should be able to input credentials and sign in', async ({ page }) => {
    // Add delays between actions
    await page.waitForTimeout(1000);
    
    // Click email field and type
    await flutterHelper.tap(config.coordinates.email.x, config.coordinates.email.y);
    await flutterHelper.typeText('test@example.com');
    
    await page.waitForTimeout(500);
    
    // Click password field and type
    await flutterHelper.tap(config.coordinates.password.x, config.coordinates.password.y);
    await flutterHelper.typeText('password123');
    
    await page.waitForTimeout(500);
    
    // Click sign in button
    await flutterHelper.tap(config.coordinates.signIn.x, config.coordinates.signIn.y);
    
    // Wait for navigation or response
    await flutterHelper.waitForNetworkIdle();
  });
});





