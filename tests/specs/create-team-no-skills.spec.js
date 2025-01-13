const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');

test.describe('Team Builder Balancer - Team Creation Without Skills Tests', () => {
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

  test('should attempt to create team without skills and without players', async ({ page }) => {
    // Verify we're on team creation page
    const teamPageText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(teamPageText).toContain('Create Team');

    // Fill only team name
    await flutterHelper.tap(720, 300); // Team name field
    await flutterHelper.typeText('Team No Skills');
    
    // Try to create team without selecting any skills
    await flutterHelper.tap(720, 800); // Create Team button
    await page.waitForTimeout(2000);

    // Verify validation message about missing skills
    const validationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(validationText).toMatch(/select.*skills|required.*skills|missing.*skills/i);
  });

  test('should attempt to create team without skills but with new player', async ({ page }) => {
    // Verify team creation page
    const teamPageText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(teamPageText).toContain('Create Team');

    // Fill team name only
    await flutterHelper.tap(720, 300); // Team name field
    await flutterHelper.typeText('Team With Player No Skills');
    
    // Add new player without selecting team skills
    await flutterHelper.tap(720, 650); // Add Player button
    await page.waitForTimeout(1000);

    // Fill player details
    await flutterHelper.tap(720, 400); // Player name field
    await flutterHelper.typeText('John Doe');
    
    // Save player without selecting skills
    await flutterHelper.tap(720, 700); // Save Player button
    await page.waitForTimeout(1000);

    // Try to create team
    await flutterHelper.tap(720, 800); // Create Team button
    await page.waitForTimeout(2000);

    // Verify validation messages about missing skills
    const validationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(validationText).toMatch(/select.*skills|required.*skills|missing.*skills/i);
  });

  test('should verify skill selection is required for team creation', async ({ page }) => {
    // Try different combinations of missing skills
    await test.step('Verify team name without skills', async () => {
      await flutterHelper.tap(720, 300);
      await flutterHelper.typeText('Test Team Delta');
      
      await flutterHelper.tap(720, 800); // Create Team button
      await page.waitForTimeout(1000);

      const validationText = await page.evaluate(() => {
        const elements = Array.from(document.querySelectorAll('*'));
        return elements.map(el => el.textContent).join(' ');
      });
      expect(validationText).toMatch(/skills.*required|select.*skills/i);
    });

    // Try with multiple players but no skills
    await test.step('Verify multiple players without skills', async () => {
      // Add first player
      await flutterHelper.tap(720, 650);
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText('Player One');
      await flutterHelper.tap(720, 700);
      await page.waitForTimeout(1000);

      // Add second player
      await flutterHelper.tap(720, 650);
      await page.waitForTimeout(1000);
      
      await flutterHelper.tap(720, 400);
      await flutterHelper.typeText('Player Two');
      await flutterHelper.tap(720, 700);
      await page.waitForTimeout(1000);

      // Try to create team
      await flutterHelper.tap(720, 800);
      await page.waitForTimeout(1000);

      const finalValidationText = await page.evaluate(() => {
        const elements = Array.from(document.querySelectorAll('*'));
        return elements.map(el => el.textContent).join(' ');
      });
      expect(finalValidationText).toMatch(/skills.*required|select.*skills/i);
    });
  });

  test('should verify individual player skill requirements', async ({ page }) => {
    // Fill team name
    await flutterHelper.tap(720, 300);
    await flutterHelper.typeText('Team Player Skills Test');

    // Add player without selecting any skills
    await flutterHelper.tap(720, 650); // Add Player button
    await page.waitForTimeout(1000);

    // Only fill player name
    await flutterHelper.tap(720, 400);
    await flutterHelper.typeText('Skill-less Player');

    // Try to save player
    await flutterHelper.tap(720, 700);
    await page.waitForTimeout(1000);

    // Verify player skill validation
    const validationText = await page.evaluate(() => {
      const elements = Array.from(document.querySelectorAll('*'));
      return elements.map(el => el.textContent).join(' ');
    });
    expect(validationText).toMatch(/player.*skills|select.*position|skill.*required/i);
  });
});
