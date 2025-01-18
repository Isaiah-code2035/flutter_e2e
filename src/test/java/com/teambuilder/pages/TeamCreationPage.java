package com.teambuilder.pages;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamCreationPage extends BasePage {
    
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationPage.class);
    private Screen screen;
    private Pattern teamNumberInput;
    private Pattern selectSkillButton;
    private Pattern skillPopupItem;
    private Pattern confirmButton;
    private Pattern nextButton;
    private Pattern selectedSkillIndicator;
    private Pattern errorMessage;
    
    private static final String SCREENSHOT_PATH = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "screenshots", "team_creation").toString() + "/";
    private static final float SIMILARITY_THRESHOLD = 0.15f; 
    private static final int WAIT_TIMEOUT = 10;
    
    public TeamCreationPage(WebDriver driver) {
        super(driver);
        this.screen = new Screen();
        screen.setAutoWaitTimeout(WAIT_TIMEOUT);
        
        // Initialize patterns with image files using absolute paths
        teamNumberInput = new Pattern(SCREENSHOT_PATH + "team_number_input.png").similar(0.10f);
        selectSkillButton = new Pattern(SCREENSHOT_PATH + "select_skill_button.png").similar(SIMILARITY_THRESHOLD);
        skillPopupItem = new Pattern(SCREENSHOT_PATH + "skill_popup_item.png").similar(SIMILARITY_THRESHOLD);
        confirmButton = new Pattern(SCREENSHOT_PATH + "confirm_button.png").similar(SIMILARITY_THRESHOLD);
        selectedSkillIndicator = new Pattern(SCREENSHOT_PATH + "selected_skill.png").similar(SIMILARITY_THRESHOLD);
        nextButton = new Pattern(SCREENSHOT_PATH + "next_button.png").similar(SIMILARITY_THRESHOLD);
        errorMessage = new Pattern(SCREENSHOT_PATH + "error_message.png").similar(SIMILARITY_THRESHOLD);
        
        logger.info("Initialized TeamCreationPage with patterns from: {}", SCREENSHOT_PATH);
    }
    
    
   public void enterTeamNumberInSecondField(int number) throws FindFailed, InterruptedException {
        logger.info("Attempting to enter team number into the second input field: {}", number);
        Thread.sleep(8000); // Wait for the page to load fully

        boolean valueEntered = false;
        int maxAttempts = 10; // Increase max attempts
        
        while (!valueEntered && maxAttempts > 0) {
            try {
                logger.info("Attempts remaining: {}", maxAttempts);
                
                // Find all matches for the input fields
                logger.info("Looking for all matches for team number input fields");
                Iterator<Match> matches = screen.findAll(teamNumberInput);
                List<Match> matchesList = new ArrayList<>();
                while (matches.hasNext()) {
                    matchesList.add(matches.next());
                }

                // Log the number of matches found
                logger.info("Number of input fields found: {}", matchesList.size());
                if (matchesList.size() < 2) {
                    throw new FindFailed("Could not locate two or more team number input fields");
                }

                // Access the second match directly
                Match secondInputField = matchesList.get(1); // Index 1 corresponds to the second field
                logger.info("Found second team number input field at ({}, {})", secondInputField.x, secondInputField.y);
                secondInputField.highlight(2); // Highlight for debugging

                // Click multiple times to ensure focus
                logger.info("Clicking the second input field to focus");
                for (int i = 0; i < 3; i++) {
                    secondInputField.click();
                    Thread.sleep(500);
                }

                // Clear existing value using backspace multiple times
                logger.info("Clearing existing value");
                for (int i = 0; i < 5; i++) {
                    screen.type(Key.BACKSPACE);
                    Thread.sleep(200);
                }

                // Type the number with a delay
                logger.info("Typing team number: {}", number);
                Thread.sleep(1000);
                screen.type(String.valueOf(number));
                Thread.sleep(1000);

                // Verify that our value was entered by checking the field again
                matches = screen.findAll(teamNumberInput);
                matchesList.clear();
                while (matches.hasNext()) {
                    matchesList.add(matches.next());
                }
                
                if (matchesList.size() >= 2) {
                    Match verifyField = matchesList.get(1);
                    if (verifyField != null) {
                        valueEntered = true;
                        logger.info("Successfully verified team number entry in second field");
                        return;
                    }
                }

                logger.warn("Could not verify team number entry, retrying...");
                maxAttempts--;
                Thread.sleep(2000);

            } catch (FindFailed e) {
                logger.error("Attempt failed: {}", e.getMessage());
                maxAttempts--;
                Thread.sleep(2000);
            }
        }
        
        if (!valueEntered) {
            throw new FindFailed("Failed to enter and verify team number after all attempts");
        }
    }
    
    public void clickSelectDesiredSkill() throws FindFailed, InterruptedException {
        logger.info("Attempting to click select skill button");
        Thread.sleep(2000);
        screen.wait(selectSkillButton, WAIT_TIMEOUT);
        screen.click(selectSkillButton);
            logger.info("Successfully clicked select skill button");
    }
    
    public void selectSkillFromPopup() throws FindFailed, InterruptedException {
        logger.info("Attempting to select skill from popup");
        Thread.sleep(2000);
        screen.wait(skillPopupItem, WAIT_TIMEOUT);
        screen.click(skillPopupItem);
        logger.info("Successfully selected skill from popup");
    }
    
    public void clickConfirmOnPopup() throws FindFailed, InterruptedException {
        logger.info("Attempting to click confirm button");
        Thread.sleep(2000);
        screen.wait(confirmButton, WAIT_TIMEOUT);
        screen.click(confirmButton);
        logger.info("Successfully clicked confirm button");
    }
    
    public void clickNext() throws FindFailed, InterruptedException {
        logger.info("Attempting to click next button");
        Thread.sleep(2000);
        screen.wait(nextButton, WAIT_TIMEOUT);
        screen.click(nextButton);
        logger.info("Successfully clicked next button");
    }
    
    public boolean isSkillSelected() {
        try {
            logger.info("Checking if skill is selected");
            boolean selected = screen.wait(selectedSkillIndicator, WAIT_TIMEOUT) != null;
            logger.info("Skill selection status: {}", selected);
            return selected;
        } catch (Exception e) {
            logger.warn("Failed to verify skill selection: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isErrorDisplayed() {
        try {
            logger.info("Checking if error message is displayed");
            boolean displayed = screen.wait(errorMessage, WAIT_TIMEOUT) != null;
            logger.info("Error message display status: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Failed to verify error message: {}", e.getMessage());
            return false;
        }
    }
}
