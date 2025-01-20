package com.teambuilder.tests;

import com.teambuilder.pages.LoginPage;
import com.teambuilder.pages.SportSelectionPage;
import com.teambuilder.pages.TeamCreationPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.awt.AWTException;
import java.io.IOException;

public class TeamCreationWithoutSkillsTest extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SportSelectionPage sportSelectionPage;
    private TeamCreationPage teamCreationPage;
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationWithoutSkillsTest.class);

    @Test(description = "Test team creation without skills - basic flow")
    public void testBasicTeamCreation() throws Exception {
        logger.info("Starting test: Basic team creation without skills");
        performLogin();
        selectSport("basketball");
        teamCreationPage.createTeamWithoutSkills(5, 10);
    }

    @Test(description = "Test team creation with minimum players")
    public void testMinimumPlayers() throws Exception {
        logger.info("Starting test: Team creation with minimum players");
        performLogin();
        selectSport("basketball");
        teamCreationPage.createTeamWithoutSkills(2, 4);
    }

    @Test(description = "Test team creation with maximum players")
    public void testMaximumPlayers() throws Exception {
        logger.info("Starting test: Team creation with maximum players");
        performLogin();
        selectSport("basketball");
        teamCreationPage.createTeamWithoutSkills(50, 100);
    }

    @Test(description = "Test team creation with invalid team number")
    public void testInvalidTeamNumber() throws Exception {
        logger.info("Starting test: Team creation with invalid team number");
        performLogin();
        selectSport("basketball");
        teamCreationPage.testInvalidTeamNumber(-1);
    }

    protected void performLogin() throws Exception {
        logger.info("Step 1: Starting login process");
        
        // Wait for page to be ready
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
        
        try {
            // Focus the window
            driver.switchTo().window(driver.getWindowHandle());
            ((JavascriptExecutor) driver).executeScript("window.focus();");
            
            // Wait for login form to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
            
            logger.info("Entering email");
            loginPage.enterEmail("oluwabamiseolanipekun@gmail.com");
            
            logger.info("Entering password");
            loginPage.enterPassword("Bamise123");
            
            logger.info("Clicking login button");
            loginPage.clickLoginButton();
            logger.info("Login successful");
            
            // Wait for dashboard to load
            wait.until(ExpectedConditions.urlContains("/dashboard"));
        } catch (Exception e) {
            logger.error("Login failed", e);
            throw e;
        }
        
        // Wait for login to complete
        wait.until(ExpectedConditions.urlContains("/dashboard"));
    }

    protected void selectSport(String sport) throws Exception {
        logger.info("Step 2: Selecting " + sport);
        try {
            // Wait for sport selection page to load
            wait.until(ExpectedConditions.urlContains("/sports"));
            
            sportSelectionPage.selectSport(sport);
            logger.info("Sport selection successful");
            
            // Wait for team creation page to load
            wait.until(ExpectedConditions.urlContains("/team-creation"));
        } catch (Exception e) {
            logger.error("Sport selection failed", e);
            throw e;
        }
    }
}
