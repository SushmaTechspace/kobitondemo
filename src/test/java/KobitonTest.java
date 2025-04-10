
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;
import java.net.URL;


public class KobitonTest {
    @Test
    public void testKobiton() throws Exception {
        // Read credentials from system properties (set by GitHub Actions)
        String username = System.getProperty("username");
        String apiKey = System.getProperty("apiKey");
        if (username == null || apiKey == null) {
            throw new IllegalArgumentException("Kobiton username and API key must be provided via system properties.");
        }
        String kobitonServerUrl = "https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub";


        // Set up desired capabilities for Kobiton
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("kobiton:deviceName", "Galaxy S22");
        caps.setCapability("kobiton:sessionName", "AT&T Demo Test - ApiDemos");
        caps.setCapability("kobiton:captureScreenshots", true);
        // ApiDemos app URL from Kobiton App Repository
        caps.setCapability("app", "kobiton-store:v710607");


        // Initialize the driver
        AppiumDriver driver = new AndroidDriver(new URL(kobitonServerUrl), caps);


        try {
            // Wait for the app to load
            Thread.sleep(5000);


            // Navigate to "Views" (scroll and click)
            driver.findElement(By.xpath("//android.widget.TextView[@text='Views']")).click();
            Thread.sleep(2000);


            // Navigate to "Buttons" (scroll and click)
            driver.findElement(By.xpath("//android.widget.TextView[@text='Buttons']")).click();
            Thread.sleep(2000);


            // Interact with a button (e.g., "Normal" button)
            driver.findElement(By.id("com.example.android.apis:id/button_normal")).click();
            Thread.sleep(3000); // Observe result
        } finally {

            driver.quit();
        }
    }
}



