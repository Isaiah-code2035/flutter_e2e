package com.teambuilder.tests;

import com.teambuilder.pages.RegistrationPage;
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
import java.util.UUID;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import java.time.Duration;
import org.apache.commons.lang3.RandomStringUtils;

public class RegistrationTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationTest.class);
    private WebDriver driver;
    private RegistrationPage registrationPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws Exception {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Window settings - set exact size and position
        options.addArguments("--window-size=1280,800");  // Slightly smaller window
        options.addArguments("--window-position=0,0");
        
        // Disable features that might interfere with rendering
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        
        // Disable animations and notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.images", 1);
        prefs.put("profile.default_content_settings.images", 1);
        options.setExperimentalOption("prefs", prefs);
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Navigate to the application
        driver.get(BaseTest.LOGIN_URL);
        logger.info("Navigated to signin page");
        
        // Bring Chrome window to front using Robot
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        Thread.sleep(1000); // Wait for window to come to front
        
        // Initialize page object
        registrationPage = new RegistrationPage(driver);
        
        // Add extra delay to ensure everything is loaded and rendered
        Thread.sleep(10000);
    }

    @Test(description = "Test user registration with valid data")
    public void testRegistration() throws Exception {
        // Generate random name and email
        String randomSuffix = RandomStringUtils.randomAlphabetic(4);
        String name = "Test User " + randomSuffix;
        String email = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String password = "Test123!@#";
        
        logger.info("Starting registration test with name: {} and email: {}", name, email);
        
        try {
            // Perform registration
            registrationPage.register(name, email, password);
            
            // Wait for registration to complete and verify redirection
            logger.info("Waiting for registration to complete and verify redirection");
            wait.until(ExpectedConditions.urlContains(BaseTest.LOGIN_URL));
            
            // Add verification delay
            Thread.sleep(5000);
            
            // Log success
            logger.info("Registration completed successfully");
        } catch (Exception e) {
            logger.error("Registration test failed: {}", e.getMessage());
            
            // Add delay before quitting to see what happened
            Thread.sleep(10000);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            // Add delay before quitting
            Thread.sleep(5000);
            
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            logger.error("Error in tearDown: {}", e.getMessage());
        }
    }
}
