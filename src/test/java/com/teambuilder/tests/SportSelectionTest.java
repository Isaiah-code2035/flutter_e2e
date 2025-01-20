package com.teambuilder.tests;

import com.teambuilder.pages.LoginPage;
import com.teambuilder.pages.SportSelectionPage;
import org.sikuli.script.FindFailed;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;

public class SportSelectionTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(SportSelectionTest.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private SportSelectionPage sportSelectionPage;

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        logger.info("Setting up ChromeDriver");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1280,800");
        options.addArguments("--window-position=0,0");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.managed_default_content_settings.images", 1);
        prefs.put("profile.default_content_settings.images", 1);
        options.setExperimentalOption("prefs", prefs);
        
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        sportSelectionPage = new SportSelectionPage(driver);
        
        // Navigate to the application
        driver.get("https://team-building-balancer.web.app/#/signin");
        logger.info("Navigated to application URL");
        
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_ALT);
            Thread.sleep(1000);
        } catch (AWTException e) {
            logger.error("Failed to bring window to front", e);
        }
    }

    @Test(description = "Test basketball sport selection")
    public void testBasketballSelection() throws Exception {
        logger.info("Starting test: Basketball selection");
        try {
            logger.info("Performing login with test credentials");
            performLogin();
            logger.info("Selecting basketball sport");
            sportSelectionPage.selectSport("basketball");
            logger.info("Verifying basketball selection");
            sportSelectionPage.verifyBasketballSelected();
            Assert.assertTrue(sportSelectionPage.isSportSelectionPageDisplayed(), "Sport selection page should be displayed");
        } catch (Exception e) {
            logger.error("Failed to select basketball", e);
            Assert.fail("Failed to select basketball: " + e.getMessage());
        }
    }

    @Test(description = "Test football sport selection")
    public void testFootballSelection() throws Exception {
        logger.info("Starting test: Football selection");
        try {
            logger.info("Performing login with test credentials");
            performLogin();
            logger.info("Selecting football sport");
            sportSelectionPage.selectSport("football");
            logger.info("Verifying football selection");
            sportSelectionPage.verifyFootballSelected();
            Assert.assertTrue(sportSelectionPage.isSportSelectionPageDisplayed(), "Sport selection page should be displayed");
        } catch (Exception e) {
            logger.error("Failed to select football", e);
            Assert.fail("Failed to select football: " + e.getMessage());
        }
    }

    @Test(description = "Test volleyball sport selection")
    public void testVolleyballSelection() throws Exception {
        logger.info("Starting test: Volleyball selection");
        try {
            logger.info("Performing login with test credentials");
            performLogin();
            logger.info("Selecting volleyball sport");
            sportSelectionPage.selectSport("volleyball");
            logger.info("Verifying volleyball selection");
            sportSelectionPage.verifyVolleyballSelected();
            Assert.assertTrue(sportSelectionPage.isSportSelectionPageDisplayed(), "Sport selection page should be displayed");
        } catch (Exception e) {
            logger.error("Failed to select volleyball", e);
            Assert.fail("Failed to select volleyball: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
