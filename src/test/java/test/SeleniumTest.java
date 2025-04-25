package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import java.util.List;

@Listeners({AllureTestNg.class})
public class SeleniumTest {

    WebDriver driver;
    WebDriverWait wait;

    private static final int TIMEOUT = 10;
    private static final String SEARCH_KEYWORD = "automated";
    private static final String NO_SEARCH_KEYWORD = "qazwsxedc";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().driverVersion("135").setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        driver.get("https://www.selenium.dev");
    }

    @Test
    public void testSeleniumTitle() {
        checkSeleniumTitle();
    }

    @Test
    public void testAllMenuNavigation() {
        navigateToPage("downloads", "Downloads");
        navigateToPage("documentation", "Documentation");
        navigateToPage("projects", "Projects");
        navigateToPage("support", "Support");
        navigateToPage("blog", "Blog");
    }

    @Test
    public void testSearchFunction() {
        navigateToPage("documentation", "Documentation");

        clickSearchButton();
        performSearch(SEARCH_KEYWORD);
        verifySearchResultExists();

        clickSearchButton();
        performSearch(NO_SEARCH_KEYWORD);
        verifySearchResultNotExists();
    }

    @Step("Selenium 홈페이지 타이틀 확인")
    public void checkSeleniumTitle() {
        String title = driver.getTitle();

        Assert.assertTrue(title.contains("Selenium"), "타이틀에 Selenium이 포함되어야 합니다.");
    }

    @Step("페이지 이동 및 확인 : {pageName}")
    public void navigateToPage(String urlPart, String pageName) {
        // 페이지 이동
        WebElement pageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.nav-link[href='/" + urlPart + "']")));
        pageButton.click();

        // 페이지 확인
        wait.until(ExpectedConditions.urlContains(urlPart));
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains(urlPart), pageName + " 페이지로 이동하지 않았습니다.");
    }

    @Step("Search 버튼 클릭")
    public void clickSearchButton() {
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label='Search']")));
        searchButton.click();
    }

    @Step("검색어 입력 : {keyword}")
    public void performSearch(String keyword) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"docsearch-input\"]")));
        searchInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        searchInput.sendKeys(Keys.DELETE);
        searchInput.sendKeys(keyword);
    }

    @Step("검색 결과가 존재하는지 확인")
    public void verifySearchResultExists() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div")));
        WebElement searchUnorderedList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docsearch-list")));

        Assert.assertTrue(searchUnorderedList.isDisplayed(), "검색 결과가 존재하지 않습니다.");

        List<WebElement> searchList = searchUnorderedList.findElements(By.tagName("li"));

        Assert.assertTrue(searchList.size() > 0, "검색 결과가 존재하지 않습니다.");

        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.ESCAPE);
    }

    @Step("검색 결과가 존재하지 않는지 확인")
    public void verifySearchResultNotExists() {
        WebElement noResultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("DocSearch-NoResults")));

        Assert.assertTrue(noResultElement.isDisplayed(), "검색 결과가 없어야 합니다.");

        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.ESCAPE);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
