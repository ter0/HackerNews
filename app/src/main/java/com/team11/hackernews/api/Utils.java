package com.team11.hackernews.api;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Utils {

    public static Item generateItemFromMap(Map<String, Object> map) {
        Boolean dead = (Boolean) map.get("dead");
        if (dead == null)
            dead = false;

        Long parent = (Long) map.get("parent");
        if (parent == null)
            parent = (long) -1;

        URL url = null;
        try {
            url = new URL((String) map.get("url"));
        } catch (MalformedURLException ignored) {
        }

        return new Item.Builder()
                .by((String) map.get("by"))
                .id((Long) map.get("id"))
                .kids((ArrayList<Long>) map.get("kids"))
                .score((Long) map.get("score"))
                .type(Item.getTypeFromString((String) map.get("type")))
                .time((Long) map.get("time"))
                .text((String) map.get("text"))
                .dead(dead)
                .parent(parent)
                .URL(url)
                .title((String) map.get("title"))
                .parts((ArrayList<Long>) map.get("parts"))
                .build();
    }
}
