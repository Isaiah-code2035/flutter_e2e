package com.teambuilder.tests;

import com.teambuilder.pages.OnboardingPage;
import org.testng.annotations.*;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import java.util.HashMap;
import java.util.Map;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class OnboardingTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(OnboardingTest.class);
    private WebDriver driver;
    private OnboardingPage onboardingPage;

    @BeforeMethod
    public void setUp() throws Exception {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Window settings
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1440,900");
        options.addArguments("--window-position=0,0");
        
        // Disable features that might interfere with rendering
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        
        // Disable animations
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.images", 1);
        prefs.put("profile.default_content_settings.images", 1);
        options.setExperimentalOption("prefs", prefs);
        
        driver = new ChromeDriver(options);
        
        // Set window size and position programmatically as well
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1440, 900));
        
        // Navigate to the application
        driver.get(BaseTest.ONBOARDING_URL);
        logger.info("Navigated to onboarding page");
        
        // Bring Chrome window to front using Robot
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        Thread.sleep(1000); // Wait for window to come to front
        
        // Initialize page object
        onboardingPage = new OnboardingPage(driver);
        
        // Wait for page to load
        waitForPageToLoad();
        logger.info("Page loaded successfully");
        
        // Add extra delay to ensure everything is visible
        Thread.sleep(3000);
    }

    @Test(description = "Test complete onboarding flow")
    public void testCompleteOnboardingFlow() throws Exception {
        logger.info("Starting test: Complete onboarding flow");
        performLogin();
        navigateToOnboardingPage();
        
        // Verify welcome screen is displayed
        Assert.assertTrue(onboardingPage.isWelcomeScreenDisplayed(), 
            "Welcome screen should be displayed");
        
        // Complete onboarding
        onboardingPage.completeOnboarding();
    }

    @Test(description = "Test skip onboarding flow")
    public void testSkipOnboardingFlow() throws Exception {
        logger.info("Starting test: Skip onboarding flow");
        performLogin();
        navigateToOnboardingPage();
        
        // Verify welcome screen is displayed
        Assert.assertTrue(onboardingPage.isWelcomeScreenDisplayed(), 
            "Welcome screen should be displayed");
        
        // Skip onboarding
        onboardingPage.skipOnboarding();
    }

    @Test(description = "Test get started navigation")
    public void testGetStartedNavigation() throws Exception {
        logger.info("Starting test: Get started navigation");
        performLogin();
        navigateToOnboardingPage();
        
        // Verify welcome screen is displayed
        Assert.assertTrue(onboardingPage.isWelcomeScreenDisplayed(), 
            "Welcome screen should be displayed");
        
        // Click get started and verify navigation
        onboardingPage.clickGetStartedAndWaitForNavigation();
    }

    private void waitForPageToLoad() {
        logger.info("Waiting for page to load");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
