package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class novelpiaStuff {
    static WebDriver driver;
    static int smallChap = 0;

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        Random rand = new Random();
        String userHome = System.getProperty("user.home");
        System.setProperty("webdriver.chrome.driver", "C:\\Browser driver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + userHome + "\\AppData\\Local\\Google\\Chrome\\SeleniumProfile");
        options.addArguments("profile-directory=Default");
        //options.addArguments("--headless");
        options.addArguments("start-maximized"); // open Browser in maximized mode
      //  options.addArguments("disable-infobars"); // disabling infobars
      //  options.addArguments("--disable-extensions"); // disabling extensions
       // options.addArguments("--disable-gpu"); // applicable to windows os only
        //options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
       // options.addArguments("--no-sandbox"); // Bypass OS security model
       // options.addArguments("--remote-debugging-port=9222");

        driver = new ChromeDriver(options);
        /*//driver quit when program terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            driver.quit();
        }));*/

        //change the novel name for different novels
        String novelTitle = "Becoming the Financial Director of an Academy on a Brink of Bankruptcy"; //swap
        String savePath = userHome + "\\Desktop\\downloaded novels\\novelpia\\" + novelTitle + "\\";
        new File(savePath).mkdirs();

        //change link for diff novel
        driver.get("https://global.novelpia.com/viewer/253623");//cswap
        driver.manage().window().maximize();
        Thread.sleep(rand.nextInt(2000) + 3000);

        //insert how many chapters
        String prevTitle = "";
        int errorCnt = 0;

        //loop trhrough each iteration of the page until it can move onto next
        while (true) {
            //click the page to reveal the other stuff
            Thread.sleep(rand.nextInt(2000) + 1000);
            WebElement body = driver.findElement(By.tagName("body"));
            body.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String textTitle;
            //find title and get it
            while (true) {
                try {
                    WebElement titleElement = wait.until(
                            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.viewer-ep-tit"))
                    );
                    textTitle = titleElement.getText();
                    System.out.println(textTitle); // Output: "Ch.0) Prologue"
                    break;
                } catch (Exception e) {
                    errorCnt++;
                    System.out.println("Errors: " + errorCnt);
                    System.out.println("Retrying title.");
                    driver.navigate().refresh();
                    Thread.sleep(rand.nextInt(2000) + 2000);
                    WebElement body2 = driver.findElement(By.tagName("body"));
                    body2.click();
                }
            }

            //find paragraphs
            //wait.until(
            //        ExpectedConditions.visibilityOfElementLocated(By.tagName("p"))
            //);
            Thread.sleep(rand.nextInt(2000) + 3000);
            List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
            List<String> paragraphTextsHTML = new ArrayList<>();
            List<String> paragraphTexts = new ArrayList<>();
            String filename = savePath + textTitle.replaceAll("[\\\\/:*?\"<>|]", "");
            String fileNameHTML = filename + ".html";
            String fileNameTxt = filename + ".txt";

            //HTML txt file save
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameHTML))) {
                // Extract all text immediately to avoid stale references
                writer.write(textTitle);
                writer.newLine();
                writer.newLine();
                for (WebElement p : paragraphs) {
                    try {
                        String text = p.getAttribute("outerHTML");
                        if (!text.isEmpty()) {
                            paragraphTextsHTML.add(text);
                                writer.write(text);
                                writer.newLine();
                                writer.write("<br>");
                                writer.newLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Skipped a stale paragraph element during extraction");
                    }
                }

                // Now write all the extracted text
                /*for (String text : paragraphTextsHTML) {
                    writer.write(text);
                    writer.newLine();
                    writer.write("<br>");
                    writer.newLine();
                }*/
                System.out.println("✅ " + textTitle + " (HTML) saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //just txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameTxt))) {
                writer.write(textTitle);
                writer.newLine();
                writer.newLine();
                // Extract all text immediately to avoid stale references
                for (WebElement p : paragraphs) {
                    try {
                        String text = p.getText();
                        if (!text.isEmpty()) {
                            paragraphTexts.add(text);
                            writer.write(text);
                            writer.newLine();
                            writer.newLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Skipped a stale paragraph element during extraction");
                    }
                }

                // Now write all the extracted text
                /*for (String text : paragraphTexts) {
                    writer.write(text);
                    writer.newLine();
                    writer.newLine();
                }*/
                System.out.println("✅ " + textTitle + " saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(rand.nextInt(1000) + 1000);

            File file = new File(fileNameTxt);
            long fileSize = file.length();

            //repeat cycle unless new chap content successfully saved
            if (prevTitle.equalsIgnoreCase(textTitle) || fileSize < 2048) {
                if (smallChap < 4) {
                    smallChap++;
                    System.out.println("Small chapter, retrying");
                    driver.navigate().refresh();
                    continue;
                }
            }

            prevTitle = textTitle;

            //check if its the last chapter, if yes exit program
            try {
                WebElement lastChap = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("next-epi-btn")));
                String lastChapText = lastChap.getText();
                if (lastChapText.equals("This is the last chapter")) {
                    System.out.println("Last chapter exit triggered");
                    System.exit(0);
                }
            } catch (Exception e) {
                System.out.println();
            }

            while (true) {
                try {
                    WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".viewer-btn.next")));
                    nextButton.click();
                    Thread.sleep(rand.nextInt(2000) + 3000);
                    System.out.println("Next button clicked.");
                    smallChap = 0; //reset the check for small chapters when it goes to next chapter
                    break;
                } catch (Exception e) {
                    errorCnt++;
                    System.out.println("Errors: " + errorCnt);
                    System.out.println("Retrying next button.");
                    driver.navigate().refresh();
                    Thread.sleep(rand.nextInt(2000) + 3000);
                    WebElement body3 = driver.findElement(By.tagName("body"));
                    body3.click();
                }
            }
            if (errorCnt == 20) {System.exit(0);}
        }
    }








    public void login(String email, String password ) {
        //login
        //driver.get("https://global.novelpia.com/");
        driver.findElement(By.className("signin-btn")).click();
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(email);
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        WebElement loginBtn = driver.findElement(By.id("login_btn"));
        loginBtn.click();
    }

    public void closePopups() {
        //Thread.sleep(10000);
        long endTime = System.currentTimeMillis() + 15000; // max 15s to handle all popups

            while (System.currentTimeMillis() < endTime) {
                try {
                    // Find all close buttons currently visible
                    List<WebElement> closeButtons = driver.findElements(
                            By.cssSelector("button > i.fi-rr-cross")
                    );

                    if (closeButtons.isEmpty()) {
                        // no more popups, exit loop
                        break;
                    }

                    // click all found buttons
                    for (WebElement btn : closeButtons) {
                        try {
                            btn.click();
                            System.out.println("Closed one popup");
                            Thread.sleep(3000); // small delay to allow UI update
                        } catch (Exception e) {
                            // ignore in case popup disappeared too quickly
                        }
                    }

                } catch (Exception e) {
                    // ignore and continue
                }
            }

            System.out.println("Done closing popups (or timeout reached).");
    }
}
