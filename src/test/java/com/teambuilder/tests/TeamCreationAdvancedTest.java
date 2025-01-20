package com.teambuilder.tests;

import com.teambuilder.pages.LoginPage;
import com.teambuilder.pages.SportSelectionPage;
import com.teambuilder.pages.TeamCreationPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.sikuli.script.FindFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;

public class TeamCreationAdvancedTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationAdvancedTest.class);

    @DataProvider(name = "invalidTeamNumbers")
    public Object[][] invalidTeamNumbers() {
        return new Object[][] {
            {-1},
            {0},
            {101}
        };
    }

    @Test(description = "Test team creation with multiple players and skills")
    public void testTeamCreationMultiplePlayers() throws Exception {
        logger.info("Starting test: Create team with multiple players and skills");
        performLogin();
        selectSport("basketball");
        teamCreationPage.createTeamWithoutSkills(5, 10);
    }

    @Test(description = "Test team creation with maximum allowed players")
    public void testTeamCreationMaxPlayers() throws Exception {
        logger.info("Starting test: Create team with maximum players");
        performLogin();
        selectSport("basketball");
        teamCreationPage.createTeamWithoutSkills(100, 200);
    }

    @Test(dataProvider = "invalidTeamNumbers", description = "Test team creation with invalid team numbers")
    public void testInvalidTeamNumbers(int teamNumber) throws Exception {
        logger.info("Starting test: Test invalid team number {}", teamNumber);
        performLogin();
        selectSport("basketball");
        teamCreationPage.testInvalidTeamNumber(teamNumber);
    }
}
