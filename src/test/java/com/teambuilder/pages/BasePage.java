package com.teambuilder.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.nio.file.Paths;

public abstract class BasePage {
    protected WebDriver driver;
    protected Screen screen;
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    protected static final String BASE_PATH = System.getProperty("user.dir");
    protected static final String RESOURCES_PATH = Paths.get(BASE_PATH, "src", "test", "resources").toString();
    protected static final String IMAGES_PATH = Paths.get(RESOURCES_PATH, "images").toString();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.screen = new Screen();
        screen.setAutoWaitTimeout(DEFAULT_TIMEOUT.getSeconds());
    }

    protected void waitForPageLoad() {
        try {
            Thread.sleep(2000); // Give the page time to settle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void waitForSikuliPattern(Pattern pattern) throws FindFailed {
        screen.wait(pattern, DEFAULT_TIMEOUT.getSeconds());
    }

    protected void waitForSikuliPatternWithSimilarity(Pattern pattern, float similarity) throws FindFailed {
        Pattern similarPattern = pattern.similar(similarity);
        screen.wait(similarPattern, DEFAULT_TIMEOUT.getSeconds());
    }

    protected void clickSikuliPattern(Pattern pattern) throws FindFailed {
        waitForSikuliPattern(pattern);
        screen.click(pattern);
    }

    protected void typeSikuliText(Pattern pattern, String text) throws FindFailed {
        waitForSikuliPattern(pattern);
        screen.click(pattern);
        screen.type(text);
    }

    protected String getImagePath(String category, String imageName) {
        return Paths.get(IMAGES_PATH, category, imageName).toString();
    }

    protected void bringWindowToFront() {
        driver.switchTo().window(driver.getWindowHandle());
        ((JavascriptExecutor) driver).executeScript("window.focus();");
    }
}
