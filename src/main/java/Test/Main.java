package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.setProperty("webdriver.chrome.mainDriver", "C:\\Browser mainDriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("https://global.novelpia.com/viewer/302745");
        Thread.sleep(10000);
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
                        Thread.sleep(300); // small delay to allow UI update
                    } catch (Exception e) {
                        // ignore in case popup disappeared too quickly
                    }
                }

            } catch (Exception e) {
                // ignore and continue
            }
        }

        System.out.println("Done closing popups (or timeout reached).");

        WebElement body = driver.findElement(By.tagName("body"));
        body.click();
        driver.findElement(By.className("next")).click();
    }
}
