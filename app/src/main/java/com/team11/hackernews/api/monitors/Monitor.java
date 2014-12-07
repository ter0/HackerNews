package com.team11.hackernews.api.monitors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.accessors.StoryAccessor;
import com.team11.hackernews.api.data.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Monitor {
    private Query mQuery;
    private List<ValueEventListener> mListeners;

    protected Monitor() {
        mQuery = Utils.getFirebaseInstance().child(HackerNewsAPI.UPDATES);
        mListeners = new ArrayList<ValueEventListener>();
    }

    public void removeListeners() {
        for (ValueEventListener listener : mListeners) {
            mQuery.removeEventListener(listener);
        }
        mListeners.clear();
    }

    protected void addListener(final AddListenerCallbacks callbacks) {
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> rootMap = (Map<String, Object>) dataSnapshot.getValue();
                List<Long> newItems = (List<Long>) rootMap.get("items");

                for (long id : newItems) {
                    callbacks.onNewItem(id);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        mQuery.addValueEventListener(listener);
        mListeners.add(listener);
    }

    protected void getRootParent(long id, final GetRootParentCallbacks callbacks) {
        new StoryAccessor().getStory(id, new StoryAccessor.GetStoryCallbacks() {
            @Override
            public void onSuccess(com.team11.hackernews.api.data.Thread thread) {
                callbacks.onFound(thread);
            }

            @Override
            public void onDeleted(long id) {
                onError();
            }

            @Override
            public void onWrongItemType(Utils.ItemType itemType, long id) {
                if (itemType != Utils.ItemType.Comment) {
                    onError();
                    return;
                }
                getRootParent(id, callbacks);
            }

            @Override
            public void onError() {
            }
        });
    }

    protected interface GetRootParentCallbacks {
        public void onFound(Thread thread);
    }

    protected interface AddListenerCallbacks {
        public void onNewItem(Long id);
    }
}
