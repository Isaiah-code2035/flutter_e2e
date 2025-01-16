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

public class OnboardingTest {
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
        driver.get("https://team-building-balancer.web.app/#/onboarding");
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
        onboardingPage.waitForPageToLoad();
        logger.info("Page loaded successfully");
        
        // Add extra delay to ensure everything is visible
        Thread.sleep(3000);
    }

    @Test
    public void testOnboardingFlow() throws Exception {
        // Verify welcome text is displayed
        logger.info("Checking for welcome text");
        Assert.assertTrue(onboardingPage.isWelcomeTextDisplayed(), 
            "Welcome text should be displayed");
        
        // Add delay before clicking
        Thread.sleep(3000);

        // Click Get Started button until navigation occurs
        logger.info("Clicking Get Started button until navigation");
        onboardingPage.clickGetStartedUntilNavigated();
        logger.info("Successfully navigated to next page");
        
        // Wait for 1 minute (60000 milliseconds)
        logger.info("Waiting for 1 minute...");
        Thread.sleep(60000);
        logger.info("1-minute wait completed");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
