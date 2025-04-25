package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class ElementCheck {
    public static void main(String[] args){
        WebDriverManager.chromedriver().driverVersion("135").setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/documentation/");

        /*
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label='Search']")));
        searchButton.click();
        By modal = By.className("DocSearch-Modal");
        wait.until(ExpectedConditions.visibilityOfElementLocated(modal));
        */

        By selector = By.id("docsearch-list");
        boolean isPresent = isElementPresent(driver, selector);
        System.out.println("Element present : " + isPresent);

        driver.quit();
    }

    public static boolean isElementPresent(WebDriver driver, By selector) {
        List<?> elements = driver.findElements(selector);
        return !elements.isEmpty();
    }
}
