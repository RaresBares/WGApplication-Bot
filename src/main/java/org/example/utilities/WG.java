package org.example.utilities;

import java.util.HashMap;
import java.util.Map;

public class WG {

    public int price;
    public String loc;
    public String lang;

    public WGType type;

    public String link;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }



    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public WG(int price, String loc, String link, WGType type) {
        this.price = price;
        this.loc = loc;

        this.link = link;
    }

    public HashMap<String, String> writeToJsonMap(){


        HashMap<String, String> map = new HashMap<>();
        map.put("location", loc);
        map.put("price", String.valueOf(price));
        map.put("link", link);
        map.put("lang", lang);
        map.put("type", type == null ? WGType.WGZIMMER.toString() : type.toString());
        return map;
    }
}
