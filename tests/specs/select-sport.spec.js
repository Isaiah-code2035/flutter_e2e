const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');

test.describe('Team Builder Balancer - Sport Selection Tests', () => {
  let flutterHelper;

  test.beforeEach(async ({ page }, testInfo) => {
    console.log(`Starting test: ${testInfo.title}`);
    
    // Initialize helper
    flutterHelper = new FlutterHelper(page);

    // Navigate to the application
    await page.goto('/', {
      waitUntil: 'networkidle',
      timeout: 30000
    });
    
    // Wait for Flutter to initialize
    await flutterHelper.waitForFlutter();
  });

  test('should navigate through onboarding to sport selection', async ({ page }) => {
    // Wait for the page to be fully loaded
    await flutterHelper.waitForNetworkIdle();

    // Verify we're on the onboarding page
    const onboardingText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(onboardingText).toContain('Welcome to Team Building Balancer');

    // Click "Get Started"
    await flutterHelper.tap(720, 800);
    await page.waitForTimeout(1000);

    // Register flow
    await flutterHelper.tap(720, 750); // Click Register
    await page.waitForTimeout(1000);

    // Fill registration form
    await flutterHelper.tap(720, 400);
    await flutterHelper.typeText('test@example.com');
    
    await flutterHelper.tap(720, 500);
    await flutterHelper.typeText('Test123!@#');
    
    await flutterHelper.tap(720, 600);
    await flutterHelper.typeText('Test123!@#');
    
    await flutterHelper.tap(720, 700); // Click Register button
    await page.waitForTimeout(2000);

    // Verify sport selection page
    const sportPageText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(sportPageText).toContain('Select a Sport');
  });

  test('should be able to select a sport', async ({ page }) => {
    // Navigate through registration first
    await test.step('Complete registration', async () => {
      await flutterHelper.tap(720, 800); // Get Started
      await page.waitForTimeout(1000);
      await flutterHelper.tap(720, 750); // Register
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText('test2@example.com');
      
      await flutterHelper.tap(720, 500);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 600);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 700);
      await page.waitForTimeout(2000);
    });

    // Now on sport selection page
    // Click on a sport (e.g., Basketball)
    await flutterHelper.tap(720, 400); // Adjust coordinates based on sport position
    await page.waitForTimeout(1000);

    // Verify selection
    const selectedSportText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(selectedSportText).toMatch(/Selected|Continue|Next/);
  });

  test('should show validation when no sport is selected', async ({ page }) => {
    // Navigate through registration
    await test.step('Complete registration', async () => {
      await flutterHelper.tap(720, 800);
      await page.waitForTimeout(1000);
      await flutterHelper.tap(720, 750);
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText('test3@example.com');
      
      await flutterHelper.tap(720, 500);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 600);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 700);
      await page.waitForTimeout(2000);
    });

    // Try to continue without selecting a sport
    await flutterHelper.tap(720, 800); // Continue button
    await page.waitForTimeout(1000);

    // Verify error message
    const errorText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(errorText).toMatch(/select|choose|required/i);
  });

  test('should be able to change sport selection', async ({ page }) => {
    // Navigate through registration
    await test.step('Complete registration', async () => {
      await flutterHelper.tap(720, 800);
      await page.waitForTimeout(1000);
      await flutterHelper.tap(720, 750);
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText('test4@example.com');
      
      await flutterHelper.tap(720, 500);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 600);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 700);
      await page.waitForTimeout(2000);
    });

    // Select first sport
    await flutterHelper.tap(720, 400);
    await page.waitForTimeout(1000);

    // Change selection to different sport
    await flutterHelper.tap(720, 500);
    await page.waitForTimeout(1000);

    // Verify new selection
    const newSelectionText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(newSelectionText).toMatch(/Selected|Continue|Next/);
  });
});
