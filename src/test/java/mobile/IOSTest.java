package mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.MobileDriverFactory;

import java.net.MalformedURLException;
import java.time.Duration;

public class IOSTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    @BeforeClass
    @Parameters({"platform"})
    public void setUp(@Optional("ios") String platform) throws MalformedURLException {
        driver = MobileDriverFactory.createDriver(platform);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginAndLogout() {
        // 로그인
        loginFlow();
        openMenu();
        // 로그아웃
        performLoginAndLogout();
    }

    @Test
    public void testCart() throws InterruptedException {
        loginFlow();
        addToCartFlow();
        removeProduct();
        verifyRemoveProductInCart();
    }

    @Step("메뉴 화면 열기")
    public void openMenu() {
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("More-tab-item")));
        menuButton.click();
    }

    @Step("로그인 화면 이동")
    public void performLoginAndLogout() {
        WebElement loginMenuItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("LogOut-menu-item")));
        loginMenuItem.click();
    }

    @Step("아이디와 비밀번호 입력 후 로그인")
    public void submitLoginForm() {
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"bob@example.com\"`]")));
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("10203040")));
        usernameInput.click();
        passwordInput.click();

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Login\"`]")));
        loginButton.click();
    }

    @Step("로그인 전체 흐름")
    public void loginFlow() {
        openMenu();
        performLoginAndLogout();
        submitLoginForm();
    }

    @Step("카탈로그 화면 이동")
    public void navigateToCatalogScreen() {
        WebElement catalogButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Catalog-tab-item")));
        catalogButton.click();
    }

    @Step("상품 선택")
    public void selectProduct() {
        WebElement item = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == \"ProductItem\"`][1]")));
        item.click();
    }

    @Step("상품 상세 정보 확인")
    public void verifyProductInfo() throws InterruptedException {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Sauce Labs Backpack - Black")));
        WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Price")));

        Assert.assertTrue(title.isDisplayed(), "상품 상세 페이지에서 상품 이름이 표시되지 않습니다.");
        Assert.assertTrue(price.isDisplayed(), "상품 상세 페이지에서 상품 가격이 표시되지 않습니다.");
    }

    @Step("상품을 장바구니에 추가")
    public void addToCart() throws InterruptedException {
        WebElement addCartButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("AddToCart")));
        addCartButton.click();

        Thread.sleep(2000);
    }

    @Step("장바구니 열기")
    public void openCart() {
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Cart-tab-item")));
        cartButton.click();
    }

    @Step("장바구니 상품 추가 확인")
    public void verifyProductInCart() {
        WebElement cartItem = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.className("XCUIElementTypeCell")));
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
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Remove Item\"`]")));
        removeButton.click();

        Thread.sleep(2000);
    }

    @Step("장바구니 상품 삭제 확인")
    public void verifyRemoveProductInCart() {
        WebElement noItem = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("No Items")));
        Assert.assertTrue(noItem.isDisplayed(), "장바구니에서 상품이 삭제되지 않았습니다.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
