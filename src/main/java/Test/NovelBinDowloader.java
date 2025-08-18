package Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NovelBinDowloader {

    public WebDriver chromeDriverSetup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Browser driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    public void webAccessor(String url, WebDriver driver) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void webCloser(WebDriver driver) {
        driver.close();
    }
}
