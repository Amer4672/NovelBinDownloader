package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NovelBinDownloaderCleaned {

    //webdriver stuff///////////////////////////////////////////////////////////////////////////////////////
    static final String[] USER_AGENT = {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 OPR/120.0.0.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0",
            "Mozilla/5.0 (X11; CrOS x86_64 14541.0.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.3.1 Safari/605.1.15",
            "Mozilla/5.0 (Linux; Android 15; SM-S931B Build/AP3A.240905.015.A2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/127.0.6533.103 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; Android 15; SM-S931U Build/AP3A.240905.015.A2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/132.0.6834.163 Mobile Safari/537.36",
            "Mozila/5.0 (Linux; Android 14; SM-S928B/DS) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.6099.230 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; Android 13; SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; Android 14; Pixel 9 Pro Build/AD1A.240418.003; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/124.0.6367.54 Mobile Safari/537.36"};
    static final Random rand = new Random();
    List<String> chapterLinks = new ArrayList<>();
    List<String> chapterTitles = new ArrayList<>();
    WebDriver mainDriver;
    List<WebElement> chapterElements = new ArrayList<>();
    //int startAt;
    //String mainPageURL;
    //String path;

    public NovelBinDownloaderCleaned(String mainPageURL, int startAt) throws InterruptedException {
        mainDriver = chromeDriverSetup();
        webAccessor(mainDriver, mainPageURL);
        Thread.sleep(5000);
        String path = dirSetup(mainDriver);//setup file and returns path
        elementGatherer();
        chapLinksAndTitlesToList();
        webCloser(mainDriver);

        for (int i = 1 + startAt; i < chapterLinks.size(); i++) {
            for (int j = 0; j < 12; j++) {
                System.out.println();
                WebDriver newDriver = chromeDriverSetup();
                webAccessor(newDriver, chapterLinks.get(i));
                Thread.sleep(2000);
                try {
                    File file = saveChapterContent(newDriver, path, i);//return file name and downloads the file
                    webCloser(newDriver);
                    long size = file.length();
                    if (size > 2048) {
                        break;
                    }
                } catch (Exception e) {
                    webCloser(newDriver);
                    continue;
                }
                if ((j + 1) % 4 == 0) {
                    Thread.sleep(rand.nextInt(3000) + 5000);
                }
                System.out.println("Retrying");
                webCloser(newDriver);
            }
            if(i % 4 == 0) {
                Thread.sleep(rand.nextInt(3000) + 5000);
            }
        }
        webCloser(mainDriver);
    }

    //constructor for testing purposes
    public NovelBinDownloaderCleaned(String mainPageURL, int startAt, int filler) throws InterruptedException {
        mainDriver = chromeDriverSetup();
        webAccessor(mainDriver, mainPageURL);
        Thread.sleep(5000);
        String path = dirSetup(mainDriver);//setup file and returns path
        elementGatherer();
        chapLinksAndTitlesToList();
        webCloser(mainDriver);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 1 + startAt; i < chapterLinks.size(); i++) {
            final int chapterIndex = i;
            executor.submit(() -> {
                try {
                    downloadChapter(path, chapterIndex);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public void downloadChapter(String path, int index) throws InterruptedException {
        for (int j = 0; j < 12; j++) {
            System.out.println();
            WebDriver newDriver = chromeDriverSetup();
            Thread.sleep(3000);
            webAccessor(newDriver, chapterLinks.get(index));
            Thread.sleep(2000);
            try {
                File file = saveChapterContent(newDriver, path, index);//return file name and downloads the file
                webCloser(newDriver);
                Thread.sleep(1000);
                long size = file.length();
                if (size > 2048) {
                    break;
                }
            } catch (Exception e) {
                webCloser(newDriver);
                continue;
            }
            if ((j + 1) % 4 == 0) {
                Thread.sleep(rand.nextInt(3000) + 5000);
            }
            System.out.println("Retrying");
            webCloser(newDriver);
        }
    }
    public WebDriver chromeDriverSetup() {
        System.setProperty("webdriver.chrome.mainDriver", "C:\\Browser mainDriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        String userAgent = USER_AGENT[rand.nextInt(USER_AGENT.length)];
        options.addArguments("--user-agent=" + userAgent);
        //options.addArguments("--headless");
        System.out.println("User Agent in use: " + userAgent);

        WebDriver newDriver = new ChromeDriver(options);
        return newDriver;
    }

    public WebDriverWait waitDriver() { /*WebDriverWait*/
        Random rand = new Random();
        WebDriverWait wait = new WebDriverWait(mainDriver, Duration.ofSeconds(rand.nextInt(10) + 10));
        return wait;//return wait so that i can do expected conditions
    }

    public void webAccessor(WebDriver driver, String url) throws InterruptedException {
        driver.get(url);
        //driver.manage().window().maximize();
        //waitDriver(mainDriver);
    }

    public void webCloser(WebDriver driver) {
        driver.quit();
    }

    public File saveChapterContent(WebDriver driver, String path, int index) throws InterruptedException {
        String title = chapterTitleGet(driver);

        //System.out.println(chapterTitles.get(index));
        System.out.println(chapterLinks.get(index));

        // Collect all <p> text
        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));

        String fileName = path + title + " (HTML).txt";
        File file = new File(fileName);

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (WebElement p : paragraphs) {
                String text = p.getAttribute("outerHTML");
                if (!text.isEmpty()) {
                    writer.write(text);
                    writer.newLine();
                    writer.write("<br>");
                    writer.newLine();
                }
            }
            System.out.println("✅ " + title + " saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void elementGatherer() throws InterruptedException {
        //Find all chapter links
        //waitDriver(mainDriver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/chapter-']")));
        chapterElements = mainDriver.findElements(By.cssSelector("a[href*='chapter-']"));
        //chapterElements = mainDriver.findElements(By.tagName("a"));
        waitDriver();
    }

    public void chapLinksAndTitlesToList() throws InterruptedException {
        for (WebElement el : chapterElements) {
            String title = el.getText().replaceAll("[\\\\/:*?\"<>|]", "");
            if (title != null && !title.isEmpty()) {
                chapterTitles.add(title);
            }
            String href = el.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                chapterLinks.add(href);
            }
        }
    }

    public String chapterTitleGet(WebDriver driver) throws NoSuchElementException {
        String title = driver.findElement(By.cssSelector("span.chr-text")).getText().replaceAll("[\\\\/:*?\"<>|]", "");
        return title;
    }

    public String novelTitleGet(WebDriver driver) {
        //String title = driver.findElement(By.cssSelector("h3.title")).getText().replaceAll("[\\\\/:*?\"<>|]", "");
        WebElement el = driver.findElement(By.cssSelector("h3.title"));
        String title = el.getAttribute("textContent")
                .replaceAll("[\\\\/:*?\"<>|]", "")
                .trim();
        return title;
    }

    public String dirSetup(WebDriver driver) throws InterruptedException {
        String novelTitle = novelTitleGet(driver);
        String userHome = System.getProperty("user.home");
        String savePath = userHome + "\\Desktop\\downloaded novels\\" + novelTitle + "\\";
        //System.out.println(novelTitle);
        new File(savePath).mkdirs();
        return savePath;
    }

    /*public void downloadAllChaptersHTML(String url, String saveDir, int startAt, String path) throws InterruptedException {
        webAccessor(mainDriver, url);
        elementGatherer();
        chapLinksAndTitlesToList();
        webCloser(mainDriver);

        int multiplier;
        for (int i = 0; i < chapterLinks.size()/4; i++) {
            multiplier = i * 4;

            List<WebDriver> newDriversList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                WebDriver newDriver = chromeDriverSetup();
                newDriversList.add(newDriver);//4 new drivers separate from the main mainDriver
                //newDrivers.get(insert no.); will correspond to each mainDriver
            }
            for (int j = 0; j < newDriversList.size(); j++) {
                webCloser(newDriversList.get(j));
                webAccessor(newDriversList.get(j * multiplier), chapterLinks.get(j * multiplier));
                saveChapterContent(newDriversList.get(j), saveDir, j);
            }
        }

        saveChapterContent(mainDriver, path, index);
        webCloser(mainDriver);
    }*/

   /* public void saveChapterContent4AtOnce
            (WebDriver mainDriver, String path, List<String> chapterTitle, List<String> chapterLinks, int startAt)
            throws InterruptedException {

        for (int i = 0; i < chapterLinks.size()/4; i++) { //int i = 2 to skip the read now and latest chapter //chapterLinks.size()
            System.out.println(chapterTitle.get(i));
            System.out.println(chapterLinks.get(i));

            List<WebDriver> newDrivers = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                WebDriver resetDriver = chromeDriverSetup();
                newDrivers.add(resetDriver);
                //newDrivers.get(insert no.); will correspond to each mainDriver
            }

            //WebDriver resetDriver = chromeDriverSetup();
            Thread.sleep(rand.nextInt(5000) + 3000);

            int multiplier = i * 4;

            newDrivers.get(i).get(chapterLinks.get(i));
            newDrivers.get(i + 1).get(chapterLinks.get(i +1));
            newDrivers.get(i + 2).get(chapterLinks.get(i + 2));
            newDrivers.get(i + 3).get(chapterLinks.get(i + 3));

            // mainDriver.get(chapterLinks.get(i));
            //waitDriver(resetDriver);

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
                System.out.println("✅ " + chapterTitle.get(i) + " saved.");
                webCloser(resetDriver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void saveChapterContent
    (WebDriver mainDriver, String path, List<String> chapterTitles, List<String> chapterLinks, int startAt, int index)
    throws InterruptedException {

        for (int i = 1 + startAt; i < chapterLinks.size(); i++) { //int i = 2 to skip the read now and latest chapter //chapterLinks.size()
            System.out.println(chapterTitles.get(i));
            System.out.println(chapterLinks.get(i));

            WebDriver resetDriver = chromeDriverSetup();
            Thread.sleep(rand.nextInt(5000) + 3000);
            resetDriver.get(chapterLinks.get(i));
            // mainDriver.get(chapterLinks.get(i));
            waitDriver(resetDriver);

            // Collect all <p> text
            List<WebElement> paragraphs = resetDriver.findElements(By.tagName("p"));

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + chapterTitles.get(i) + " (HTML).txt"))) {
                for (WebElement p : paragraphs) {
                    String text = p.getAttribute("outerHTML");
                    if (!text.isEmpty()) {
                        writer.write(text);
                        writer.newLine();
                        writer.write("<br>");
                        writer.newLine();
                    }
                }
                System.out.println("✅ " + chapterTitles.get(i) + " saved.");
                webCloser(resetDriver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}