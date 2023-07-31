package org.example.websites;

import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.example.Statistics;
import org.example.utilities.Utilities;
import org.example.utilities.WG;
import org.example.utilities.WGType;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import static org.example.Files.loadJSON;
import static org.example.Files.saveJSON;
import static org.example.Upload.*;

public class WGZimmer {





    public static void searchOnWGZ(WebDriver driver) throws IOException, InterruptedException {
        loadJSON();


        driver.manage().window().maximize();
        driver.manage().window().setSize(new Dimension(1920, 1300));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));



        for (int l = 43; l <= 49; l++) {
            driver.manage().deleteAllCookies();
            driver.navigate().to("https://www.wgzimmer.ch/wgzimmer/search/mate.html");
            try {


                Select maxPreis = new Select(driver.findElement(By.xpath("//*[@id=\"searchMateForm\"]/fieldset/div[3]/div/select")));
                maxPreis.selectByIndex(10);


            } catch (Exception e) {


                Thread.sleep(10000);

                driver.manage().deleteAllCookies();
                driver.navigate().to("https://www.wgzimmer.ch/wgzimmer/search/mate.html");

            }
            Select region = new Select(driver.findElement(By.xpath("//*[@id=\"selector-state\"]")));
            Thread.sleep(1000);
            String loc = "Zürich (Stadt)";

            switch (l - 1) {
                case 42:
                    loc = "Zug";
                    break;
                case 43:
                    loc = "Zürich (Stadt)";
                    break;
                case 44:
                    loc = "Zürich Hongg";
                    break;
                case 45:
                    loc = "Zürich Oerlikon";
                    break;
                case 46:
                    loc = "Zürich RuS";
                    break;
                case 47:
                    loc = "Zürich Aeug";
                    break;
                case 48:
                    loc = "Zürich Unterland";
                    break;
                case 49:
                    loc = "Zürich Oberland";
                    break;

            }


            region.selectByIndex(l);

            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"permanent\"]")).click();


            Thread.sleep(1000);

            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/div[3]/form/fieldset/div[9]/input")));

            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/div[3]/form/fieldset/div[9]/input")).click();

            ArrayList<WebElement> houses = (ArrayList<WebElement>) driver.findElements(By.xpath("//ul[@class='list']/li"));
            for (WebElement house : houses) {


                if (house.findElements(By.xpath("./child::*")).size() > 1) {
                    String link = house.findElements(By.xpath("./child::*")).get(2).getAttribute("href");
                    String price = house.getText().split("fristet\n")[1].split("\n")[0];
                    WG found = new WG(-999, loc, link, WGType.WGZIMMER);

                    try {
                        found = new WG(Integer.parseInt(price), loc, link, WGType.WGZIMMER);
                    } catch (Exception e) {

                    }
                    if (!wgs.containsKey(link)) {

                        System.out.println("Neue WG: " + link);
                        news.add(found);
                        wgs.put(link, found);
                    }
                }
            }
            for (WG wg : news) {


                apply(driver, wg);

                    Thread.sleep(10000);

            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {


            }
            news.clear();


            saveWgs();
            saveJSON();
            System.out.println("JSON gespeichert");


            Thread.sleep(10000);


        }

        System.out.println("WGZimmer.ch abgesucht. Aktuelle angeschriebene WGs: " + wgs.keySet().size());
        //  Thread.sleep(120000);


    }
    public static void apply(WebDriver driver, WG wg) {

        driver.navigate().to(wg.link);
        String txt = driver.findElement(By.cssSelector("#content > div.text.result.nbt > div.wrap.col-wrap.room-content > p")).getText() +
                driver.findElement(By.cssSelector("#content > div.text.result.nbt > div.wrap.col-wrap.mate-content.nbb > p")).getText();
        if (shouldEnglish(txt)) {
            wg.lang = "eng";
        } else {

            wg.lang = "de";

        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        driver.findElement(By.cssSelector("a.small-link:nth-child(1)")).click();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(By.xpath("//*[@id=\"senderName\"]")).sendKeys("Rares Sahleanu");
        driver.findElement(By.xpath("//*[@id=\"senderEmail\"]")).sendKeys(Utilities.Email);
        driver.findElement(By.xpath("//*[@id=\"senderPhone\"]")).sendKeys("+49 1523 7975992");
        String name = driver.findElement(By.xpath("//*[@id=\"contact-detail\"]/div[1]/p/strong")).getText();
        if (wg.lang == "de") {

            driver.findElement(By.xpath("//*[@id=\"senderText\"]")).sendKeys(Utilities.getMessageDE(name));

        } else {

            driver.findElement(By.xpath("//*[@id=\"senderText\"]")).sendKeys(Utilities.getMessageEN(name));
        }
        driver.findElement(By.cssSelector(".submit-inline-mail")).click();
        System.out.println("Schicke Bewerbung an: " + name);
        Statistics.WGZimmer++;
    }

    public static void saveWgs() {


        for (WG wg : wgs.values()) {
            if (!json.toString().contains(wg.link)) {
                json.getJSONArray("wgs").put(wg.writeToJsonMap());
            }
        }

    }

    public static boolean shouldEnglish(String txt) {

        LanguageDetector detector = new OptimaizeLangDetector().loadModels();

        if (detector.detect(txt).getLanguage() == "en") {
            System.out.println(detector.detect(txt).getConfidence() + " -- " + detector.detect(txt).getLanguage());
            return true;

        } else {
            System.out.println(detector.detect(txt).getConfidence() + " -- " + detector.detect(txt).getLanguage());
            return false;
        }


    }

}
