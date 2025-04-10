import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KobitonTest {
    private static final Logger logger = LoggerFactory.getLogger(KobitonTest.class);


    @Test
    public void testKobiton() throws Exception {
        logger.info("Starting Kobiton test...");


        // Read credentials from system properties (set by GitHub Actions)
        String username = System.getProperty("username");
        String apiKey = System.getProperty("apiKey");
        if (username == null || apiKey == null) {
            logger.error("Kobiton username or API key not provided via system properties.");
            throw new IllegalArgumentException("Kobiton username and API key must be provided via system properties.");
        }
        String kobitonServerUrl = "https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub";
        logger.info("Kobiton server URL: {}", kobitonServerUrl);


        // Set up desired capabilities for Kobiton
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("kobiton:deviceName", "Galaxy S21 5G");
        caps.setCapability("platformVersion", "14");
        caps.setCapability("kobiton:groupId", 12854);
        caps.setCapability("kobiton:deviceGroup", "ORGANIZATION");
        caps.setCapability("kobiton:sessionName", "AT&T Demo Test - ApiDemos");
        caps.setCapability("kobiton:captureScreenshots", true);
        // ApiDemos app URL from Kobiton App Repository
        caps.setCapability("app", "kobiton-store:v710607");
        logger.info("Desired capabilities set: {}", caps.asMap());


        // Initialize the driver
        AppiumDriver driver = null;
        try {
            logger.info("Initializing Appium driver...");
            driver = new AndroidDriver(new URL(kobitonServerUrl), caps);
            logger.info("Appium driver initialized successfully.");


            // Wait for the app to load
            logger.info("Waiting for the app to load...");
            Thread.sleep(5000);


            // Navigate to "Views" (scroll and click)
            logger.info("Navigating to 'Views'...");
            driver.findElement(By.xpath("//android.widget.TextView[@text='Views']")).click();
            Thread.sleep(2000);


            // Navigate to "Buttons" (scroll and click)
            logger.info("Navigating to 'Buttons'...");
            driver.findElement(By.xpath("//android.widget.TextView[@text='Buttons']")).click();
            Thread.sleep(2000);


            // Interact with a button (e.g., "Normal" button)
            logger.info("Clicking the 'Normal' button...");
            driver.findElement(By.id("com.example.android.apis:id/button_normal")).click();
            Thread.sleep(3000);
            logger.info("Test completed successfully.");
        } catch (Exception e) {
            logger.error("Test failed with exception: ", e);
            throw e; // Re-throw to fail the test
        } finally {
            // Always quit the driver to end the session
            if (driver != null) {
                logger.info("Quitting Appium driver...");
                driver.quit();
            }
        }
    }
}



