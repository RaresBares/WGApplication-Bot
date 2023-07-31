package org.example;


import org.apache.commons.io.IOUtils;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.example.utilities.Email;
import org.example.utilities.Pair;
import org.example.utilities.Utilities;
import org.example.utilities.WG;
import org.example.websites.FlatFox;
import org.example.websites.WGZimmer;
import org.example.websites.Woko;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import static org.example.Files.saveJSON;
import static org.example.websites.WGZimmer.searchOnWGZ;


public class Upload {

    public static JSONObject json;
    public static HashMap<String, WG> wgs = new HashMap<String, WG>();
    public static ArrayList<WG> news = new ArrayList<>();

    private static WebDriver ffdriver;
    private static WebDriver wgdriver;
    private static FirefoxOptions options;

    public static void main(String[] args) throws IOException, InterruptedException {

        if(Files.loadPropFile()) {
            setupFFSelenium();
            setupWGZSelenium();


            Thread ff = new Thread(new Runnable() {
                @Override
                public void run() {

                    int i = 1;
                    while (true) {
                        System.out.println("\n\n\nFlatFox search number " + i + " starting\n\n\n");
                        try {
                            FlatFox.applyOnFlatFox(ffdriver);
                        } catch (Exception e) {
                            System.out.println("\n\n\nFlatFox gescheitert.... starte neu....\n\n\n");
                            e.printStackTrace();
                            Statistics.Errors++;
                            try {
                                Thread.sleep(120 * 1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        try {
                            System.out.println("\n\n\nFlatFox search number " + i + " finished\n\n\n");
                            i++;
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
            );

            Thread wgz = new Thread(new Runnable() {
                @Override
                public void run() {

                    int i = 1;
                    while (true) {
                        System.out.println("\n\n\nWGZimmer search number " + i + " starting\n\n\n");
                        try {
                            WGZimmer.searchOnWGZ(wgdriver);
                        } catch (Exception e) {
                            System.out.println("\n\n\nWGZimmer gescheitert.... starte neu....\n\n\n");
                            e.printStackTrace();
                            Statistics.Errors++;
                            try {
                                Thread.sleep(120 * 1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        try {
                            System.out.println("\n\n\nWGZimmer search number " + i + " finished\n\n\n");
                            i++;
                            Thread.sleep(180000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            );


            Thread woko = new Thread(new Runnable() {
                @Override
                public void run() {

                    int i = 1;
                    while (true) {
                        System.out.println("\n\n\nWoko search number " + i + " starting\n\n\n");
                        try {
                            for (Pair<String, String> cred : Woko.getCreds()) {
                                Woko.apply(cred);
                            }
                        } catch (Exception e) {
                            System.out.println("\n\n\nWoko gescheitert.... starte neu....\n\n\n");
                            try {
                                Thread.sleep(180000*3);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            Statistics.Errors++;
                            e.printStackTrace();
                            try {
                                Thread.sleep(120 * 1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        try {
                            System.out.println("\n\n\nWoko search number " + i + " finished\n\n\n");
                            i++;
                            Thread.sleep(180000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            );


            Thread info = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5 * 60 * 1000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    while (true) {



                        Email.sendEmail("raresmagbares@gmail.com", "Update", "Der stündliche application report ist da!\n" +
                                " \n" +
                                "neue FlatFox-Bewerbungen: " + Statistics.FlatFox + "\n" +
                                "neue WGZimmer-Bewerbungen: " + Statistics.WGZimmer + "\n" +
                                "neue Woko-Bewerbungen: " + Statistics.Woko + "\n" +
                                "Bewerbungen gesamt: " + wgs.keySet().size() + "\n" +
                                " \n" +
                                "Errors: " + Statistics.Errors + "\n" +
                                "\n" +
                                "Fertig!");

                        Email.sendEmail("csahleanu@gmail.com", "Update", "Der stündliche application report ist da!\n" +
                                " \n" +
                                "neue FlatFox-Bewerbungen: " + Statistics.FlatFox + "\n" +
                                "neue WGZimmer-Bewerbungen: " + Statistics.WGZimmer + "\n" +
                                "neue Woko-Bewerbungen: " + Statistics.Woko + "\n" +
                                "Bewerbungen gesamt: " + wgs.keySet().size() + "\n" +
                                " \n" +
                                "Errors: " + Statistics.Errors + "\n" +
                                "\n" +
                                "Fertig!");


                            Statistics.reset();
                        try {
                            Thread.sleep(3600000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                    }

                }
            });


            info.start();
            woko.start();
            ff.start();
            wgz.start();
            //driver.close();
        }else {

            System.out.println("file not exist. pls fill in props");

        }
    }

    private static void setupFFSelenium() {
        System.setProperty("webdriver.gecko.driver", Utilities.GeckoPath);
        options = new FirefoxOptions();
        options.addArguments("user-agent=user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
       options.setBinary(Utilities.FireFoxPath);

        try {
            ffdriver = new FirefoxDriver(options);
        }catch ( Exception e){

            e.printStackTrace();

        }
        ffdriver.manage().window().maximize();
    }
    private static void setupWGZSelenium() {
        System.setProperty("webdriver.gecko.driver", Utilities.GeckoPath);
        options = new FirefoxOptions();
        options.addArguments("user-agent=user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
       options.setBinary(Utilities.FireFoxPath);


        wgdriver = new FirefoxDriver(options);
        wgdriver.manage().window().maximize();
    }
}
