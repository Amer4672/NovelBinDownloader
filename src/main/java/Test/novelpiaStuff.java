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
import java.util.Scanner;

public class novelpiaStuff {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        String userHome = System.getProperty("user.home");
        System.setProperty("webdriver.chrome.driver", "C:\\Browser driver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + userHome + "\\AppData\\Local\\Google\\Chrome\\SeleniumProfile");
        options.addArguments("profile-directory=Default");
        options.addArguments("start-maximized"); // open Browser in maximized mode
      //  options.addArguments("disable-infobars"); // disabling infobars
      //  options.addArguments("--disable-extensions"); // disabling extensions
       // options.addArguments("--disable-gpu"); // applicable to windows os only
        //options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
       // options.addArguments("--no-sandbox"); // Bypass OS security model
       // options.addArguments("--remote-debugging-port=9222");

        WebDriver driver = new ChromeDriver(options);


        //login
        driver.get("https://global.novelpia.com/");
       /* driver.findElement(By.className("signin-btn")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.sendKeys(userHome);
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys(userHome);
        WebElement loginBtn = driver.findElement(By.id("login_btn"));
        loginBtn.click();*/
/*
        //change the novel name for different novels
        String savePath = userHome + "\\Desktop\\downloaded novels\\novelpia\\My EX Has Been Taken\\";
        new File(savePath).mkdirs();

        //change link for diff novel
        driver.get("https://global.novelpia.com/viewer/302745");
        driver.manage().window().maximize();
        Thread.sleep(5000);

        //insert how many chapters
        for (int i = 0 ; i < 5 ; i++) {
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

            //click the page to reveal the other stuff
            WebElement body = driver.findElement(By.tagName("body"));
            body.click();

            //find title and get it
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement titleElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.viewer-ep-tit"))
            );
            String textTitle = titleElement.getText();
            System.out.println(textTitle); // Output: "Ch.0) Prologue"

            //find paragraphs
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.tagName("p"))
            );
            List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
            List<String> paragraphTexts = new ArrayList<>();

            String fileName = savePath + textTitle + " (HTML).txt";
            File file = new File(fileName);

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Extract all text immediately to avoid stale references
                for (WebElement p : paragraphs) {
                    try {
                        String text = p.getAttribute("outerHTML");
                        if (!text.isEmpty()) {
                            paragraphTexts.add(text);
                        }
                    } catch (Exception e) {
                        System.out.println("Skipped a stale paragraph element during extraction");
                    }
                }

                // Now write all the extracted text
                for (String text : paragraphTexts) {
                    writer.write(text);
                    writer.newLine();
                    writer.write("<br>");
                    writer.newLine();
                }
                System.out.println("✅ " + textTitle + " saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
            WebElement nextButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector(".viewer-btn.next"))
            );
            nextButton.click();
            Thread.sleep(5000);
            System.out.println("Next button clicked.");
        }*/
    }
}
