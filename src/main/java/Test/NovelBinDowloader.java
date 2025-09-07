package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NovelBinDowloader {

    //webdriver stuff///////////////////////////////////////////////////////////////////////////////////////
    String[] userAgents = {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 OPR/120.0.0.0",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0",
                            "Mozilla/5.0 (X11; CrOS x86_64 14541.0.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.3.1 Safari/605.1.15",
    "Mozilla/5.0 (Linux; Android 15; SM-S931B Build/AP3A.240905.015.A2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/127.0.6533.103 Mobile Safari/537.36",
    "Mozilla/5.0 (Linux; Android 15; SM-S931U Build/AP3A.240905.015.A2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/132.0.6834.163 Mobile Safari/537.36",
    "Mozila/5.0 (Linux; Android 14; SM-S928B/DS) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.6099.230 Mobile Safari/537.36",
    "Mozilla/5.0 (Linux; Android 13; SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36",
    "Mozilla/5.0 (Linux; Android 14; Pixel 9 Pro Build/AD1A.240418.003; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/124.0.6367.54 Mobile Safari/537.36"};
    Random rand = new Random();


    public WebDriver chromeDriverSetup() {
        System.setProperty("webdriver.chrome.mainDriver", "C:\\Browser mainDriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=" + userAgents[rand.nextInt(userAgents.length)]);

        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    public WebDriverWait waitDriver(WebDriver driver) { /*WebDriverWait*/
        Random rand = new Random();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(rand.nextInt(10) + 10));
        return wait;//return wait so that i can do expected conditions
    }

    public void webAccessor(String url, WebDriver driver) {
        driver.get(url);
        driver.manage().window().maximize();
        waitDriver(driver);
    }

    public void webCloser(WebDriver driver) {
        driver.quit();
    }

    //download chapter methods///////////////////////////////////////////////////////////////////////////////////////////

    public void downloadAllChaptersHTML(WebDriver driver, String path, int startAt) throws InterruptedException {

        //Find all chapter links
        Thread.sleep(5000);
        //waitDriver(mainDriver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/chapter-']")));
        List<WebElement> chapterElements = driver.findElements(By.cssSelector("a[href*='chapter-']"));
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

        webCloser(driver);

        for (int i = 1 + startAt; i < chapterLinks.size(); i++) { //int i = 2 to skip the read now and latest chapter //chapterLinks.size()

            System.out.println(chapterTitle.get(i));
            System.out.println(chapterLinks.get(i));

            WebDriver resetDriver = chromeDriverSetup();
            Thread.sleep(rand.nextInt(5000) + 3000);
            resetDriver.get(chapterLinks.get(i));
           // mainDriver.get(chapterLinks.get(i));
            waitDriver(resetDriver);

            // Collect all <p> text
            List<WebElement> paragraphs = resetDriver.findElements(By.tagName("p"));

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + chapterTitle.get(i) + " (HTML).txt"))) {
                for (WebElement p : paragraphs) {
                    String text = p.getAttribute("outerHTML");
                    if (!text.isEmpty()) {
                        writer.write(text);
                        writer.newLine();
                        writer.write("<br>");
                        writer.newLine();
                    }
                }
                System.out.println("✅ " +chapterTitle.get(i) + " saved.");
                webCloser(resetDriver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadAllChaptersHTMLver2(String path, String fileNameLink, String fileNameTitle) throws InterruptedException {
        List<String> chapLinks = new ArrayList<>();
        List<String> chapTitle = new ArrayList<>();

        //chap link into list
        try (BufferedReader reader = new BufferedReader(new FileReader( fileNameLink + ".txt"))) {
            String link;
            while ((link = reader.readLine()) != null) {
                chapLinks.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //chap title into list
        try (BufferedReader reader = new BufferedReader(new FileReader( fileNameTitle + ".txt"))) {
            String title;
            while ((title = reader.readLine()) != null) {
                chapTitle.add(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 2; i < 4; i++) { //chapLinks.size()

            WebDriver resetDriver = chromeDriverSetup();
            Thread.sleep(10000);
            resetDriver.get(chapLinks.get(i));

            // Collect all <p> text
            List<WebElement> paragraphs = resetDriver.findElements(By.tagName("p"));

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + chapTitle.get(i) + " (HTML).txt"))) {
                for (WebElement p : paragraphs) {
                    String text = p.getAttribute("outerHTML");
                    if (!text.isEmpty()) {
                        writer.write(text);
                        writer.newLine();
                        writer.write("<br>");
                        writer.newLine();
                    }
                }
                System.out.println("✅ " + chapTitle.get(i) + " saved.");
                resetDriver.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadIndivChapterHTML(WebDriver driver, String url) {
        driver.get(url);
        driver.manage().window().maximize();
        waitDriver(driver);
        int chapNum = 1;
        // Collect all <p> text
        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter( "Chapter" + chapNum + ".txt"))) {
            for (WebElement p : paragraphs) {
                String text = p.getAttribute("outerHTML");
                if (!text.isEmpty()) {
                    writer.write(text);
                    writer.newLine();
                    writer.write("<br>");
                    writer.newLine();
                }
            }
            System.out.println("✅ " + "Chapter" + chapNum + " saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void allLinksToTxt(WebDriver driver, String fileName) throws InterruptedException {
        //Find all chapter links
        Thread.sleep(10000);
        //waitDriver(mainDriver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/chapter-']")));
        List<WebElement> chapterElements = driver.findElements(By.cssSelector("a[href*='chapter-']"));
        waitDriver(driver);

        List<String> chapterLinks = new ArrayList<>();

        for (WebElement el : chapterElements) {
            String href = el.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                chapterLinks.add(href);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            for (String link : chapterLinks) {
                if (!link.isEmpty()) {
                    writer.write(link);
                    writer.newLine();
                }
            }
           System.out.println("✅ " + fileName + " saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void titleDownloader(WebDriver driver, String fileName) throws InterruptedException {
        //Find all chapter links
        Thread.sleep(10000);
        //waitDriver(mainDriver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/chapter-']")));
        List<WebElement> chapterElements = driver.findElements(By.cssSelector("a[href*='chapter-']"));
        waitDriver(driver);

        List<String> chapterTitle = new ArrayList<>();

        for (WebElement el : chapterElements) {
            String title = el.getText().replaceAll("[\\\\/:*?\"<>|]", "");
            if (title != null && !title.isEmpty()) {
                chapterTitle.add(title);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            for (String title : chapterTitle) {
                if (!title.isEmpty()) {
                    writer.write(title);
                    writer.newLine();
                }
            }
            System.out.println("✅ " + fileName + " saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
