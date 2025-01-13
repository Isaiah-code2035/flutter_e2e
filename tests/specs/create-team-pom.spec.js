const { test, expect } = require('@playwright/test');
const FlutterHelper = require('../helpers/flutter.helper');
const OnboardingPage = require('../pages/onboarding.page');
const AuthPage = require('../pages/auth.page');
const SportSelectionPage = require('../pages/sport-selection.page');
const TeamCreationPage = require('../pages/team-creation.page');

test.describe('Team Builder Balancer - Team Creation Tests (POM)', () => {
    let flutterHelper;
    let onboardingPage;
    let authPage;
    let sportSelectionPage;
    let teamCreationPage;

    test.beforeEach(async ({ page }) => {
        flutterHelper = new FlutterHelper(page);
        
        // Initialize pages
        onboardingPage = new OnboardingPage(page, flutterHelper);
        authPage = new AuthPage(page, flutterHelper);
        sportSelectionPage = new SportSelectionPage(page, flutterHelper);
        teamCreationPage = new TeamCreationPage(page, flutterHelper);

        // Navigate and complete prerequisites
        await onboardingPage.navigateToPage();
        expect(await onboardingPage.verifyOnboardingPage()).toBeTruthy();
        
        await onboardingPage.clickGetStarted();
        expect(await authPage.verifyAuthPage()).toBeTruthy();
        
        await authPage.register(`test${Date.now()}@example.com`, 'Test123!@#');
        expect(await sportSelectionPage.verifySportSelectionPage()).toBeTruthy();
        
        await sportSelectionPage.selectSport();
        await sportSelectionPage.continue();
    });

    test('should create team with skills and without players', async () => {
        expect(await teamCreationPage.verifyTeamCreationPage()).toBeTruthy();

        await teamCreationPage.fillTeamName('Test Team Alpha');
        await teamCreationPage.selectSkills();
        await teamCreationPage.createTeam();

        expect(await teamCreationPage.verifySuccessMessage()).toBeTruthy();
    });

    test('should create team with skills and players', async () => {
        expect(await teamCreationPage.verifyTeamCreationPage()).toBeTruthy();

        await teamCreationPage.fillTeamName('Test Team Beta');
        await teamCreationPage.selectSkills();
        
        await teamCreationPage.addPlayer('John Doe');
        await teamCreationPage.addPlayer('Jane Smith');
        
        await teamCreationPage.createTeam();
        expect(await teamCreationPage.verifySuccessMessage()).toBeTruthy();
    });

    test('should validate required fields', async () => {
        expect(await teamCreationPage.verifyTeamCreationPage()).toBeTruthy();

        // Try to create team without any data
        await teamCreationPage.createTeam();
        expect(await teamCreationPage.verifyValidationMessage()).toBeTruthy();

        // Fill only team name and try again
        await teamCreationPage.fillTeamName('Validation Test Team');
        await teamCreationPage.createTeam();
        expect(await teamCreationPage.verifyValidationMessage()).toBeTruthy();
    });
});
