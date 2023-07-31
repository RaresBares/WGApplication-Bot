package org.example.websites;

import com.squareup.okhttp.*;
import org.example.Statistics;
import org.example.utilities.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;

public class FlatFox {


    public static void applyOnFlatFox(WebDriver driver) throws Exception {
        for (String flatFoxWG : getFFList()) {
            applyWG(driver, flatFoxWG);
        }
    }


    public static ArrayList<String> getFFList() throws IOException {

        OkHttpClient client = new OkHttpClient();
        ArrayList<String> wg = new ArrayList<>();
        Request request = new Request.Builder()
                .url("\n" +
                        "https://flatfox.ch/api/v1/pin/?east=8.678528&is_temporary=false&max_count=400&max_price=800&north=47.570063&object_category=SHARED&ordering=date&south=47.193554&west=8.351898")
                .get()
                .addHeader("authority", "flatfox.ch")
                .addHeader("accept", "application/json")
                .addHeader("accept-language", "de")
                .addHeader("content-type", "application/json")
                .addHeader("referer", "https://flatfox.ch/de/search/?east=8.669998&is_temporary=false&max_price=800&north=47.436858&object_category=SHARED&ordering=date&query=Z%C3%BCrich&south=47.345333&west=8.357926")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .addHeader("x-datasource", "web")
                .addHeader("x-flatfox-org-switcher-owner", "1906487")
                .build();

        Response response = client.newCall(request).execute();
        String[] raw = response.body().string().split("pk\":");
        System.out.println("Insgesamt gibt es auf FlatFox.ch " + raw.length + " Inserate!");
        for (int i = 1; i < raw.length; i++) {
            wg.add(raw[i].split(",\"lat")[0]);
        }
        return wg;
    }


    public static void applyWG(WebDriver driver, String token) throws InterruptedException {
        try {


            driver.manage().deleteAllCookies();


            driver.navigate().to("https://flatfox.ch/de/" + token + "/m/");
          //COOKIES

            driver.navigate().refresh();
            driver.findElement(By.cssSelector("#id_name")).clear();
            driver.findElement(By.cssSelector("#id_name")).sendKeys("Rares Sahleanu");
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("#id_email")).clear();
            driver.findElement(By.cssSelector("#id_email")).sendKeys("raresmagbares@gmail.com");
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("#id_phone_number")).sendKeys("+49 15237975992");
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("#id_text")).clear();
            driver.findElement(By.cssSelector("#id_text")).sendKeys(Utilities.getMessageWithout());
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("div.fui-stack:nth-child(2) > form:nth-child(1) > div:nth-child(1) > div:nth-child(1) > button:nth-child(8)")).click();
            Statistics.FlatFox++;

        } catch (Exception e) {

        }


    }

    /*    Experimental   */
    /*public static void applyFLatFox(String link) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "csrfmiddlewaretoken=2U3iS1N4o2UsBTynbW51C7NmLe6m9myUq9B19QPIsN3Y7R7rGGW6SrOVWYnYjHL4&name=RaresBares&email=raresmagbares%40gmail.com&phone_number=+49 15237975992&text=" + Utilities.getMessageWithout() + "&create_subscription=on&=contact-advertiser%3D");
        Request request = new Request.Builder()
                .url(link)
                .post(body)
                .addHeader("cookie", "cid=c8b59b8ae31f4ebaa2603b51ac627afe; flatfoxDevice=6dc4f8526c014dec81632bc4b58a5467; _gcl_au=1.1.1210955965.1689368360; _fbp=fb.1.1689368360015.831637797; csrftoken=2U3iS1N4o2UsBTynbW51C7NmLe6m9myUq9B19QPIsN3Y7R7rGGW6SrOVWYnYjHL4; sessionid=k5h599djnna4ozqtrr5xgcdhatx68vmu; _gid=GA1.2.1602976112.1689586030; __cflb=04dToPArR6Q6HcXUHHBHivqvKLj7JYE9quKiEJUMWK; _gat=1; _ga=GA1.1.c8b59b8ae31f4ebaa2603b51ac627afe; amplitude_id_0edef3d53cf532ceb73be318a9fcfcf1flatfox.ch=eyJkZXZpY2VJZCI6ImM4YjU5YjhhZTMxZjRlYmFhMjYwM2I1MWFjNjI3YWZlIiwidXNlcklkIjoiMTkwNjQ4NyIsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTY4OTYzMDYyMDU2NCwibGFzdEV2ZW50VGltZSI6MTY4OTYzMDg2NTI1OCwiZXZlbnRJZCI6OCwiaWRlbnRpZnlJZCI6MCwic2VxdWVuY2VOdW1iZXIiOjh9; _ga_JLFJYRLRG8=GS1.1.1689630621.22.1.1689630869.45.0.0")
                .addHeader("authority", "flatfox.ch")
                .addHeader("accept-language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cache-control", "max-age=0")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("origin", "https://flatfox.ch")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());


    }
    public static String getLoc(int id) throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://flatfox.ch/de/916358/m")
                .get()
                .addHeader("cookie", "cid=c8b59b8ae31f4ebaa2603b51ac627afe; flatfoxDevice=766114f4c755414295bd1177a05353b6; __cflb=04dToPArR6Q6HcXUHU3ZhDRhmKfC1Zv1FTft3KVYnF; csrftoken=2U3iS1N4o2UsBTynbW51C7NmLe6m9myUq9B19QPIsN3Y7R7rGGW6SrOVWYnYjHL4")
                .addHeader("authority", "flatfox.ch")
                .addHeader("accept-language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string().split("<meta property=\"al:ios:url\" content=\"")[1].split(id + "")[0] + id + "/";
    }
    */

}
