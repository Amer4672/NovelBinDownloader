package Test;

import java.util.Scanner;

public class main2 {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        /*String userHome = System.getProperty("user.home");

        System.out.print("Enter novel title: ");
        String novelTitle = in.nextLine();
        String saveDir = userHome + "\\Desktop\\downloaded novels\\" + novelTitle + "\\";
        new File(saveDir).mkdirs();*/
        System.out.print("Enter link: ");
        String link = in.nextLine();
        System.out.print("Start at chapter: ");
        int startAt= in.nextInt();

        NovelBinDownloaderCleaned nbdc = new NovelBinDownloaderCleaned(link, startAt);
    }
}
