package Test;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

       Scanner in = new Scanner(System.in);
        String userHome = System.getProperty("user.home");

        NovelBinDowloader nbd = new NovelBinDowloader();
        WebDriver driver = nbd.chromeDriverSetup();

        System.out.print("Enter novel title: ");
        String saveDir = userHome + "\\Desktop\\downloaded_novels\\" + in.nextLine().trim() + "\\";
        new File(saveDir).mkdirs();

        System.out.print("Enter link: ");
        String link = in.nextLine();

        nbd.downloadChapter(driver, link);
/*
        nbd.webAccessor(link, driver);
        try {
            nbd.downloadAllChapters(driver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        nbd.webCloser(driver);*/
    }
}