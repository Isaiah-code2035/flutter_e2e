package com.teambuilder.pages;

import org.sikuli.script.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import java.time.Duration;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class OnboardingPage {
    private static final Logger logger = LoggerFactory.getLogger(OnboardingPage.class);
    private final WebDriver driver;
    private final Screen screen;
    private final Pattern welcomeText;
    private final Pattern getStartedButton;
    private static final String BASE_PATH = "/Users/oluwabamise/flutter-e2e-tests";
    private static final String IMAGE_PATH = "src/test/resources/images/onboarding";
    private static final double DEFAULT_WAIT_TIMEOUT = 30.0;
    private static final double DEFAULT_SIMILARITY = 0.7;
    private static final int MAX_CLICK_ATTEMPTS = 5;
    private static final int CLICK_RETRY_INTERVAL = 5000; // 5 seconds

    public OnboardingPage(WebDriver driver) {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        
        // Initialize patterns with captured images using absolute paths
        String absolutePath = new File(BASE_PATH, IMAGE_PATH).getAbsolutePath();
        this.welcomeText = new Pattern(new File(absolutePath, "welcome-text.png").getAbsolutePath())
            .similar(DEFAULT_SIMILARITY);
        this.getStartedButton = new Pattern(new File(absolutePath, "get-started-button.png").getAbsolutePath())
            .similar(DEFAULT_SIMILARITY);
        
        logger.info("Welcome text image path: {}", welcomeText.getFilename());
        logger.info("Get started button image path: {}", getStartedButton.getFilename());
        
        // Verify that image files exist
        File welcomeFile = new File(welcomeText.getFilename());
        File buttonFile = new File(getStartedButton.getFilename());
        
        logger.info("Welcome text image exists: {}", welcomeFile.exists());
        logger.info("Get started button image exists: {}", buttonFile.exists());
        logger.info("Using similarity threshold: {}", DEFAULT_SIMILARITY);
        
        if (!welcomeFile.exists() || !buttonFile.exists()) {
            throw new RuntimeException("Required image files are missing");
        }
    }

    public boolean isWelcomeTextDisplayed() {
        try {
            logger.info("Checking if welcome text is displayed...");
            Match match = screen.exists(welcomeText, 5.0); // Use shorter timeout for checking
            boolean result = match != null;
            logger.info("Welcome text displayed: {}", result);
            if (match != null) {
                logger.info("Found at location: ({}, {}), score: {}", match.getX(), match.getY(), match.getScore());
            }
            return result;
        } catch (Exception e) {
            logger.error("Failed to check if welcome text is displayed: {}", e.getMessage());
            return false;
        }
    }

    public void clickGetStartedUntilNavigated() throws FindFailed {
        int attempts = 0;
        boolean navigated = false;

        while (!navigated && attempts < MAX_CLICK_ATTEMPTS) {
            attempts++;
            logger.info("Attempt {} to click Get Started button", attempts);

            try {
                // Find and click the button
                logger.info("Waiting for Get Started button...");
                Match match = screen.wait(getStartedButton, DEFAULT_WAIT_TIMEOUT);
                if (match != null) {
                    logger.info("Found Get Started button at location: ({}, {}), score: {}", 
                        match.getX(), match.getY(), match.getScore());
                    
                    // Move mouse to button center
                    screen.mouseMove(match.getCenter());
                    logger.info("Moved mouse to button center");
                    Thread.sleep(1000); // Wait a bit to make the hover visible
                    
                    // Click exactly 3 times with pauses
                    for (int i = 0; i < 3; i++) {
                        match.click();
                        logger.info("Click {} on Get Started button", i + 1);
                        Thread.sleep(800); // Wait 800ms between clicks
                    }
                    
                    // Move mouse away after clicks
                    screen.mouseMove(new Location(0, 0));
                    
                    // Wait longer for navigation
                    Thread.sleep(CLICK_RETRY_INTERVAL);
                    
                    // Check if we've navigated away from the welcome page
                    if (!isWelcomeTextDisplayed()) {
                        logger.info("Successfully navigated away from welcome page");
                        navigated = true;
                        break;
                    }
                    
                    logger.info("Navigation not detected, will try again");
                } else {
                    logger.error("Get Started button not found after waiting");
                    throw new FindFailed("Get Started button not found after waiting");
                }
            } catch (Exception e) {
                logger.error("Error during click attempt {}: {}", attempts, e.getMessage());
                if (attempts >= MAX_CLICK_ATTEMPTS) {
                    if (e instanceof FindFailed) {
                        throw (FindFailed) e;
                    }
                    throw new FindFailed("Failed to click Get Started button after " + MAX_CLICK_ATTEMPTS + " attempts: " + e.getMessage());
                }
            }
        }

        if (!navigated) {
            throw new FindFailed("Failed to navigate away from welcome page after " + MAX_CLICK_ATTEMPTS + " attempts");
        }
    }

    public void waitForPageToLoad() {
        try {
            logger.info("Waiting for page to load...");
            
            // First wait for the Flutter app to load using Selenium
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("flt-glass-pane")));
            logger.info("Flutter app loaded");
            
            // Add a small delay to let animations complete
            Thread.sleep(2000);
            
            // Now try to find the welcome text
            logger.info("Looking for welcome text...");
            Match match = screen.wait(welcomeText, DEFAULT_WAIT_TIMEOUT);
            if (match != null) {
                logger.info("Found welcome text at location: ({}, {}), score: {}", 
                    match.getX(), match.getY(), match.getScore());
            } else {
                logger.error("Welcome text not found after waiting");
                throw new RuntimeException("Welcome text not found after waiting");
            }
        } catch (Exception e) {
            logger.error("Page failed to load: {}", e.getMessage());
            throw new RuntimeException("Onboarding page did not load properly: " + e.getMessage());
        }
    }
}
