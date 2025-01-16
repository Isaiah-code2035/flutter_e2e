package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class SportSelectionPage {
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String IMAGE_PATH = "src/test/resources/images/sports";
    private static final String IMAGE_DIR = BASE_PATH + "/" + IMAGE_PATH + "/";
    private static final Logger logger = LoggerFactory.getLogger(SportSelectionPage.class);
    private static final int DEFAULT_WAIT_TIMEOUT = 30;

    private final WebDriver driver;
    private final Screen screen;
    private final Pattern basketballOption;
    private final Pattern verifySoccerImage;

    public SportSelectionPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        this.basketballOption = new Pattern(IMAGE_DIR + "basketball.png").similar(0.4f);
        this.verifySoccerImage = new Pattern(IMAGE_DIR + "verifysoccer.png").similar(0.3f);
    }

    public void selectBasketball() throws FindFailed {
        logger.info("Attempting to select Basketball");
        screen.wait(basketballOption).click();
        logger.info("Successfully clicked on Basketball option");
        
        // Wait for a moment to let any animations complete
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted while waiting for animations", e);
        }
        
        // Verify the soccer image is displayed
        try {
            Match soccerMatch = screen.wait(verifySoccerImage, 20);
            logger.info("Successfully verified soccer image is displayed");
            
            // Click on the soccer image
            soccerMatch.click();
            logger.info("Clicked on soccer image");
            
            // Wait for 10 seconds to see the result
            Thread.sleep(10000);
            logger.info("Finished waiting after soccer click");
        } catch (FindFailed e) {
            logger.error("Failed to verify soccer image after selecting basketball", e);
            throw new FindFailed("Could not verify soccer image after selecting basketball: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted while waiting after soccer click", e);
        }
    }
}
