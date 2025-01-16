const BasePage = require('./base.page');
const SikulixHelper = require('../helpers/sikulix.helper');
const path = require('path');

class OnboardingPage extends BasePage {
    constructor(page, flutterHelper) {
        super(page, flutterHelper);
        this.sikulix = new SikulixHelper();
        
        // Image paths for Sikulix
        this.images = {
            getStartedButton: 'onboarding/get-started-button.png',
            welcomeText: 'onboarding/welcome-text.png'
        };

        // Expected text content (keeping as fallback)
        this.text = {
            welcome: 'Welcome to Team Building Balancer',
            getStarted: 'Get Started'
        };
    }

    async navigateToPage() {
        await this.page.goto('https://team-building-balancer.web.app', {
            waitUntil: 'networkidle',
            timeout: 30000
        });
        await this.waitForPageLoad();
        await this.sikulix.initialize();
    }

    async clickGetStarted() {
        try {
            // Try image recognition first
            const clicked = await this.sikulix.findAndClick(this.images.getStartedButton);
            if (!clicked) {
                throw new Error('Image recognition failed');
            }
        } catch (error) {
            console.log('Image recognition failed, trying semantic selector');
            // Try semantic selector
            try {
                await this.page.evaluate(() => {
                    const button = document.querySelector('flt-semantics-placeholder[aria-label="Get Started"]');
                    if (button) button.click();
                });
            } catch (secondError) {
                console.log('Semantic selector failed, falling back to coordinates');
                // Final fallback to coordinate-based click
                await this.flutterHelper.tap(720, 800);
            }
        }
        await this.waitForTimeout(1000);
    }

    async verifyOnboardingPage() {
        try {
            // Try image recognition first
            const welcomeExists = await this.sikulix.imageExists(this.images.welcomeText);
            const buttonExists = await this.sikulix.imageExists(this.images.getStartedButton);
            
            if (welcomeExists && buttonExists) {
                return true;
            }
        } catch (error) {
            console.log('Image recognition verification failed, trying alternatives');
        }

        // Try semantic selectors as fallback
        const hasSemanticElements = await this.page.evaluate(() => {
            const welcome = document.querySelector('flt-semantics-placeholder[aria-label="Welcome to Team Building Balancer"]');
            const button = document.querySelector('flt-semantics-placeholder[aria-label="Get Started"]');
            return Boolean(welcome && button);
        });

        if (!hasSemanticElements) {
            // Final fallback to text content check
            const pageText = await this.getPageText();
            return pageText.includes(this.text.welcome) && 
                   pageText.includes(this.text.getStarted);
        }
        return true;
    }

    async waitForGetStartedButton() {
        try {
            // Try waiting for image
            return await this.sikulix.waitForImage(this.images.getStartedButton, 10);
        } catch (error) {
            console.log('Image wait failed, falling back to semantic selector');
            // Fallback to semantic selector wait
            return await this.page.waitForFunction(() => {
                return document.querySelector('flt-semantics-placeholder[aria-label="Get Started"]') !== null;
            }, { timeout: 5000 });
        }
    }

    async capturePageElements() {
        // Helper method to capture reference images for Sikulix
        const screenshotOptions = { path: path.join(this.sikulix.imagesPath, this.images.getStartedButton) };
        await this.page.screenshot(screenshotOptions);
        
        // Capture welcome text area
        screenshotOptions.path = path.join(this.sikulix.imagesPath, this.images.welcomeText);
        await this.page.screenshot(screenshotOptions);
    }
}

module.exports = OnboardingPage;
