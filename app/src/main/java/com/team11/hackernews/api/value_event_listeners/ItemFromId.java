package com.team11.hackernews.api.value_event_listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.Item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ItemFromId implements ValueEventListener {

    public interface Callbacks {
        public void addItem(Item item);

        public void itemFailed();
    }

    Callbacks mCallbacks;

    public ItemFromId(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    //double check this being final is cool
    public void onDataChange(final DataSnapshot snapshot) {
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
        Boolean dead = (Boolean) map.get("dead");
        if (dead == null)
            dead = false;

        Integer parent = (Integer) map.get("parent");
        if (parent == null)
            parent = -1;

        URL url = null;
        try {
            url = new URL((String) map.get("url"));
        } catch (MalformedURLException ignored) {
        }

        Item item = new Item.Builder()
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
        mCallbacks.addItem(item);
    }

    @Override
    public void onCancelled(FirebaseError error) {
        mCallbacks.itemFailed();
    }
}