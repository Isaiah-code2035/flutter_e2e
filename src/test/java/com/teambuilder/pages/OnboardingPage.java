package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnboardingPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(OnboardingPage.class);
    private static final String ONBOARDING_IMAGES_PATH = "onboarding";

    private final Pattern welcomePattern;
    private final Pattern getStartedPattern;
    private final Pattern skipPattern;
    private final Pattern nextPattern;
    private final Pattern donePattern;

    public OnboardingPage(WebDriver driver) {
        super(driver);
        welcomePattern = new Pattern(getImagePath(ONBOARDING_IMAGES_PATH, "welcome.png")).similar(0.4f);
        getStartedPattern = new Pattern(getImagePath(ONBOARDING_IMAGES_PATH, "get_started.png")).similar(0.4f);
        skipPattern = new Pattern(getImagePath(ONBOARDING_IMAGES_PATH, "skip.png")).similar(0.4f);
        nextPattern = new Pattern(getImagePath(ONBOARDING_IMAGES_PATH, "next.png")).similar(0.4f);
        donePattern = new Pattern(getImagePath(ONBOARDING_IMAGES_PATH, "done.png")).similar(0.4f);
    }

    public void completeOnboarding() throws FindFailed {
        logger.info("Starting onboarding completion process");
        waitForPageLoad();
        
        try {
            // Click Get Started
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for Get Started button with similarity {}", threshold);
                    Pattern pattern = getStartedPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }

            // Click through onboarding screens
            for (int i = 0; i < 3; i++) {
                waitForPageLoad();
                for (float threshold : thresholds) {
                    try {
                        logger.info("Looking for Next button with similarity {}", threshold);
                        Pattern pattern = nextPattern.similar(threshold);
                        Match match = screen.wait(pattern, 10);
                        match.highlight(1);
                        match.click();
                        break;
                    } catch (FindFailed e) {
                        if (threshold == thresholds[thresholds.length - 1]) {
                            throw e;
                        }
                    }
                }
            }

            // Click Done on final screen
            waitForPageLoad();
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for Done button with similarity {}", threshold);
                    Pattern pattern = donePattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to complete onboarding", e);
            throw e;
        }
    }

    public void skipOnboarding() throws FindFailed {
        logger.info("Skipping onboarding process");
        waitForPageLoad();
        
        try {
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for Skip button with similarity {}", threshold);
                    Pattern pattern = skipPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to skip onboarding", e);
            throw e;
        }
    }

    public boolean isWelcomeScreenDisplayed() {
        logger.info("Checking if welcome screen is displayed");
        try {
            waitForPageLoad();
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for welcome screen with similarity {}", threshold);
                    Pattern pattern = welcomePattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    return true;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        logger.info("Welcome screen not found with any similarity threshold");
                        return false;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Failed to check welcome screen", e);
            return false;
        }
    }

    public void clickGetStartedAndWaitForNavigation() throws FindFailed {
        logger.info("Clicking Get Started and waiting for navigation");
        waitForPageLoad();
        
        try {
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for Get Started button with similarity {}", threshold);
                    Pattern pattern = getStartedPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    
                    // Wait for the Get Started button to disappear
                    screen.wait(pattern.similar(threshold), 10);
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to click Get Started button", e);
            throw e;
        }
    }

    @Override
    protected void waitForPageLoad() {
        logger.info("Waiting for onboarding page to load");
        try {
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for welcome text with similarity {}", threshold);
                    Pattern pattern = welcomePattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    if (match != null) {
                        match.highlight(1);
                        logger.info("Found welcome text at location: ({}, {}), score: {}", 
                            match.getX(), match.getY(), match.getScore());
                        return;
                    }
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        logger.warn("Welcome text not found with any similarity threshold");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to wait for page load", e);
        }
        // Give the page time to settle even if we don't find the welcome text
        super.waitForPageLoad();
    }
}
