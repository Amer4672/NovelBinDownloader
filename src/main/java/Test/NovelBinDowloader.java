package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NovelBinDowloader {

    public WebDriver chromeDriverSetup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Browser driver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/135.0.0.0 Safari/537.36 OPR/120.0.0.0");

        WebDriver driver = new ChromeDriver();
        return driver;
    }

    public void waitDriver(WebDriver driver) {
        Random rand = new Random();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(rand.nextInt(10) + 10));
    }

    public void webAccessor(String url, WebDriver driver) {
        driver.get(url);
        driver.manage().window().maximize();
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitDriver(driver);

    }

    public void webCloser(WebDriver driver) {
        driver.quit();
    }

    /*/outdated method
    public void chapterDownloader(WebDriver driver, int chapNum, String title) {
        driver.findElement(By.cssSelector("a[href*='/chapter-" + chapNum + "']")).click();
        //String text = driver.findElement(By.tagName("body")).getText();

        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Chapter-" + chapNum + ".txt"))) {
            for (WebElement p : paragraphs) {
                writer.write(p.getText().trim());
                writer.newLine(); // adds line break between paragraphs
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.navigate().back();
    }*/

    public void downloadAllChapters(WebDriver driver) throws InterruptedException {
        //Find all chapter links
        List<WebElement> chapterElements = driver.findElements(By.cssSelector("a[href*='/chapter-']"));
        waitDriver(driver);

        List<String> chapterLinks = new ArrayList<>();
        List<String> chapterTitle = new ArrayList<>();
        for (WebElement el : chapterElements) {
            String href = el.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                chapterLinks.add(href);
            }
            String title = el.getText().replaceAll("[\\\\/:*?\"<>|]", "");
            if (title != null && !title.isEmpty()) {
                chapterTitle.add(title);
            }
        }

        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitDriver(driver);


        for (int i = 0; i < 3/*chapterLinks.size()*/; i++) {
            driver.get(chapterLinks.get(i));
            waitDriver(driver);

            // Collect all <p> text
            List<WebElement> paragraphs = driver.findElements(By.tagName("p"));

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(chapterTitle.get(i) + ".txt"))) {
                for (WebElement p : paragraphs) {
                    String text = p.getText().trim();
                    if (!text.isEmpty()) {
                        writer.write(text);
                        writer.newLine();
                        writer.newLine();
                    }
                }
                System.out.println("✅ " +chapterTitle.get(i) + " saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadChapter(WebDriver driver, String url) {
        driver.get(url);
        driver.manage().window().maximize();
        waitDriver(driver);
        int chapNum = 1;
        // Collect all <p> text
        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter( "Chapter" + chapNum + ".txt"))) {
            for (WebElement p : paragraphs) {
                String text = p.getText().trim();
                if (!text.isEmpty()) {
                    writer.write(text);
                    writer.newLine();
                    writer.newLine();
                }
            }
            System.out.println("✅ " + "Chapter" + chapNum + " saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
