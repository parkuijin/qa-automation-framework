package mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppiumTest {

    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        String appPath = System.getProperty("user.dir") + "/apps/mda-2.2.0-25.apk";

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Pixel_5");
        caps.setCapability("app", appPath);
        caps.setCapability("noReset", true);
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.saucelabs.mydemoapp.android");
        caps.setCapability("appActivity", "com.saucelabs.mydemoapp.android.view.activities.SplashActivity");

        URL appiumServer = new URL("http://127.0.0.1:4723/");

        driver = new AndroidDriver(appiumServer, caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}