package com.team11.hackernews.api.value_event_listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ItemFromId implements ValueEventListener {

    Callbacks mCallbacks;
    private boolean mCancelPendingCallbacks;
    private Firebase mFirebase;

    public ItemFromId(Callbacks callbacks) {
        mCancelPendingCallbacks = false;
        mCallbacks = callbacks;

        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
    }

    public void getItem(String id) {
        mFirebase.child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(this);
    }

    @Override
    //double check this being final is cool
    public void onDataChange(final DataSnapshot snapshot) {
        if (mCancelPendingCallbacks) {
            return;
        }
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
        if (mCancelPendingCallbacks) {
            return;
        }
        mCallbacks.itemFailed();
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface Callbacks {
        public void addItem(Item item);

        public void itemFailed();
    }
}
