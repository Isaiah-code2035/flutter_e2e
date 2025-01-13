const BasePage = require('./base.page');

class AuthPage extends BasePage {
    constructor(page, flutterHelper) {
        super(page, flutterHelper);
        
        this.elements = {
            registerButton: { x: 720, y: 750 },
            emailField: { x: 720, y: 400 },
            passwordField: { x: 720, y: 500 },
            confirmPasswordField: { x: 720, y: 600 },
            submitButton: { x: 720, y: 700 }
        };

        this.text = {
            signIn: 'Sign In',
            register: 'Register'
        };
    }

    async register(email, password) {
        // Click register button
        await this.flutterHelper.tap(
            this.elements.registerButton.x,
            this.elements.registerButton.y
        );
        await this.waitForTimeout(1000);

        // Fill email
        await this.flutterHelper.tap(
            this.elements.emailField.x,
            this.elements.emailField.y
        );
        await this.flutterHelper.typeText(email);

        // Fill password
        await this.flutterHelper.tap(
            this.elements.passwordField.x,
            this.elements.passwordField.y
        );
        await this.flutterHelper.typeText(password);

        // Confirm password
        await this.flutterHelper.tap(
            this.elements.confirmPasswordField.x,
            this.elements.confirmPasswordField.y
        );
        await this.flutterHelper.typeText(password);

        // Submit
        await this.flutterHelper.tap(
            this.elements.submitButton.x,
            this.elements.submitButton.y
        );
        await this.waitForTimeout(2000);
    }

    async verifyAuthPage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.signIn) && 
               pageText.includes(this.text.register);
    }
}

module.exports = AuthPage;
