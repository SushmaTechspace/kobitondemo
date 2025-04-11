


import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
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


        // Read credentials from system properties (set by GitHub Actions)
        String username = System.getProperty("username");
        String apiKey = System.getProperty("apiKey");
        if (username == null || apiKey == null) {
            logger.error("Kobiton username or API key not provided via system properties.");
            throw new IllegalArgumentException("Kobiton username and API key must be provided via system properties.");
        }
        logger.info("Username: {}", username);
        logger.info("API Key: [REDACTED]");
        String kobitonServerUrl = "https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub";
        logger.info("Kobiton server URL: {}", kobitonServerUrl);


        // Set up desired capabilities for Kobiton
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // 1) Basic Session info
        capabilities.setCapability("appium:sessionName", "Automation test session");
        capabilities.setCapability("appium:sessionDescription", "");
        capabilities.setCapability("appium:deviceOrientation", "portrait");
        capabilities.setCapability("appium:captureScreenshots", true);
        capabilities.setCapability("appium:groupId", 13581); // Group: SA Demo
        capabilities.setCapability("appium:deviceGroup", "ORGANIZATION");
        capabilities.setCapability("appium:deviceName", "Pixel 9 Pro");
        capabilities.setCapability("appium:platformVersion", "14");
        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:kobi:retainDurationInSeconds", 0);


        // 2) Tags
        capabilities.setCapability("kobiton:tags", Arrays.asList("smoke1", "smoke2"));


        // 3) Install the app from Kobiton's App Repository
        capabilities.setCapability("appium:app", "kobiton-store:v697834");


        // 4) Use appPackage/appActivity for API Demos
        capabilities.setCapability("appium:appPackage", "io.appium.android.apis");
        capabilities.setCapability("appium:appActivity", "io.appium.android.apis.ApiDemos");
        logger.info("Desired capabilities set: {}", capabilities.asMap());


        // Initialize the driver
        AndroidDriver driver = null;
        try {
            logger.info("Initializing Appium driver...");
            driver = new AndroidDriver(new URL(kobitonServerUrl), capabilities);
            logger.info("Appium driver initialized successfully. Session ID: {}", driver.getSessionId());


            // Wait for the app to load
            logger.info("Waiting for the app to load...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Content")));


            // Step Group 1: "Click Content"
            driver.setSetting("kobiton:stepGroup", "Home screen Test");
            logger.info("Clicking on 'Content'...");
            WebElement contentElement = driver.findElement(AppiumBy.accessibilityId("Content"));
            contentElement.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Resources")));


            // Step Group 2: "Click Resources"
            driver.setSetting("kobiton:stepGroup", "Search screen Test");
            logger.info("Clicking on 'Resources' (first time)...");
            WebElement resourcesFirst = driver.findElement(AppiumBy.accessibilityId("Resources"));
            resourcesFirst.click();
            Thread.sleep(9000); // Wait for the screen to load


            // Step Group 3: "Click Resources" (again)
            driver.setSetting("kobiton:stepGroup", "Add to cart Test");
            logger.info("Clicking on 'Resources' (second time)...");
            WebElement resourcesSecond = driver.findElement(AppiumBy.accessibilityId("Resources"));
            resourcesSecond.click();
            Thread.sleep(9000); // Wait for the screen to load


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



