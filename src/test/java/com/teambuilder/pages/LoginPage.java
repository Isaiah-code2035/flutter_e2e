package com.teambuilder.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;

public class LoginPage {
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String REG_IMAGE_PATH = "src/test/resources/images/registration";
    private static final String IMAGE_PATH = "src/test/resources/images/login";
    private static final String IMAGE_DIR = BASE_PATH + "/" + IMAGE_PATH + "/";
    private static final String REG_IMAGE_DIR = BASE_PATH + "/" + REG_IMAGE_PATH + "/";
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static final int DEFAULT_WAIT_TIMEOUT = 30;
    private static final int LOGIN_TIMEOUT = 30000; // 30 seconds for login verification
    private static final int HEADER_WAIT_TIMEOUT = 15; // 15 seconds for header to appear

    private final WebDriver driver;
    private final Screen screen;
    private final WebDriverWait wait;
    private final Pattern emailInput;
    private final Pattern passwordInput;
    private final Pattern loginButton;
    private final Pattern teamBalancerHeader;

    public LoginPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));

        // Initialize patterns with images and similarity thresholds
        this.emailInput = new Pattern(REG_IMAGE_DIR + "emailinputlogin.png").similar(0.4f);
        this.passwordInput = new Pattern(REG_IMAGE_DIR + "password-input.png").similar(0.4f);
        this.loginButton = new Pattern(REG_IMAGE_DIR + "login-button.png").similar(0.4f);
        this.teamBalancerHeader = new Pattern(IMAGE_DIR + "team-balancer-text.png").similar(0.3f); // Lower threshold for text matching

        // Verify images exist
        verifyImageExists(REG_IMAGE_DIR + "emailinputlogin.png", "emailinputlogin.png");
        verifyImageExists(REG_IMAGE_DIR + "password-input.png", "password-input.png");
        verifyImageExists(REG_IMAGE_DIR + "login-button.png", "login-button.png");
        verifyImageExists(IMAGE_DIR + "team-balancer-text.png", "team-balancer-text.png");
    }

    private void verifyImageExists(String imagePath, String imageName) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("Image file not found: " + imageName);
        }
        logger.info("Image file exists: " + imageName);
    }

    public void enterEmail(String email) throws InterruptedException, FindFailed, AWTException {
        logger.info("Entering email: {}", email);
        Thread.sleep(2000); // Increased initial wait
        
        // Find and click email input using image recognition with retries
        logger.info("Looking for email input field...");
        boolean clicked = false;
        Match emailMatch = null;
        
        for (float similarity : new float[]{0.4f, 0.5f, 0.6f, 0.7f}) {
            try {
                Pattern pattern = emailInput.similar(similarity);
                logger.info("Trying similarity threshold: {}", similarity);
                emailMatch = screen.wait(pattern, 5);
                logger.info("Found email input at location: ({}, {})", emailMatch.getX(), emailMatch.getY());
                emailMatch.highlight(1); // Highlight for visual feedback
                
                // Click multiple times to ensure focus
                emailMatch.click();
                Thread.sleep(500);
                emailMatch.click();
                Thread.sleep(500);
                
                clicked = true;
                break;
            } catch (FindFailed e) {
                logger.warn("Failed to find email input with similarity {}: {}", similarity, e.getMessage());
            }
        }
        
        if (!clicked || emailMatch == null) {
            throw new FindFailed("Could not find email input after all attempts");
        }
        
        // Clear any existing text using backspace
        Robot robot = new Robot();
        for (int i = 0; i < 30; i++) {
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            Thread.sleep(50);
        }
        
        Thread.sleep(1000);
        
        // Click again to ensure focus
        emailMatch.click();
        Thread.sleep(1000);
        
        // Type the email character by character
        logger.info("Typing email text: {}", email);
        for (char c : email.toCharArray()) {
            logger.info("Typing character: {}", c);
            
            if (c == '@') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '.') {
                robot.keyPress(KeyEvent.VK_PERIOD);
                robot.keyRelease(KeyEvent.VK_PERIOD);
            } else {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                if (keyCode != KeyEvent.VK_UNDEFINED) {
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else {
                    robot.keyPress(Character.toLowerCase(c));
                    robot.keyRelease(Character.toLowerCase(c));
                }
            }
            Thread.sleep(300); // Longer delay between keystrokes
        }
        
        Thread.sleep(2000);
        logger.info("Finished entering email");
    }

    public void enterPassword(String password) throws FindFailed, InterruptedException, AWTException {
        logger.info("Entering password: {}", password);
        screen.wait(passwordInput, 10);
        screen.click(passwordInput);
        Thread.sleep(2000);
        
        // Use Robot to type text
        Robot robot = new Robot();
        for (char c : password.toCharArray()) {
            logger.info("Typing character: {}", c);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c >= '0' && c <= '9') {
                // For numbers, directly press the corresponding key
                robot.keyPress(c);
                robot.keyRelease(c);
            } else if (c == '!') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '@') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
            }
            Thread.sleep(200);
        }
        logger.info("Finished entering password");
        Thread.sleep(2000);
    }

    public void clickLoginButton() throws FindFailed, InterruptedException {
        logger.info("Clicking login button");
        screen.wait(loginButton, 10);
        screen.click(loginButton);
        Thread.sleep(2000);
    }

    public void verifyDashboardHeader() throws FindFailed, InterruptedException {
        logger.info("Verifying Team Balancer header is visible");
        try {
            // Wait longer for header to appear
            Match headerMatch = screen.wait(teamBalancerHeader, HEADER_WAIT_TIMEOUT);
            headerMatch.highlight(1);
            logger.info("Found Team Balancer header at location: ({}, {})", headerMatch.getX(), headerMatch.getY());
        } catch (FindFailed e) {
            logger.error("Failed to find Team Balancer header: {}", e.getMessage());
            throw e;
        }
    }

    public void login(String email, String password) throws FindFailed, InterruptedException, AWTException {
        // Wait for page to be ready
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
            
        String beforeUrl = driver.getCurrentUrl();
        logger.info("URL before login: {}", beforeUrl);
            
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        
        // Add extra wait after clicking login button
        Thread.sleep(2000);
        
        // Check for any error messages in the page source
        String pageSource = driver.getPageSource();
        if (pageSource.contains("Invalid email") || pageSource.contains("Invalid password") || 
            pageSource.contains("Wrong password") || pageSource.contains("User not found")) {
            logger.error("Login error message found on page");
            throw new RuntimeException("Login failed - Error message found on page");
        }
        
        // Wait for URL to change and verify Team Balancer header appears
        long startTime = System.currentTimeMillis();
        long timeout = LOGIN_TIMEOUT; // Increased timeout to 30 seconds
        boolean loginSuccessful = false;
        
        while (System.currentTimeMillis() - startTime < timeout) {
            String currentUrl = driver.getCurrentUrl();
            logger.info("Current URL: {}", currentUrl);
            
            if (!currentUrl.equals(beforeUrl) && !currentUrl.contains("/signin")) {
                logger.info("URL changed, waiting for Team Balancer header...");
                try {
                    // Add extra wait before checking for header
                    Thread.sleep(2000);
                    // Verify the Team Balancer header is visible
                    verifyDashboardHeader();
                    loginSuccessful = true;
                    logger.info("Successfully logged in and verified Team Balancer header. New URL: {}", currentUrl);
                    break;
                } catch (FindFailed e) {
                    logger.warn("URL changed but Team Balancer header not found yet: {}", e.getMessage());
                }
            } else {
                logger.info("Still on signin page or URL hasn't changed yet");
            }
            Thread.sleep(1000); // Increased sleep between checks
        }
        
        if (!loginSuccessful) {
            // Log the final page source to see what's on the page
            logger.error("Final page source after login attempt: {}", driver.getPageSource());
            logger.error("Login failed - Could not verify successful login");
            throw new RuntimeException("Login failed - Could not verify successful login");
        }
    }
}
