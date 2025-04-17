

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

public class KobitonTest {
    private static final Logger logger = LoggerFactory.getLogger(KobitonTest.class);

    @Test
    public void testApiDemos() throws Exception {
        logger.info("Starting Kobiton test...");

        // Read credentials from system properties (set by GitHub Actions or local runs)
        String username = System.getProperty("username");
        String apiKey = System.getProperty("apiKey");
        if (username == null || apiKey == null) {
            logger.error("Kobiton username or API key not provided via system properties.");
            throw new IllegalArgumentException("Kobiton username and API key must be provided via system properties.");
        }
        logger.info("Username: {}", username);
        logger.info("API Key: [REDACTED]");

        // Build the Kobiton/Appium server URL with credentials
        String kobitonServerUrl = "https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub";
        logger.info("Kobiton server URL: {}", kobitonServerUrl);

        // ----------------------------------------------------------------
        // Use UiAutomator2Options for Appium 2.0 (Android-specific driver)
        // ----------------------------------------------------------------
        UiAutomator2Options options = new UiAutomator2Options();

        // (1) Session Info
        // For Appium or standard capabilities, use "appium:" prefixs added
        options.setCapability("appium:sessionName", "Github actions integration");
        options.setCapability("appium:sessionDescription", "");
        options.setCapability("appium:deviceOrientation", "portrait");
        options.setCapability("appium:captureScreenshots", true);

        // (2) Device & Platform details
        // Use "appium:deviceName" for the device name, etc.
        options.setCapability("appium:deviceGroup", "ORGANIZATION");
        options.setCapability("appium:deviceName", "Pixel 9 Pro");
        options.setCapability("appium:platformVersion", "14");
        options.setPlatformName("Android");


        // (3) Kobiton-specific settings
        // Must use "kobiton:" prefix
        options.setCapability("kobiton:groupId", 13581);  // Group: SA Demo
        options.setCapability("kobiton:tags", Arrays.asList("smoke1", "smoke2"));
        options.setCapability("appium:kobi:retainDurationInSeconds", 0);

        // (4) App to Install (from Kobiton store)
        options.setCapability("appium:app", "kobiton-store:v697834");

        // (5) Additional app-specific capabilities
        options.setCapability("appium:appPackage", "io.appium.android.apis");
        options.setCapability("appium:appActivity", "io.appium.android.apis.ApiDemos");

        logger.info("UiAutomator2Options: {}", options.asMap());

        // Initialize the driver
        AndroidDriver driver = null;
        try {
            logger.info("Initializing Appium driver...");
            driver = new AndroidDriver(new URL(kobitonServerUrl), options);
            logger.info("Appium driver initialized successfully. Session ID: {}", driver.getSessionId());

            // Wait for the app to load
            logger.info("Waiting for the app to load...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Content")));

            // Step Group 1: "Click Content"
            // 'kobiton:stepGroup' is a Kobiton extension to group steps in logs
            driver.setSetting("kobiton:stepGroup", "Main Screen Test");
            logger.info("Clicking on 'Content'...");
            WebElement contentElement = driver.findElement(AppiumBy.accessibilityId("Content"));
            contentElement.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Resources")));

            // Step Group 2: "Click Resources"
            driver.setSetting("kobiton:stepGroup", "Second screen Test");
            logger.info("Clicking on 'Resources' (first time)...");
            WebElement resourcesFirst = driver.findElement(AppiumBy.accessibilityId("Resources"));
            resourcesFirst.click();
            Thread.sleep(9000); // Wait for the screen to load; consider an explicit wait if possible

            // Step Group 3: "Click Resources"
            driver.setSetting("kobiton:stepGroup", "Final screen Test");
            logger.info("Clicking on 'Resources' (second time)...");
            WebElement resourcesSecond = driver.findElement(AppiumBy.accessibilityId("Resources"));
            resourcesSecond.click();
            Thread.sleep(9000);

            // Step Group 4: "Close the app"
            driver.setSetting("kobiton:stepGroup", "Close the app");
            logger.info("Test completed successfully.");

        } catch (Exception e) {
            logger.error("Test failed with exception: ", e);
            throw e; // Re-throw to fail the test
        } finally {
            // Always quit the driver to end the session
            if (driver != null) {
                logger.info("Quitting Appium driver...");
                driver.quit();
                System.out.println("Driver session ended.");
            }
        }
    }
}


