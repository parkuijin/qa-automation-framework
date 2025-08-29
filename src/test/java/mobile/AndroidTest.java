package mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.MobileDriverFactory;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Collections;

public class AndroidTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";

    private static final String FULL_NAME = "Uijin Park";
    private static final String ADDRESS_LINE_1 = "134, Gajwa-ro";
    private static final String ADDRESS_LINE_2 = "Engineering Building, 812";
    private static final String CITY = "Seodaemun-gu";
    private static final String STATE_REGION = "Seoul";
    private static final String ZIPCODE = "03656";
    private static final String COUNTRY = "Republic of Korea";
    private static final String CARD_NUMBER = "1234 5678 9109 8765";
    private static final String EXPIRATION_DATE = "10/25";
    private static final String SECURITY_CODE = "123";

    @BeforeClass
    @Parameters({"platform"})
    public void setUp(@Optional("android") String platform) throws MalformedURLException {
        driver = MobileDriverFactory.createDriver(platform);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLogin() {
        ensureLoggedOut();
        openMenu();
        navigateToLoginScreen();
        submitLoginForm();
    }

    @Test(dependsOnMethods = "testLogin")
    public void testLogout() {
        ensureLoggedIn();
        openMenu();
        performLogout();
    }

    @Test
    public void testCart() throws InterruptedException {
        addToCartFlow();
        removeProduct();
        verifyRemoveProductInCart();
    }

    @Test
    public void testPayment() throws InterruptedException {
        ensureLoggedIn();
        addToCartFlow();
        navigateToPaymentScreen();
        enterShoppingAddress();
        enterPaymentMethod();
        verifyPaymentInfo();
        verifyPaymentComplete();
    }

    @Step("메뉴 열기")
    public void openMenu() {
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/menuIV")));
        menuButton.click();

        WebElement menuHeader = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/topViewTV")));
        Assert.assertTrue(menuHeader.isDisplayed(), "메뉴 헤더가 보이지 않습니다.");

        WebElement menuRecycler = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/menuRV")));
        Assert.assertTrue(menuRecycler.isDisplayed(), "메뉴 리스트가 보이지 않습니다.");
    }

    @Step("로그인 화면 이동")
    public void navigateToLoginScreen() {
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
        WebElement logoutMenuItem = driver.findElement(AppiumBy.accessibilityId("Logout Menu Item"));
        logoutMenuItem.click();

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
        Assert.assertTrue(logoutButton.isDisplayed(), "로그아웃 창이 생성되지 않았습니다.");
        logoutButton.click();
    }

    @Step("카탈로그 화면 이동")
    public void navigateToCatalogScreen() {
        WebElement catalogMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator("new UiSelector().text(\"Catalog\")")));
        catalogMenuItem.click();
    }

    @Step("상품 선택")
    public void selectProduct() {
        WebElement recyclerView = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/productRV")));

        WebElement item = recyclerView.findElements(By.className("android.view.ViewGroup")).get(1);
        item.click();
    }

    @Step("상품 상세 정보 확인")
    public void verifyProductInfo() throws InterruptedException {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/productTV")));
        WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/priceTV")));

        Assert.assertTrue(title.isDisplayed(), "상품 상세 페이지에서 상품 이름이 표시되지 않습니다.");
        Assert.assertTrue(price.isDisplayed(), "상품 상세 페이지에서 상품 가격이 표시되지 않습니다.");

        WebElement desc = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/descTV\"))"));

        // 애니메이션 종료 대기
        Thread.sleep(1000);

        Assert.assertTrue(desc.isDisplayed(), "상품 상세 페이지에서 상품 설명이 표시되지 않습니다.");
    }

    @Step("상품을 장바구니에 추가")
    public void addToCart() throws InterruptedException {
        WebElement addCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cartBt")));

        addCartButton.click();

        // 추가 처리 대기
        Thread.sleep(3000);
    }

    @Step("장바구니 열기")
    public void openCart() {
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cartIV")));
        cartButton.click();
    }

    @Step("장바구니 상품 추가 확인")
    public void verifyProductInCart() {
        WebElement cartItem = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/productRV")));

        Assert.assertTrue(cartItem.isDisplayed(), "장바구니에 상품이 존재하지 않습니다.");
    }

    @Step("상품을 장바구니에 담는 전체 흐름")
    public void addToCartFlow() throws InterruptedException {
        openMenu();
        navigateToCatalogScreen();
        selectProduct();
        verifyProductInfo();
        addToCart();
        openCart();
        verifyProductInCart();
    }

    @Step("장바구니 상품 삭제")
    public void removeProduct() throws InterruptedException {
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/removeBt")));

        removeButton.click();

        // 삭제 처리 대기
        Thread.sleep(5000);
    }

    @Step("장바구니 상품 삭제 확인")
    public void verifyRemoveProductInCart() {
        WebElement noItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/noItemTitleTV")));

        Assert.assertTrue(noItem.isDisplayed(), "장바구니에서 상품이 삭제되지 않았습니다.");
    }

    @Step("결제 화면 이동")
    public void navigateToPaymentScreen() {
        WebElement paymentButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cartBt")));
        paymentButton.click();
    }

    @Step("결제 주소 입력")
    public void enterShoppingAddress() {
        WebElement fullName = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/fullNameET")));
        WebElement addressLine1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/address1ET")));
        WebElement addressLine2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/address2ET")));
        WebElement city = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cityET")));
        WebElement stateRegion = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/stateET")));
        WebElement zipCode = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/zipET")));
        WebElement country = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/countryET")));

        fullName.sendKeys(FULL_NAME);
        addressLine1.sendKeys(ADDRESS_LINE_1);
        addressLine2.sendKeys(ADDRESS_LINE_2);
        city.sendKeys(CITY);
        stateRegion.sendKeys(STATE_REGION);
        zipCode.sendKeys(ZIPCODE);
        country.sendKeys(COUNTRY);

        WebElement toPaymentButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/paymentBtn")));
        toPaymentButton.click();
    }

    @Step("결제 수단 입력")
    public void enterPaymentMethod() {
        WebElement fullName = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/nameET")));
        WebElement cardNumber = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cardNumberET")));
        WebElement expirationDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/expirationDateET")));
        WebElement securityCode = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/securityCodeET")));

        fullName.sendKeys(FULL_NAME);
        cardNumber.sendKeys(CARD_NUMBER);
        expirationDate.sendKeys(EXPIRATION_DATE);
        securityCode.sendKeys(SECURITY_CODE);

        WebElement reviewOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/paymentBtn")));
        reviewOrderButton.click();
    }

    @Step("결제 정보 확인")
    public void verifyPaymentInfo() {
        WebElement fullName = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/fullNameTV")));
        WebElement address = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/addressTV")));
        WebElement city = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/cityTV")));
        WebElement country = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/countryTV")));

        Assert.assertEquals(fullName.getText(), FULL_NAME, "이름이 일치하지 않습니다.");
        Assert.assertEquals(address.getText(), ADDRESS_LINE_1, "주소가 일치하지 않습니다.");
        Assert.assertEquals(city.getText(), CITY + ", " + STATE_REGION, "도시가 일치하지 않습니다.");
        Assert.assertEquals(country.getText(), COUNTRY + ", " + ZIPCODE, "우편번호가 일치하지 않습니다");

        WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/paymentBtn")));
        placeOrderButton.click();
    }

    @Step("결제 완료 확인")
    public void verifyPaymentComplete() {
        WebElement checkoutComplete = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/completeTV")));
        Assert.assertTrue(checkoutComplete.isDisplayed(), "결제가 완료되지 않았습니다.");

        WebElement continueShoppingButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.saucelabs.mydemoapp.android:id/shoopingBt")));
        continueShoppingButton.click();
    }

    @Step("로그아웃 상태일 시 로그인")
    public void ensureLoggedIn() {
        openMenu();
        try {
            WebElement loginMenuItem = driver.findElement(AppiumBy.accessibilityId("Login Menu Item"));
            if (loginMenuItem.isDisplayed()) {
                navigateToLoginScreen();
                submitLoginForm();
            }
        } catch (NoSuchElementException e) {
            tap(driver, 1000, 1000);
        }
    }

    @Step("로그인 상태일 시 로그아웃")
    public void ensureLoggedOut() {
        openMenu();
        try {
            WebElement logoutMenuItem = driver.findElement(AppiumBy.accessibilityId("Logout Menu Item"));
            if (logoutMenuItem.isDisplayed()) {
                logoutMenuItem.click();

                WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
                Assert.assertTrue(logoutButton.isDisplayed(), "로그아웃 창이 생성되지 않았습니다.");
                logoutButton.click();
            }
        } catch (NoSuchElementException e) {
            tap(driver, 1000, 1000);
        }
    }

    public static void tap(RemoteWebDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}