package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamCreationPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationPage.class);
    private static final String TEAM_IMAGES_PATH = "team";

    private final Screen screen;
    private final Pattern teamNumberPattern;
    private final Pattern playerNumberPattern;
    private final Pattern createButtonPattern;
    private final Pattern errorMessagePattern;
    private final Pattern successMessagePattern;

    public TeamCreationPage(WebDriver driver) {
        super(driver);
        this.screen = new Screen();
        screen.setAutoWaitTimeout(30);
        
        teamNumberPattern = new Pattern(getImagePath(TEAM_IMAGES_PATH, "team-number.png")).similar(0.4f);
        playerNumberPattern = new Pattern(getImagePath(TEAM_IMAGES_PATH, "player-number.png")).similar(0.4f);
        createButtonPattern = new Pattern(getImagePath(TEAM_IMAGES_PATH, "create.png")).similar(0.4f);
        errorMessagePattern = new Pattern(getImagePath(TEAM_IMAGES_PATH, "error-message.png")).similar(0.4f);
        successMessagePattern = new Pattern(getImagePath(TEAM_IMAGES_PATH, "success-message.png")).similar(0.4f);
    }

    public void createTeamWithoutSkills(int numTeams, int numPlayers) throws FindFailed {
        logger.info("Creating team without skills. Teams: {}, Players: {}", numTeams, numPlayers);
        waitForPageLoad();
        
        try {
            // Enter team number
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for team number input with similarity {}", threshold);
                    Pattern pattern = teamNumberPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    screen.type(String.valueOf(numTeams));
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
            
            // Enter player number
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for player number input with similarity {}", threshold);
                    Pattern pattern = playerNumberPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    screen.type(String.valueOf(numPlayers));
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
            
            // Click create button
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for create button with similarity {}", threshold);
                    Pattern pattern = createButtonPattern.similar(threshold);
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
            
            // Wait for success message
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for success message with similarity {}", threshold);
                    Pattern pattern = successMessagePattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    logger.info("Team created successfully");
                    return;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to create team", e);
            throw e;
        }
    }

    public void testInvalidTeamNumber(int invalidNumber) throws FindFailed {
        logger.info("Testing invalid team number: {}", invalidNumber);
        waitForPageLoad();
        
        try {
            // Enter invalid team number
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for team number input with similarity {}", threshold);
                    Pattern pattern = teamNumberPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    screen.type(String.valueOf(invalidNumber));
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
            
            // Click create button
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for create button with similarity {}", threshold);
                    Pattern pattern = createButtonPattern.similar(threshold);
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
            
            // Wait for error message
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for error message with similarity {}", threshold);
                    Pattern pattern = errorMessagePattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    logger.info("Error message displayed as expected");
                    return;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to test invalid team number", e);
            throw e;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return screen.exists(errorMessagePattern) != null;
        } catch (Exception e) {
            logger.error("Failed to check error message", e);
            return false;
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return screen.exists(successMessagePattern) != null;
        } catch (Exception e) {
            logger.error("Failed to check success message", e);
            return false;
        }
    }
}
