package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class SportSelectionPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(SportSelectionPage.class);
    private static final String SPORT_IMAGES_PATH = "sports";

    private final Screen screen;
    private final Pattern basketballPattern;
    private final Pattern footballPattern;
    private final Pattern volleyballPattern;
    private final Pattern nextButtonPattern;
    private final Pattern selectedSportPattern;

    public SportSelectionPage(WebDriver driver) {
        super(driver);
        this.screen = new Screen();
        screen.setAutoWaitTimeout(30);
        
        basketballPattern = new Pattern(getImagePath(SPORT_IMAGES_PATH, "basketball.png")).similar(0.4f);
        footballPattern = new Pattern(getImagePath(SPORT_IMAGES_PATH, "football.png")).similar(0.4f);
        volleyballPattern = new Pattern(getImagePath(SPORT_IMAGES_PATH, "volleyball.png")).similar(0.4f);
        nextButtonPattern = new Pattern(getImagePath(SPORT_IMAGES_PATH, "next.png")).similar(0.4f);
        selectedSportPattern = new Pattern(getImagePath(SPORT_IMAGES_PATH, "selected.png")).similar(0.4f);
    }

    public void selectSport(String sport) throws FindFailed {
        logger.info("Selecting sport: {}", sport);
        waitForPageLoad();
        
        Pattern sportPattern;
        switch (sport.toLowerCase()) {
            case "basketball":
                sportPattern = basketballPattern;
                break;
            case "football":
                sportPattern = footballPattern;
                break;
            case "volleyball":
                sportPattern = volleyballPattern;
                break;
            default:
                throw new IllegalArgumentException("Invalid sport: " + sport);
        }
        
        try {
            // Try with different similarity thresholds
            float[] thresholds = {0.3f, 0.4f, 0.5f};
            boolean sportSelected = false;
            
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for {} with similarity {}", sport, threshold);
                    Pattern pattern = sportPattern.similar(threshold);
                    Match match = screen.wait(pattern, 10);
                    match.highlight(1);
                    match.click();
                    sportSelected = true;
                    break;
                } catch (FindFailed e) {
                    if (threshold == thresholds[thresholds.length - 1]) {
                        throw e;
                    }
                }
            }
            
            if (!sportSelected) {
                throw new FindFailed("Could not find " + sport + " button");
            }
            
            // Click next button
            for (float threshold : thresholds) {
                try {
                    logger.info("Looking for next button with similarity {}", threshold);
                    Pattern pattern = nextButtonPattern.similar(threshold);
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
            
            // Verify sport selection
            switch (sport.toLowerCase()) {
                case "basketball":
                    verifyBasketballSelected();
                    break;
                case "football":
                    verifyFootballSelected();
                    break;
                case "volleyball":
                    verifyVolleyballSelected();
                    break;
            }
            
            logger.info("{} selected successfully", sport);
        } catch (Exception e) {
            logger.error("Failed to select {}", sport, e);
            throw e;
        }
    }

    private void verifyBasketballSelected() throws FindFailed {
        logger.info("Verifying basketball is selected");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                Pattern pattern = selectedSportPattern.similar(threshold);
                Match match = screen.wait(pattern, 10);
                match.highlight(1);
                logger.info("Basketball selection verified");
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw new FindFailed("Could not verify basketball selection");
                }
            }
        }
    }

    private void verifyFootballSelected() throws FindFailed {
        logger.info("Verifying football is selected");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                Pattern pattern = selectedSportPattern.similar(threshold);
                Match match = screen.wait(pattern, 10);
                match.highlight(1);
                logger.info("Football selection verified");
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw new FindFailed("Could not verify football selection");
                }
            }
        }
    }

    private void verifyVolleyballSelected() throws FindFailed {
        logger.info("Verifying volleyball is selected");
        float[] thresholds = {0.3f, 0.4f, 0.5f};
        for (float threshold : thresholds) {
            try {
                Pattern pattern = selectedSportPattern.similar(threshold);
                Match match = screen.wait(pattern, 10);
                match.highlight(1);
                logger.info("Volleyball selection verified");
                return;
            } catch (FindFailed e) {
                if (threshold == thresholds[thresholds.length - 1]) {
                    throw new FindFailed("Could not verify volleyball selection");
                }
            }
        }
    }
}
