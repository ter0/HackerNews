package com.team11.hackernews.api.value_event_listeners;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class TopStories implements ValueEventListener {

    public interface Callbacks{
        public void useMessages(List<String> messages);
    }

    Callbacks mCallbacks;

    public TopStories(Callbacks callbacks){
        mCallbacks = callbacks;
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
        };
        List<String> messages = snapshot.getValue(t);
        if (messages == null) {
            // TODO Use the previous values, but notify user
        } else {
            Log.d("messages: ", messages.get(0));
            mCallbacks.useMessages(messages);
        }
    }

    @Override
    public void onCancelled(FirebaseError error) {

    }
}
