package web;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Listeners(AllureTestNg.class)
public class WebTest {

    WebDriver driver;
    WebDriverWait wait;

    private static final int TIMEOUT = 10;
    private static final String SEARCH_KEYWORD = "automated";
    private static final String NO_SEARCH_KEYWORD = "qazwsxedc";

    @Parameters("browser")
    @BeforeClass
    public void setUp(String browser) {
        if (browser == null)
            browser = "chrome";

        switch (browser.toLowerCase()) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
                driver = new ChromeDriver();
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            case "edge" -> {
                System.setProperty("webdriver.edge.driver", "drivers/msedgedriver");
                driver = new EdgeDriver();
            }
            default -> throw new IllegalArgumentException("지원하지 않는 브라우저입니다. : " + browser);
        }

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

    @Test
    public void testDownloadFunction() {
        navigateToPage("downloads", "Downloads");

        verifyLanguageBindingDownloads();
        verifySeleniumServerDownload();
    }

    @Test
    public void testChangeLanguage() {
        verifyChangeLanguage();
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

        // 운영체제에 따라 다른 키 사용
        String os = System.getProperty("os.name").toLowerCase();
        Keys key = os.contains("mac") ? Keys.COMMAND : Keys.CONTROL;

        searchInput.sendKeys(Keys.chord(key, "a"));
        searchInput.sendKeys(Keys.DELETE);

        searchInput.sendKeys(keyword);
    }

    @Step("검색 결과가 존재하는지 확인")
    public void verifySearchResultExists() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div")));
        WebElement searchUnorderedList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docsearch-list")));

        Assert.assertTrue(searchUnorderedList.isDisplayed(), "검색 결과가 존재하지 않습니다.");

        List<WebElement> searchList = searchUnorderedList.findElements(By.tagName("li"));

        Assert.assertFalse(searchList.isEmpty(), "검색 결과가 존재하지 않습니다.");

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

    @Step("Selenium Client 언어별 바인딩 다운로드 링크 확인")
    public void verifyLanguageBindingDownloads() {
        List<WebElement> languageBindingSection = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.col-sm-4.p-3")));

        for (WebElement card : languageBindingSection) {
            String language = card.findElement(By.cssSelector("p.card-title")).getText();

            List<WebElement> links = card.findElements(By.cssSelector("a.card-link"));
            if (!links.isEmpty()) {
                    for (WebElement link : links) {
                        String linkTitle = link.getText();

                        Assert.assertTrue(link.isDisplayed(), language + " - " + linkTitle + " 링크가 표시되지 않습니다.");

                        String href = link.getAttribute("href");

                        verifyLinkValidation(href);
                    }
            } else {
                Assert.fail(language + " 언어의 다운로드 링크가 존재하지 않습니다.");
            }
        }
    }

    @Step("Selenium Server 다운로드 링크 확인")
    public void verifySeleniumServerDownload() {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.card-text > a[href*='selenium-server']")));

        String href = link.getAttribute("href");

        verifyLinkValidation(href);

        Assert.assertTrue(href.endsWith(".jar"),href + " 다운로드 파일이 .jar 파일이 아닙니다.");
    }

    @Step("{href} 링크 유효성 검사")
    public void verifyLinkValidation(String href) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            URI uri = URI.create(href);

            // 요청을 생성, BodyPublishers.noBody()를 사용하여 Body를 제외
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // 요청을 받음, Body가 없기 때문에 HttpResponse.BodyHandlers.discarding() 사용
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            int responseCode = response.statusCode();

            Assert.assertTrue(responseCode < 400, href + " 다운로드 링크가 유효하지 않습니다. : " + responseCode);
        } catch (Exception e) {
            Assert.fail(href + " 다운로드 링크 연결 중 예외가 발생했습니다. : " + e.getMessage());
        }
    }

    @Step("언어 변경 기능 검증")
    public void verifyChangeLanguage() {
        WebElement languageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main_navbar\"]/ul/li[7]/div/a")));
        languageButton.click();

        List<WebElement> languageList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.dropdown-menu.show > li > a")));

        List<String> languages = new ArrayList<>();

        for (WebElement webElement : languageList) {
            String languageName = webElement.getText();
            languages.add(languageName);
        }

        // 언어 변경 드롭다운 닫기
        languageButton.click();

        for (String languageName : languages) {
            // DOM 요소 갱신
            languageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main_navbar\"]/ul/li[7]/div/a")));

            languageButton.click();
            WebElement languageChangButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='dropdown-menu show']/li/a[text()='" + languageName + "']")));
            languageChangButton.click();

            Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main_navbar\"]/ul/li[7]/div/a"))).getText(), languageName, "언어 변경 실패 : " + languageName);

            String url = driver.getCurrentUrl();
            boolean isContainsLanguage = false;

            switch (languageName) {
                case "Português (Brasileiro)" -> isContainsLanguage = url.contains("pt-br");
                case "中文简体" -> isContainsLanguage = url.contains("zh-cn");
                case "日本語" -> isContainsLanguage = url.contains("ja");
                case "Other" -> isContainsLanguage = url.contains("other");
            }
            Assert.assertTrue(isContainsLanguage, "언어 변경 실패, URL에 " + languageName + " 언어 코드가 포함되지 않았습니다.");
        }

        // 초기 언어로 설정
        languageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main_navbar\"]/ul/li[7]/div/a")));
        languageButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='dropdown-menu show']/li/a[text()='English']"))).click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}