package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

@Listeners({AllureTestNg.class})
public class SeleniumTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().driverVersion("135").setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.selenium.dev");
    }

    @Test
    public void testSeleniumTitle() {
        checkSeleniumTitle();
    }

    @Test
    public void testDownloadsButton() {
        clickDownloadsButton();
        verifyDownloadsPage();
    }

    @Test
    public void testDocumentationButton() {
        clickDocumentationButton();
        verifyDocumentationPage();
    }

    @Step("Selenium 홈페이지 타이틀 확인")
    public void checkSeleniumTitle() {
        String title = driver.getTitle();

        System.out.println("페이지 타이틀 : " + title);
        Assert.assertTrue(title.contains("Selenium"), "타이틀에 'Selenium'이 포함되어야 합니다.");
    }

    @Step("Downloads 버튼 클릭")
    public void clickDownloadsButton() {
        WebElement downloadsButton = driver.findElement(By.cssSelector("a.nav-link[href='/downloads']"));
        downloadsButton.click();
    }

    @Step("Downloads 페이지 확인")
    public void verifyDownloadsPage() {
        // 페이지가 로드되었는지 확인
        wait.until(ExpectedConditions.urlContains("downloads"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("downloads"), "Downloads 페이지로 이동하지 않았습니다.");
    }

    @Step("Documentation 버튼 클릭")
    public void clickDocumentationButton() {
        WebElement docsButton = driver.findElement(By.cssSelector("a.nav-link[href='/documentation']"));
        docsButton.click();
    }

    @Step("Documentation 페이지 확인")
    public void verifyDocumentationPage() {
        wait.until(ExpectedConditions.urlContains("documentation"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("documentation"), "Documentation 페이지로 이동하지 않았습니다.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
