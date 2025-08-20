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

    @Test
    public void testCart() throws InterruptedException {
        openMenu();
        navigateToCatalogScreen();
        selectProduct();
        verifyProductInfo();
        addToCart();
        openCart();
        verifyProductInCart();
        removeProduct();
        verifyRemoveProductInCart();
    }

    @Test
    public void testOrder() throws InterruptedException {
        openMenu();
        navigateToCatalogScreen();
        selectProduct();
        verifyProductInfo();
        addToCart();
        openCart();
        verifyProductInCart();

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
        }

        WebElement loginMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Login Menu Item")));
        loginMenuItem.click();
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

    @Step("카탈로그 화면으로 이동")
    public void navigateToCatalogScreen() {
        WebElement catalogMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator("new UiSelector().text(\"Catalog\")")));
        catalogMenuItem.click();
    }

    @Step("상품 리스트에서 상품 선택")
    public void selectProduct() {
        WebElement recyclerView = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/productRV")));

        WebElement item = recyclerView.findElements(By.className("android.view.ViewGroup")).get(1);
        item.click();
    }

    @Step("상품 상세 정보 확인")
    public void verifyProductInfo() throws InterruptedException {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/productTV")));
        WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/priceTV")));

        Assert.assertTrue(title.isDisplayed(), "상품 상세 페이지에서 상품 이름이 표시되지 않음");
        Assert.assertTrue(price.isDisplayed(), "상품 상세 페이지에서 상품 가격이 표시되지 않음");

        WebElement desc = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/descTV\"))"));

        // 애니메이션 종료 대기
        Thread.sleep(1000);

        Assert.assertTrue(desc.isDisplayed(), "상품 상세 페이지에서 상품 설명이 표시되지 않음");
    }

    @Step("상품을 장바구니에 추가")
    public void addToCart() throws InterruptedException {
        WebElement addCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cartBt")));

        addCartButton.click();

        Thread.sleep(3000);
    }

    @Step("장바구니 열기")
    public void openCart() {
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cartIV")));
        cartButton.click();
    }

    @Step("장바구니에 상품이 추가되었는지 확인")
    public void verifyProductInCart() {
        WebElement cartItem = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/productRV")));

        Assert.assertTrue(cartItem.isDisplayed(), "장바구니에 상품이 존재하지 않음");
    }

    @Step("장바구니에 추가된 상품 삭제")
    public void removeProduct() throws InterruptedException {
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/removeBt")));

        removeButton.click();

        Thread.sleep(5000);
    }

    @Step("장바구니에서 상품이 삭제되었는지 확인")
    public void verifyRemoveProductInCart() {
        WebElement noItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/noItemTitleTV")));

        Assert.assertTrue(noItem.isDisplayed(), "장바구니에서 상품이 삭제되지 않았습니다.");
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