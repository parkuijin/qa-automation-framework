package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileDriverFactory {
    public static AppiumDriver createDriver(String platformName) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        URL appiumServer = new URL("http://127.0.0.1:4723/");

        if (platformName.equalsIgnoreCase("android")) {
            String appPath = System.getProperty("user.dir") + "/apps/mda-2.2.0-25.apk";

            caps.setCapability("platformName", "Android");
            caps.setCapability("deviceName", "Pixel_5");
            caps.setCapability("app", appPath);
            caps.setCapability("automationName", "UiAutomator2");
            caps.setCapability("appPackage", "com.saucelabs.mydemoapp.android");
            caps.setCapability("appActivity", "com.saucelabs.mydemoapp.android.view.activities.SplashActivity");

            return new AndroidDriver(appiumServer, caps);

        } else if (platformName.equalsIgnoreCase("ios")) {
            String appPath = System.getProperty("user.dir") + "/apps/다운받고수정하기";

            caps.setCapability("platformName", "iOS");
            caps.setCapability("deviceName", "iPhone 13");
            caps.setCapability("automationName", "XCUITest");
            caps.setCapability("app", appPath);

            return new IOSDriver(appiumServer, caps);
        }

        throw new IllegalArgumentException("지원하지 않는 플랫폼 : " + platformName);
    }
}
