const { defineConfig } = require('@playwright/test');

module.exports = defineConfig({
  testDir: './tests/specs',
  use: {
    baseURL: 'https://team-building-balancer.web.app/#/onboarding',
    viewport: { width: 1440, height: 900 },
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },
  retries: 1,
  workers: 1,
  reporter: [
    ['html'],
    ['list']
  ]
});