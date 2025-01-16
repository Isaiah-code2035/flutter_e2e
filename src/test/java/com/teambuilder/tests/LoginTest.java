package com.teambuilder.tests;

import com.teambuilder.pages.LoginPage;
import com.teambuilder.pages.RegistrationPage;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @BeforeClass
    public void setup() throws Exception {
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
        
        // Initialize pages
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);
    }

    @Test(description = "Test user login with valid credentials")
    public void testLogin() {
        try {
            // Navigate to signin page
            driver.get("https://team-building-balancer.web.app/#/signin");
            logger.info("Navigated to signin page");
            
            // Bring Chrome window to front using Robot
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_ALT);
            Thread.sleep(1000); // Wait for window to come to front
            
            // Add extra delay to ensure everything is loaded and rendered
            Thread.sleep(10000);

            // Attempt to login
            String testEmail = "oluwabamiseolanipekun@gmail.com";
            String testPassword = "Bamise123";
            logger.info("Attempting to login with email: {}", testEmail);

            loginPage.login(testEmail, testPassword);

        } catch (Exception e) {
            logger.error("Exception occurred during login: {}", e.getMessage());
            throw new RuntimeException("Failed to login", e);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
