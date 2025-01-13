const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');

test.describe('Team Builder Balancer - Team Creation Tests', () => {
  let flutterHelper;

  test.beforeEach(async ({ page }, testInfo) => {
    console.log(`Starting test: ${testInfo.title}`);
    
    // Initialize helper
    flutterHelper = new FlutterHelper(page);

    // Navigate to the application and complete prerequisites
    await test.step('Complete prerequisites', async () => {
      await page.goto('/', {
        waitUntil: 'networkidle',
        timeout: 30000
      });
      
      await flutterHelper.waitForFlutter();
      
      // Complete registration and sport selection
      await flutterHelper.tap(720, 800); // Get Started
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 750); // Register
      await page.waitForTimeout(1000);
      
      // Fill registration
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText(`test${Date.now()}@example.com`);
      
      await flutterHelper.tap(720, 500);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 600);
      await flutterHelper.typeText('Test123!@#');
      
      await flutterHelper.tap(720, 700); // Register button
      await page.waitForTimeout(2000);
      
      // Select sport (e.g., Basketball)
      await flutterHelper.tap(720, 400);
      await page.waitForTimeout(1000);
      
      // Continue to team creation
      await flutterHelper.tap(720, 800);
      await page.waitForTimeout(2000);
    });
  });

  test('should create team without adding new players', async ({ page }) => {
    // Verify we're on team creation page
    const teamPageText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(teamPageText).toContain('Create Team');

    // Fill team details
    await flutterHelper.tap(720, 300); // Team name field
    await flutterHelper.typeText('Test Team Alpha');
    
    // Select skill levels for different positions
    // Assuming skills are in a grid or list
    await flutterHelper.tap(600, 400); // Position 1 skill
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(600, 500); // Position 2 skill
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(600, 600); // Position 3 skill
    await page.waitForTimeout(500);

    // Create team without adding players
    await flutterHelper.tap(720, 800); // Create Team button
    await page.waitForTimeout(2000);

    // Verify team creation success
    const successText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(successText).toMatch(/success|created|dashboard/i);
  });

  test('should create team with new players', async ({ page }) => {
    // Verify team creation page
    const teamPageText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(teamPageText).toContain('Create Team');

    // Fill team details
    await flutterHelper.tap(720, 300); // Team name field
    await flutterHelper.typeText('Test Team Beta');
    
    // Select skill levels
    await flutterHelper.tap(600, 400); // Position 1 skill
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(600, 500); // Position 2 skill
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(600, 600); // Position 3 skill
    await page.waitForTimeout(500);

    // Add new player
    await flutterHelper.tap(720, 650); // Add Player button
    await page.waitForTimeout(1000);

    // Fill player details
    await flutterHelper.tap(720, 400); // Player name field
    await flutterHelper.typeText('John Doe');
    
    // Select player position/role
    await flutterHelper.tap(720, 500);
    await page.waitForTimeout(500);
    
    // Select player skill level
    await flutterHelper.tap(720, 600);
    await page.waitForTimeout(500);
    
    // Save player
    await flutterHelper.tap(720, 700); // Save Player button
    await page.waitForTimeout(1000);

    // Add second player
    await flutterHelper.tap(720, 650); // Add Player button
    await page.waitForTimeout(1000);

    // Fill second player details
    await flutterHelper.tap(720, 400);
    await flutterHelper.typeText('Jane Smith');
    
    await flutterHelper.tap(720, 500); // Position
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(720, 600); // Skill level
    await page.waitForTimeout(500);
    
    await flutterHelper.tap(720, 700); // Save Player
    await page.waitForTimeout(1000);

    // Create team with players
    await flutterHelper.tap(720, 800); // Create Team button
    await page.waitForTimeout(2000);

    // Verify team creation with players
    const finalText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(finalText).toMatch(/success|created|dashboard/i);
    expect(finalText).toContain('John Doe');
    expect(finalText).toContain('Jane Smith');
  });

  test('should validate required fields when creating team', async ({ page }) => {
    // Try to create team without filling required fields
    await flutterHelper.tap(720, 800); // Create Team button
    await page.waitForTimeout(1000);

    // Verify validation messages
    const validationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(validationText).toMatch(/required|fill|complete/i);
  });

  test('should validate player details when adding new player', async ({ page }) => {
    // Fill team name
    await flutterHelper.tap(720, 300);
    await flutterHelper.typeText('Test Team Gamma');

    // Click add player without filling details
    await flutterHelper.tap(720, 650); // Add Player button
    await page.waitForTimeout(1000);

    // Try to save player without details
    await flutterHelper.tap(720, 700); // Save Player button
    await page.waitForTimeout(500);

    // Verify validation messages
    const validationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(validationText).toMatch(/required|name|position|skill/i);
  });
});
