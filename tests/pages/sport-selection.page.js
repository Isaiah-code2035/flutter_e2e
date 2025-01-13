const BasePage = require('./base.page');

class SportSelectionPage extends BasePage {
    constructor(page, flutterHelper) {
        super(page, flutterHelper);
        
        this.elements = {
            sportOption: { x: 720, y: 400 },
            continueButton: { x: 720, y: 800 }
        };

        this.text = {
            selectSport: 'Select a Sport',
            continue: 'Continue'
        };
    }

    async selectSport() {
        await this.flutterHelper.tap(
            this.elements.sportOption.x,
            this.elements.sportOption.y
        );
        await this.waitForTimeout(1000);
    }

    async continue() {
        await this.flutterHelper.tap(
            this.elements.continueButton.x,
            this.elements.continueButton.y
        );
        await this.waitForTimeout(2000);
    }

    async verifySportSelectionPage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.selectSport);
    }
}

module.exports = SportSelectionPage;
