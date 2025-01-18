package com.teambuilder.tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import com.teambuilder.pages.*;
import com.teambuilder.utils.BaseTest;
import org.sikuli.script.FindFailed;
import org.openqa.selenium.JavascriptExecutor;
import java.awt.AWTException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamCreationTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationTest.class);
    private LoginPage loginPage;
    private SportSelectionPage sportSelectionPage;
    private TeamCreationPage teamCreationPage;
    
    @Test(description = "Test team creation without adding new player")
    public void testTeamCreationWithoutNewPlayer() throws FindFailed, InterruptedException, AWTException, IOException {
        logger.info("Starting team creation test");
        
        // Initialize page objects
        logger.info("Initializing page objects");
        loginPage = new LoginPage(driver);
        sportSelectionPage = new SportSelectionPage(driver);
        teamCreationPage = new TeamCreationPage(driver);
        
        // Step 1: Login
        logger.info("Step 1: Starting login process");
        Thread.sleep(5000);
        
        driver.switchTo().window(driver.getWindowHandle());
        ((JavascriptExecutor) driver).executeScript("window.focus();");
        Thread.sleep(2000);
        
        logger.info("Entering email");
        loginPage.enterEmail("oluwabamiseolanipekun@gmail.com");
        Thread.sleep(2000);
        
        logger.info("Entering password");
        loginPage.enterPassword("Bamise123");
        Thread.sleep(2000);
        
        logger.info("Clicking login button");
        loginPage.clickLoginButton();
        Thread.sleep(20000);
        
        // Step 2: Select sport
        logger.info("Step 2: Selecting basketball");
        sportSelectionPage.selectBasketball();
        Thread.sleep(10000);
        
        driver.switchTo().window(driver.getWindowHandle());
        ((JavascriptExecutor) driver).executeScript("window.focus();");
        Thread.sleep(5000);
        
        // Step 3: Enter team number
        logger.info("Step 3: Entering team number");
        teamCreationPage.enterTeamNumberInSecondField(2);
        Thread.sleep(3000);
        
        // Step 4: Select skills
        logger.info("Step 4: Selecting skills");
        teamCreationPage.clickSelectDesiredSkill();
        Thread.sleep(2000);
        
        teamCreationPage.selectSkillFromPopup();
        Thread.sleep(2000);
        
        teamCreationPage.clickConfirmOnPopup();
        Thread.sleep(2000);
    }
}
