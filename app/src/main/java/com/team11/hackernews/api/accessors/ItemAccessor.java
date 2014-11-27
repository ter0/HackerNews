package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ItemAccessor extends Accessor {

    public void getItem(long id, final GetItemCallbacks callbacks) {
        mFirebase.child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
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
                callbacks.onSuccess(item);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                callbacks.onError();
            }
        });
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface GetItemCallbacks {
        public void onSuccess(Item item);
        public void onError();
    }
}