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

        System.out.println("1. Download all links\n2. Download all titles\n3. Download chapters");
        int options = in.nextInt();
        in.nextLine();

        if (options == 1) {
            System.out.print("Enter file name: ");
            String fileName = in.nextLine();
            System.out.print("Enter link: ");
            String link = in.nextLine();

            nbd.webAccessor(link, driver);

            try {
                nbd.allLinksToTxt(driver, fileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nbd.webCloser(driver);
        }

        else if (options == 2) {
            System.out.print("Enter file name: ");
            String fileName = in.nextLine();
            System.out.print("Enter link: ");
            String link = in.nextLine();

            nbd.webAccessor(link, driver);

            try {
                nbd.titleDownloader(driver, fileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nbd.webCloser(driver);

        }

        else if (options == 3) {
            System.out.print("Enter novel title: ");
            String novelTitle = in.nextLine();
            String saveDir = userHome + "\\Desktop\\downloaded novels\\" + novelTitle + "\\";
            new File(saveDir).mkdirs();
            System.out.print("Enter file name for chapter content link: ");
            String fileNameLink = in.nextLine();
            System.out.print("Enter file name for chapter title: ");
            String fileNameTitle = in.nextLine();

            try {
                nbd.downloadAllChaptersHTMLver2(saveDir, fileNameLink, fileNameTitle);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        nbd.webCloser(driver);
    }
}