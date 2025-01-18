package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.awt.Rectangle;

public class SportSelectionPage {
    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String IMAGE_PATH = "src/test/resources/images/sports";
    private static final String IMAGE_DIR = BASE_PATH + "/" + IMAGE_PATH + "/";
    private static final Logger logger = LoggerFactory.getLogger(SportSelectionPage.class);
    private static final int DEFAULT_WAIT_TIMEOUT = 30;
    private static final float SIMILARITY_THRESHOLD = 0.15f; // Even lower threshold for more lenient matching

    private final WebDriver driver;
    private final Screen screen;
    private final Pattern basketballOption;
    private final Pattern verifySoccerImage;

    public SportSelectionPage(WebDriver driver) throws IOException {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_WAIT_TIMEOUT);
        
        String basketballPath = IMAGE_DIR + "basketball.png";
        String soccerPath = IMAGE_DIR + "verifysoccer.png";
        
        // Verify image files exist and log their details
        File basketballFile = new File(basketballPath);
        File soccerFile = new File(soccerPath);
        
        if (!basketballFile.exists()) {
            throw new IOException("Basketball image not found at: " + basketballPath);
        }
        if (!soccerFile.exists()) {
            throw new IOException("Soccer image not found at: " + soccerPath);
        }
        
        logger.info("Loading basketball image from: {}", basketballPath);
        logger.info("Basketball image size: {} bytes", basketballFile.length());
        logger.info("Loading soccer image from: {}", soccerPath);
        logger.info("Soccer image size: {} bytes", soccerFile.length());
        
        this.basketballOption = new Pattern(basketballPath).similar(SIMILARITY_THRESHOLD);
        this.verifySoccerImage = new Pattern(soccerPath).similar(SIMILARITY_THRESHOLD);
        
        // Log screen information
        logger.info("Screen dimensions: {}x{}", screen.w, screen.h);
        logger.info("Screen bounds: {}", screen.getBounds());
    }

    public void selectBasketball() throws FindFailed {
        logger.info("Attempting to select Basketball");
        
        try {
            // Wait longer after login before looking for basketball
            logger.info("Waiting for page to stabilize...");
            Thread.sleep(10000);
            
            // Log current screen state
            Rectangle bounds = screen.getBounds();
            logger.info("Screen state before search - Width: {}, Height: {}, X: {}, Y: {}", 
                bounds.width, bounds.height, bounds.x, bounds.y);
            
            // Try to find the basketball option with multiple attempts
            Match match = null;
            int attempts = 3;
            
            for (int i = 0; i < attempts && match == null; i++) {
                try {
                    logger.info("Attempt {} to find basketball image...", i + 1);
                    match = screen.wait(basketballOption, 5);
                    logger.info("Found basketball on attempt {}", i + 1);
                    break;
                } catch (FindFailed e) {
                    if (i == attempts - 1) throw e;
                    logger.info("Attempt {} failed, retrying...", i + 1);
                    Thread.sleep(2000);
                }
            }
            
            if (match != null) {
                logger.info("Found basketball option at location: ({}, {})", match.x, match.y);
                logger.info("Match score: {}", match.getScore());
                logger.info("Match target: {}", match.getTarget());
                
                // Highlight the match for debugging (longer duration)
                match.highlight(5);
                
                // Click the basketball option with offset
                screen.click(match.offset(0, 5));
                logger.info("Successfully clicked on Basketball option with offset");
                
                // Wait for animations
                Thread.sleep(5000);
                
                // Verify the soccer image is displayed
                Match soccerMatch = screen.wait(verifySoccerImage, 20);
                logger.info("Found soccer image at location: ({}, {})", soccerMatch.x, soccerMatch.y);
                logger.info("Soccer match score: {}", soccerMatch.getScore());
                
                // Highlight the soccer match for debugging
                soccerMatch.highlight(5);
                
                // Click on the soccer image with offset
                screen.click(soccerMatch.offset(0, 5));
                logger.info("Clicked on soccer image with offset");
                
                // Wait for any final animations
                Thread.sleep(10000);
                logger.info("Finished waiting after soccer click");
            } else {
                throw new FindFailed("Could not find basketball image after " + attempts + " attempts");
            }
            
        } catch (FindFailed e) {
            logger.error("Failed to find or click on basketball/soccer image: {}", e.getMessage());
            logger.error("Screen dimensions during failure: {}x{}", screen.w, screen.h);
            throw e;
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
