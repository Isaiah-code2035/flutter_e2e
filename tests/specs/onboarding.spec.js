const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');

test.describe('Team Builder Balancer Registration Tests', () => {
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

  test('should verify elements and complete registration flow', async ({ page }) => {
    // Wait for the page to be fully loaded
    await flutterHelper.waitForNetworkIdle();

    // Verify onboarding page elements
    const onboardingText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    
    // Verify welcome text
    expect(onboardingText).toContain('Welcome to Team Building Balancer');
    expect(onboardingText).toContain('Get Started');
    
    // Click "Get Started" button
    await flutterHelper.tap(720, 800);
    await page.waitForTimeout(1000);

    // Verify authentication page elements
    const authText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    
    // Verify auth options
    expect(authText).toContain('Sign In');
    expect(authText).toContain('Register');

    // Click "Register" button
    await flutterHelper.tap(720, 750);
    await page.waitForTimeout(1000);

    // Verify registration form elements
    const registrationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    
    // Verify form labels and buttons
    expect(registrationText).toContain('Email');
    expect(registrationText).toContain('Password');
    expect(registrationText).toContain('Confirm Password');
    expect(registrationText).toContain('Register');

    // Fill in registration form
    // Click email input field
    await flutterHelper.tap(720, 400);
    await flutterHelper.typeText('test@example.com');
    
    // Click password input field
    await flutterHelper.tap(720, 500);
    await flutterHelper.typeText('Test123!@#');
    
    // Click confirm password field
    await flutterHelper.tap(720, 600);
    await flutterHelper.typeText('Test123!@#');
    
    // Click Register button
    await flutterHelper.tap(720, 700);
    await page.waitForTimeout(2000);

    // Verify successful registration or next step
    const afterRegistrationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });

    // Verify either success message or redirection
    // Adjust these expectations based on actual application behavior
    expect(afterRegistrationText).toMatch(/Success|Dashboard|Welcome/);
  });
});
