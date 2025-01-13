class FlutterHelper {
  constructor(page) {
    this.page = page;
  }

  // Improved Flutter initialization check
  async waitForFlutter(timeout = 60000) {
    await this.page.waitForFunction(() => {
      // Check for Flutter-specific elements
      return document.querySelector('flt-glass-pane') !== null;
    }, { timeout });
  }

  // Helper to click at specific coordinates with retry logic
  async tap(x, y, retries = 3) {
    for (let i = 0; i < retries; i++) {
      try {
        await this.page.mouse.click(x, y);
        break;
      } catch (error) {
        if (i === retries - 1) throw error;
        await this.page.waitForTimeout(1000);
      }
    }
  }

  // Enhanced type text with pre-click wait
  async typeText(text, delay = 100) {
    await this.page.waitForTimeout(500); // Wait for input focus
    for (const char of text) {
      await this.page.keyboard.type(char);
      await this.page.waitForTimeout(delay);
    }
  }

  // Enhanced network idle wait
  async waitForNetworkIdle() {
    try {
      await this.page.waitForLoadState('networkidle', { timeout: 10000 });
    } catch (error) {
      console.log('Network did not reach idle state, continuing...');
    }
  }
}

module.exports = FlutterHelper;