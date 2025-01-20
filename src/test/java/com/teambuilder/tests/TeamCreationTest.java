package com.teambuilder.tests;

import org.sikuli.script.FindFailed;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.AWTException;

public class TeamCreationTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TeamCreationTest.class);

    @Test(description = "Test team creation without players")
    public void testTeamCreationWithoutPlayers() throws Exception {
        logger.info("Starting test: Team creation without players");
        try {
            performLogin();
            selectSport("basketball");
            teamCreationPage.createTeamWithoutSkills(5, 0);
        } catch (Exception e) {
            logger.error("Test failed", e);
            throw e;
        }
    }

    @Test(description = "Test team creation with players but no skills")
    public void testTeamCreationWithPlayersNoSkills() throws Exception {
        logger.info("Starting test: Team creation with players but no skills");
        try {
            performLogin();
            selectSport("basketball");
            teamCreationPage.createTeamWithoutSkills(5, 10);
        } catch (Exception e) {
            logger.error("Test failed", e);
            throw e;
        }
    }

    @Test(description = "Test team creation with players and skills")
    public void testTeamCreationWithPlayersAndSkills() throws Exception {
        logger.info("Starting test: Team creation with players and skills");
        try {
            performLogin();
            selectSport("basketball");
            teamCreationPage.createTeamWithoutSkills(5, 10);
        } catch (Exception e) {
            logger.error("Test failed", e);
            throw e;
        }
    }

    @Test(description = "Test invalid team numbers")
    public void testInvalidTeamNumbers() throws Exception {
        logger.info("Starting test: Test invalid team numbers");
        try {
            performLogin();
            selectSport("basketball");
            teamCreationPage.testInvalidTeamNumber(-1);
        } catch (Exception e) {
            logger.error("Test failed", e);
            throw e;
        }
    }
}
