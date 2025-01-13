const BasePage = require('./base.page');

class OnboardingPage extends BasePage {
    constructor(page, flutterHelper) {
        super(page, flutterHelper);
        
        // Element coordinates
        this.elements = {
            getStartedButton: { x: 720, y: 800 }
        };

        // Expected text content
        this.text = {
            welcome: 'Welcome to Team Building Balancer',
            getStarted: 'Get Started'
        };
    }

    async navigateToPage() {
        await this.page.goto('/', {
            waitUntil: 'networkidle',
            timeout: 30000
        });
        await this.waitForPageLoad();
    }

    async clickGetStarted() {
        await this.flutterHelper.tap(
            this.elements.getStartedButton.x,
            this.elements.getStartedButton.y
        );
        await this.waitForTimeout(1000);
    }

    async verifyOnboardingPage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.welcome) && 
               pageText.includes(this.text.getStarted);
    }
}

module.exports = OnboardingPage;
