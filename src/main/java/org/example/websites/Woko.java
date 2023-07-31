package org.example.websites;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.example.Files;
import org.example.Statistics;
import org.example.Upload;
import org.example.utilities.*;

import java.io.IOException;
import java.util.ArrayList;

public class Woko {


    public static ArrayList<Pair<String, String>> getCreds() throws IOException {
        ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        for (String s : getNewWoko()) {
            Pair<String, String> g = getCredsofWG(s);
            list.add(g);
        }
        return list;
    }


    public static ArrayList<String> getNewWoko() throws IOException {
        ArrayList<String> news = new ArrayList<>();


        for (String zuerichWG : getZuerichWGs()) {

            if (Upload.wgs.containsKey(zuerichWG)) {

            }else {
                news.add(zuerichWG);
                Upload.wgs.put(zuerichWG, new WG(-999, "zuerich", zuerichWG, WGType.WOKO));

            }
        }
        for (String zuerichWG : getWWWGs()) {


         if (Upload.wgs.containsKey(zuerichWG)) {

         }else {
             Upload.wgs.put(zuerichWG, new WG(-999, "ww", zuerichWG, WGType.WOKO));
             news.add(zuerichWG);

         }
        }


       WGZimmer.saveWgs();
        Files.saveJSON();

        return news;

    }


    public static Pair<String, String> getCredsofWG(String link) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();

        String email = resp.split("href=\"mailto:")[1].split("\">")[0];
        String name = "";
        try {
           name = resp.split("<td>Von</td>")[1].substring(12).split("</t")[0];
        } catch (IllegalStateException e){

            name = "Unknown";

        }


        Pair<String, String> res = new Pair<String, String>(email, name);
        return  res;


    }




    public static ArrayList<String> getZuerichWGs() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.woko.ch/de/zimmer-in-zuerich")
                .get()
                .addHeader("cookie", "PHPSESSID=e20ce9slp844069pt22mr3igh0; _csrf=07d4fd6c424bfb61d828b05f1b6bd6a289a4ddd90941c8bf10583e0ea5170af4a%253A2%253A%257Bi%253A0%253Bs%253A5%253A%2522_csrf%2522%253Bi%253A1%253Bs%253A32%253A%2522HxcGdrZ-SIOHFawMF3czWqTwMhTLTAdd%2522%253B%257D")
                .build();

        Response response = client.newCall(request).execute();
        ArrayList<String> lists = new ArrayList<>();
        String body =  response.body().string();
        for (int i = 1; i < body.split("zimmer-in-zuerich-details/").length; i++) {



            lists.add("https://www.woko.ch/de/zimmer-in-zuerich-details/" + body.split("zimmer-in-zuerich-details/")[i].split("\">")[0]);

        }
        return lists;
    }


    public static ArrayList<String> getWWWGs() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.woko.ch/de/zimmer-in-winterthur-und-waedenswil")
                .get()
                .addHeader("cookie", "PHPSESSID=e20ce9slp844069pt22mr3igh0; _csrf=07d4fd6c424bfb61d828b05f1b6bd6a289a4ddd90941c8bf10583e0ea5170af4a%253A2%253A%257Bi%253A0%253Bs%253A5%253A%2522_csrf%2522%253Bi%253A1%253Bs%253A32%253A%2522HxcGdrZ-SIOHFawMF3czWqTwMhTLTAdd%2522%253B%257D")
                .build();

        Response response = client.newCall(request).execute();
        ArrayList<String> lists = new ArrayList<>();
        String body =  response.body().string();
        for (int i = 1; i < body.split("zimmer-in-winterthur-und-waedenswil-details/").length; i++) {



            lists.add("https://www.woko.ch/de/zimmer-in-winterthur-und-waedenswil-details/" + body.split("zimmer-in-winterthur-und-waedenswil-details/")[i].split("\">")[0]);

        }
        return lists;
    }


    public static void apply(Pair<String, String> creds){

        Email.sendEmail(creds.key,"Interesse an WG/Studio auf der WOKO-Seite", Utilities.getMessageDE(creds.value));
        Statistics.Woko++;
    }


}
