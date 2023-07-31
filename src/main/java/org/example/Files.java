package org.example;

import org.apache.commons.io.IOUtils;
import org.example.utilities.Utilities;
import org.example.utilities.WG;
import org.example.utilities.WGType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.Property;

import java.io.*;
import java.util.Properties;

import static org.example.Upload.json;
import static org.example.Upload.wgs;

public class Files {


    public static void loadJSON() throws IOException {
        File f = new File(Utilities.WGJSonPath);
        json = new JSONObject();
        if (f.exists()) {
            InputStream is = new FileInputStream(Utilities.WGJSonPath);
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            json = new JSONObject(jsonTxt);
        } else {
            f.createNewFile();
            JSONArray array = new JSONArray();
            json.put("wgs", array);
            saveJSON();
        }
        JSONArray array = json.getJSONArray("wgs");

        for (Object o : array) {

            JSONObject w = (JSONObject) o;
            WG wg = new WG(Integer.parseInt(w.getString("price")), w.getString("location"), w.getString("link"),   w.has("type")? WGType.valueOf(w.getString("type")) : WGType.UNKNOWN);
            wgs.put(w.getString("link"), wg);
        }

    }
    public static void saveJSON() throws IOException {

        File f = new File(Utilities.WGJSonPath);
        FileWriter fw = new FileWriter(f);
        fw.write(json.toString());
        fw.close();

    }




    public static boolean loadPropFile() throws IOException {

        String path = Files.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(path);
        String jarDir = jarFile.getParentFile().getAbsolutePath();

        File f = new File(jarDir+"/status.properties");
        Properties prop = new Properties();
        boolean status = true;
        System.out.println("accessing file: " + f.getPath());
        if(f.exists()){


        }else {

            status = false;
            f.createNewFile();
            FileWriter fileWriter = new FileWriter(f);

            prop.put("geckodriver", jarDir+"/geckodriver");
            prop.put("firefox",jarDir+"/firefox");
            prop.put("wgjson",jarDir+"/WGJson.json");
            prop.store(fileWriter, "");
            fileWriter.close();
        }

   /*     Utilities.FireFoxPath = prop.getProperty("firefox");
        Utilities.GeckoPath = prop.getProperty("geckodriver");
        Utilities.WGJSonPath = prop.getProperty("wgjson");
        */

        return status;

    }


}
