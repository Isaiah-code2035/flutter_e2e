package com.teambuilder.pages;

import org.sikuli.script.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.awt.Rectangle;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;

public class RegistrationPage {
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String IMAGE_PATH = "src/test/resources/images/registration";
    private static final String IMAGE_DIR = BASE_PATH + "/" + IMAGE_PATH + "/";
    private static final Logger logger = LoggerFactory.getLogger(RegistrationPage.class);
    private final WebDriver driver;
    private final Screen screen;
    private final Pattern signupLink;
    private final Pattern nameInput;
    private final Pattern emailInput;
    private final Pattern passwordInput;
    private final Pattern confirmPassword;
    private final Pattern registerButton;
    private final WebDriverWait wait;
    
    private static final double DEFAULT_WAIT_TIMEOUT = 30.0;
    private static final double[] SIMILARITY_THRESHOLDS = {0.9, 0.8, 0.7, 0.6};
    private static final int CLICK_RETRY_COUNT = 3;

    public RegistrationPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Initialize patterns with images and similarity thresholds
        this.signupLink = new Pattern(IMAGE_DIR + "signup-link.png").similar(0.4f);
        this.nameInput = new Pattern(IMAGE_DIR + "name-input.png").similar(0.6f);
        this.emailInput = new Pattern(IMAGE_DIR + "email-input.png").similar(0.4f);
        this.passwordInput = new Pattern(IMAGE_DIR + "password-input.png").similar(0.6f);
        this.confirmPassword = new Pattern(IMAGE_DIR + "confirm-password.png").similar(0.6f);
        this.registerButton = new Pattern(IMAGE_DIR + "register-button.png").similar(0.6f);
        
        // Verify all images exist
        verifyImageExists("signup-link.png");
        verifyImageExists("name-input.png");
        verifyImageExists("email-input.png");
        verifyImageExists("password-input.png");
        verifyImageExists("confirm-password.png");
        verifyImageExists("register-button.png");
    }

    private void verifyImageExists(String imageFile) throws IOException {
        File file = new File(IMAGE_DIR, imageFile);
        if (!file.exists()) {
            throw new IOException("Image file not found: " + imageFile);
        }
        logger.info("Image file exists: {}", imageFile);
    }

    public void clickSignupLink() throws FindFailed, InterruptedException {
        logger.info("Clicking signup link");
        
        // Wait for page to be fully loaded and scroll to top
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        Thread.sleep(1000);
        
        // Set longer timeout for image search
        screen.setAutoWaitTimeout(10.0);
        
        // Try multiple times to click the signup link
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Pattern pattern = signupLink.similar(0.6);
                logger.info("Looking for signup link with similarity 0.6 (attempt {})", attempt);
                
                Match match = screen.find(pattern);
                match.highlight(2);
                logger.info("Found signup link at: x={}, y={}, w={}, h={}, score={}", 
                    match.x, match.y, match.w, match.h, match.getScore());
                    
                // Click and verify URL changes
                String beforeUrl = driver.getCurrentUrl();
                match.click();
                Thread.sleep(3000);
                String afterUrl = driver.getCurrentUrl();
                
                if (!afterUrl.equals(beforeUrl)) {
                    logger.info("URL changed after click: {} -> {}", beforeUrl, afterUrl);
                    return;
                }
                
                logger.warn("URL did not change after click, retrying...");
                Thread.sleep(2000);
                
            } catch (FindFailed e) {
                logger.warn("Failed to find signup link on attempt {}: {}", attempt, e.getMessage());
                if (attempt == 3) {
                    throw e;
                }
                Thread.sleep(2000);
            }
        }
        
        throw new FindFailed("Could not click signup link after all attempts");
    }

    public void enterName(String name) throws FindFailed, InterruptedException, AWTException {
        logger.info("Entering name: {}", name);
        
        // Find and click name input using image recognition
        screen.wait(nameInput, 10);
        screen.click(nameInput);
        Thread.sleep(2000);
        
        // Use Robot to type text
        Robot robot = new Robot();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == ' ') {
                robot.keyPress(KeyEvent.VK_SPACE);
                robot.keyRelease(KeyEvent.VK_SPACE);
            } else {
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
            }
            Thread.sleep(200);
        }
        Thread.sleep(2000);
    }

    public void enterEmail(String email) throws FindFailed, InterruptedException, AWTException {
        logger.info("Entering email: {}", email);
        
        // Find and click email input using image recognition
        screen.wait(emailInput, 10);
        screen.click(emailInput);
        Thread.sleep(2000);
        
        // Use Robot to type text
        Robot robot = new Robot();
        for (char c : email.toCharArray()) {
            if (c == '@') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '.') {
                robot.keyPress(KeyEvent.VK_PERIOD);
                robot.keyRelease(KeyEvent.VK_PERIOD);
            } else {
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
            }
            Thread.sleep(200);
        }
        Thread.sleep(2000);
    }

    public void enterPassword(String password) throws FindFailed, InterruptedException, AWTException {
        logger.info("Entering password");
        
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
            } else if (c == '#') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_3);
                robot.keyRelease(KeyEvent.VK_3);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
            }
            Thread.sleep(200);
        }
        Thread.sleep(2000);
    }

    public void enterConfirmPassword(String password) throws FindFailed, InterruptedException, AWTException {
        logger.info("Entering confirm password");
        
        // Find and click confirm password input using image recognition
        screen.wait(confirmPassword, 10);
        screen.click(confirmPassword);
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
            } else if (c == '#') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_3);
                robot.keyRelease(KeyEvent.VK_3);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                robot.keyPress(Character.toLowerCase(c));
                robot.keyRelease(Character.toLowerCase(c));
            }
            Thread.sleep(200);
        }
        Thread.sleep(2000);
    }

    public void clickRegisterButton() throws FindFailed, InterruptedException {
        logger.info("Clicking register button");
        
        // Find and click register button using image recognition
        screen.wait(registerButton, 10);
        screen.click(registerButton);
        Thread.sleep(1000);
    }

    public void register(String name, String email, String password) throws FindFailed, InterruptedException, AWTException {
        // Click signup link and wait for page to load
        clickSignupLink();
        Thread.sleep(5000);
        
        // Follow exact registration flow
        enterName(name);
        Thread.sleep(2000);
        
        enterEmail(email);
        Thread.sleep(2000);
        
        enterPassword(password);
        Thread.sleep(2000);
        
        enterConfirmPassword(password);
        Thread.sleep(2000);
        
        // Click register button and wait for navigation
        clickRegisterButton();
        Thread.sleep(5000);
        
        // Verify URL changed
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL after registration: {}", currentUrl);
        if (!currentUrl.contains("/onboarding")) {
            throw new RuntimeException("Registration failed. URL did not change to onboarding page. Current URL: " + currentUrl);
        }
    }
}
