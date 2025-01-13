class BasePage {
    constructor(page, flutterHelper) {
        this.page = page;
        this.flutterHelper = flutterHelper;
    }

    async waitForPageLoad() {
        await this.flutterHelper.waitForFlutter();
        await this.flutterHelper.waitForNetworkIdle();
    }

    async getPageText() {
        return await this.page.evaluate(() => {
            const elements = Array.from(document.querySelectorAll('*'));
            return elements.map(el => el.textContent).join(' ');
        });
    }

    async waitForTimeout(ms) {
        await this.page.waitForTimeout(ms);
    }
}

module.exports = BasePage;
