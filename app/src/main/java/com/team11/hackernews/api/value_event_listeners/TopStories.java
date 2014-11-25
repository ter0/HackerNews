package com.team11.hackernews.api.value_event_listeners;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;

import java.util.List;

public class TopStories implements ValueEventListener {

    Callbacks mCallbacks;
    private int mStartItem;
    private int mAmount;
    private boolean mCancelPendingCallbacks;
    private boolean mFirstQuery;

    public TopStories(int amount, Callbacks callbacks) {
        mAmount = amount;
        mCallbacks = callbacks;
        mCancelPendingCallbacks = false;
        mFirstQuery = true;
    }

    public void getStories(Firebase firebase) {
        Query query = firebase.child(HackerNewsAPI.TOP_STORIES);
        //query with order by and startAt wasn't working as I expected
        //proof of concept, keep reloading first n stories RS
        //query.orderByKey();
        if (!mFirstQuery) {
            //query = query.startAt(null, String.valueOf(mStartItem));
        }
        query.limitToFirst(mAmount).addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        if (mCancelPendingCallbacks) {
            return;
        }
        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
        };
        List<String> messages = snapshot.getValue(t);
        if (messages == null) {
            Log.d("messages: ", "null");
            // TODO Use the previous values, but notify user
        } else {
            //remember what the last item was so only new items are queried next time
            mStartItem = messages.size() - 1;//Integer.valueOf(messages.get(messages.size()-1));
            Log.d("messages: ", messages.get(0));
            mCallbacks.useMessages(messages, mFirstQuery);
            mFirstQuery = false;
        }
    }

    @Override
    public void onCancelled(FirebaseError error) {
        if (mCancelPendingCallbacks) {
            return;
        }
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface Callbacks {
        public void useMessages(List<String> messages, boolean firstQuery);
    }
}
