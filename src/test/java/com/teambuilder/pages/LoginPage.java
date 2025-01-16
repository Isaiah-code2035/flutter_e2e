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

    private final WebDriver driver;
    private final Screen screen;
    private final WebDriverWait wait;
    private final Pattern emailInput;
    private final Pattern passwordInput;
    private final Pattern loginButton;

    public LoginPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));

        // Initialize patterns with images and similarity thresholds
        this.emailInput = new Pattern(REG_IMAGE_DIR + "emailinputlogin.png").similar(0.4f);
        this.passwordInput = new Pattern(REG_IMAGE_DIR + "password-input.png").similar(0.4f);
        this.loginButton = new Pattern(REG_IMAGE_DIR + "login-button.png").similar(0.4f);

        // Verify images exist
        verifyImageExists(REG_IMAGE_DIR + "emailinputlogin.png", "emailinputlogin.png");
        verifyImageExists(REG_IMAGE_DIR + "password-input.png", "password-input.png");
        verifyImageExists(REG_IMAGE_DIR + "login-button.png", "login-button.png");
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

    public void enterPassword(String password) throws InterruptedException, FindFailed, AWTException {
        logger.info("Entering password");
        Thread.sleep(1000);
        
        // Find and click password input using image recognition
        screen.wait(passwordInput, 10);
        screen.click(passwordInput);
        Thread.sleep(2000);
        
        // Use Robot to type text
        Robot robot = new Robot();
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
                robot.keyRelease(KeyEvent.VK_SHIFT);
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
        Thread.sleep(2000);
    }

    public void clickLoginButton() throws FindFailed, InterruptedException {
        logger.info("Clicking login button");
        screen.wait(loginButton, 10);
        screen.click(loginButton);
        Thread.sleep(2000);
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
        
        // Wait for URL to change (indicating successful login)
        long startTime = System.currentTimeMillis();
        long timeout = 10000; // 10 seconds timeout
        boolean urlChanged = false;
        
        while (System.currentTimeMillis() - startTime < timeout) {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.equals(beforeUrl) && !currentUrl.contains("/signin")) {
                urlChanged = true;
                logger.info("Successfully logged in. New URL: {}", currentUrl);
                break;
            }
            Thread.sleep(500);
        }
        
        if (!urlChanged) {
            logger.error("Login failed - URL did not change from signin page");
            throw new RuntimeException("Login failed - remained on signin page");
        }
    }
}
