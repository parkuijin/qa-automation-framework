package mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppiumTest {

    AndroidDriver driver;
    WebDriverWait wait;

    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";

    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        String appPath = System.getProperty("user.dir") + "/apps/mda-2.2.0-25.apk";

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Pixel_5");
        caps.setCapability("app", appPath);
        caps.setCapability("noReset", false);
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.saucelabs.mydemoapp.android");
        caps.setCapability("appActivity", "com.saucelabs.mydemoapp.android.view.activities.SplashActivity");

        URL appiumServer = new URL("http://127.0.0.1:4723/");

        driver = new AndroidDriver(appiumServer, caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLogin() {
        openMenu();
        navigateToLoginScreen();
        submitLoginForm();
    }

    @Test(dependsOnMethods = "testLogin")
    public void testLogout() {
        openMenu();
        performLogout();
    }

    @Step("왼쪽 상단 메뉴 열기")
    public void openMenu() {
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/menuIV")));
        menuButton.click();

        WebElement menuHeader = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/topViewTV")));
        Assert.assertTrue(menuHeader.isDisplayed(), "메뉴 헤더가 보이지 않습니다.");

        WebElement menuRecycler = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/menuRV")));
        Assert.assertTrue(menuRecycler.isDisplayed(), "메뉴 리스트가 보이지 않습니다.");
    }

    @Step("로그인 화면으로 이동")
    public void navigateToLoginScreen() {
        if (isLoggedIn()) {
            WebElement logoutMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Logout Menu Item")));
            logoutMenuItem.click();

            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
            Assert.assertTrue(logoutButton.isDisplayed(), "로그아웃 창이 생성되지 않았습니다.");
            logoutButton.click();
        } else {
            WebElement loginMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Login Menu Item")));
            loginMenuItem.click();
        }
    }

    @Step("아이디와 비밀번호 입력 후 로그인")
    public void submitLoginForm() {
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/nameET")));
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/passwordET")));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/loginBtn")));

        usernameInput.sendKeys(USERNAME);
        passwordInput.sendKeys(PASSWORD);

        loginButton.click();
    }

    @Step("로그아웃 실행")
    public void performLogout() {
        WebElement logoutMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Logout Menu Item")));

        if (isLoggedIn()) {
            logoutMenuItem.click();
        } else {
            navigateToLoginScreen();
            submitLoginForm();

            openMenu();
            logoutMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Logout Menu Item")));
            logoutMenuItem.click();
        }

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
        Assert.assertTrue(logoutButton.isDisplayed(), "로그아웃 창이 생성되지 않았습니다.");
        logoutButton.click();
    }

    public boolean isLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Logout Menu Item")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}