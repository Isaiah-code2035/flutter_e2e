package com.teambuilder.tests;

import com.teambuilder.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    // WebDriver
    protected ChromeDriver driver;
    
    // Page Objects
    protected LoginPage loginPage;
    protected SportSelectionPage sportSelectionPage;
    protected TeamCreationPage teamCreationPage;
    protected OnboardingPage onboardingPage;
    protected RegistrationPage registrationPage;
    
    // Logger
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // Application URLs
    protected static final String BASE_URL = "https://team-building-balancer.web.app";
    protected static final String APP_URL = BASE_URL + "/signin";
    protected static final String LOGIN_URL = BASE_URL + "/signin";
    protected static final String ONBOARDING_URL = BASE_URL + "/onboarding";
    protected static final String REGISTRATION_URL = BASE_URL + "/signup";
    protected static final String DASHBOARD_URL = BASE_URL + "/dashboard";
    protected static final String SPORT_SELECTION_URL = BASE_URL + "/sports";
    protected static final String TEAM_CREATION_URL = BASE_URL + "/team-creation";
    
    // Test Credentials
    protected static final String TEST_EMAIL = "oluwabamiselanipekun@gmail.com";
    protected static final String TEST_PASSWORD = "Bamise123";
    
    // Browser Configuration
    protected static final int WINDOW_WIDTH = 1280;
    protected static final int WINDOW_HEIGHT = 800;

    @BeforeMethod
    public void setUp() throws Exception {
        logger.info("Setting up test environment");
        
        try {
            // Setup ChromeDriver using WebDriverManager
            WebDriverManager.chromedriver().setup();
            
            ChromeOptions options = new ChromeOptions();
            
            // Window settings
            options.addArguments("--window-size=" + WINDOW_WIDTH + "," + WINDOW_HEIGHT);
            options.addArguments("--window-position=0,0");
            
            // Performance optimizations
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
            
            // Initialize WebDriver
            driver = new ChromeDriver(options);
            
            // Initialize page objects
            loginPage = new LoginPage(driver);
            sportSelectionPage = new SportSelectionPage(driver);
            teamCreationPage = new TeamCreationPage(driver);
            onboardingPage = new OnboardingPage(driver);
            registrationPage = new RegistrationPage(driver);
            
            // Navigate to login page by default
            navigateToLoginPage();
            
        } catch (Exception e) {
            logger.error("Failed to set up test environment", e);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed successfully");
        }
    }

    protected void performLogin() throws Exception {
        logger.info("Performing login with test credentials");
        loginPage.performLogin(TEST_EMAIL, TEST_PASSWORD);
    }

    protected void selectSport(String sport) throws Exception {
        logger.info("Selecting sport: {}", sport);
        sportSelectionPage.selectSport(sport);
    }

    protected void navigateToLoginPage() {
        logger.info("Navigating to login page");
        driver.get(LOGIN_URL);
    }

    protected void navigateToOnboardingPage() {
        logger.info("Navigating to onboarding page");
        driver.get(ONBOARDING_URL);
    }

    protected void navigateToRegistrationPage() {
        logger.info("Navigating to registration page");
        driver.get(REGISTRATION_URL);
    }

    protected void navigateToSportSelectionPage() {
        logger.info("Navigating to sport selection page");
        driver.get(SPORT_SELECTION_URL);
    }

    protected void navigateToTeamCreationPage() {
        logger.info("Navigating to team creation page");
        driver.get(TEAM_CREATION_URL);
    }
}
