package com.teambuilder.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LoginPage {
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String IMAGE_PATH = "src/test/resources/images/login";
    private static final String IMAGE_DIR = BASE_PATH + "/" + IMAGE_PATH + "/";
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static final int DEFAULT_WAIT_TIMEOUT = 30;
    private static final int LOGIN_TIMEOUT = 30000; // 30 seconds for login verification

    private final WebDriver driver;
    private final Screen screen;
    private final Pattern emailInput;
    private final Pattern passwordInput;
    private final Pattern loginButton;
    private final Pattern teamBalancerHeader;

    public LoginPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);

        // Initialize patterns with images and similarity thresholds
        this.emailInput = new Pattern(IMAGE_DIR + "emailinputlogin.png").similar(0.4f);
        this.passwordInput = new Pattern(IMAGE_DIR + "password-input.png").similar(0.4f);
        this.loginButton = new Pattern(IMAGE_DIR + "login-button.png").similar(0.4f);
        this.teamBalancerHeader = new Pattern(IMAGE_DIR + "team-balancer-text.png").similar(0.3f);

        // Verify images exist
        verifyImageExists(IMAGE_DIR + "emailinputlogin.png", "emailinputlogin.png");
        verifyImageExists(IMAGE_DIR + "password-input.png", "password-input.png");
        verifyImageExists(IMAGE_DIR + "login-button.png", "login-button.png");
        verifyImageExists(IMAGE_DIR + "team-balancer-text.png", "team-balancer-text.png");
    }

    private void verifyImageExists(String imagePath, String imageName) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("Image file not found: " + imageName);
        }
        logger.info("Image file exists: " + imageName);
    }

    public void enterEmail(String email) throws FindFailed {
        logger.info("Entering email: {}", email);
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                logger.info("Looking for email input with similarity {}", threshold);
                Pattern emailInputPattern = emailInput.similar(threshold);
                Match match = screen.wait(emailInputPattern, 10);
                logger.info("Found email input at location: ({}, {})", match.getX(), match.getY());
                match.highlight(1);
                match.click();
                screen.type(email);
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw e;
                }
            }
        }
    }

    public void enterPassword(String password) throws FindFailed {
        logger.info("Entering password");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                logger.info("Looking for password input with similarity {}", threshold);
                Pattern passwordPattern = passwordInput.similar(threshold);
                Match match = screen.wait(passwordPattern, 10);
                match.highlight(1);
                match.click();
                screen.type(password);
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw e;
                }
            }
        }
    }

    public void clickLoginButton() throws FindFailed {
        logger.info("Clicking login button");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                logger.info("Looking for login button with similarity {}", threshold);
                Pattern loginPattern = loginButton.similar(threshold);
                Match match = screen.wait(loginPattern, 10);
                match.highlight(1);
                match.click();
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw e;
                }
            }
        }
    }

    public void performLogin(String email, String password) throws FindFailed {
        logger.info("Starting login process");
        String currentUrl = driver.getCurrentUrl();
        logger.info("URL before login: {}", currentUrl);
        
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        
        // Wait for login to complete by checking for the Team Balancer header
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < LOGIN_TIMEOUT) {
            try {
                verifyDashboardHeader();
                logger.info("Login successful - Team Balancer header found");
                return;
            } catch (FindFailed e) {
                // Continue waiting
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Login wait interrupted", ie);
                }
            }
        }
        throw new RuntimeException("Login failed - Team Balancer header not found after " + LOGIN_TIMEOUT + "ms");
    }

    public void verifyDashboardHeader() throws FindFailed {
        logger.info("Verifying Team Balancer header is visible");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                Pattern headerPattern = teamBalancerHeader.similar(threshold);
                Match headerMatch = screen.wait(headerPattern, 5);
                headerMatch.highlight(1);
                logger.info("Found Team Balancer header at location: ({}, {})", headerMatch.getX(), headerMatch.getY());
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw e;
                }
            }
        }
    }
}