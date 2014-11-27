package com.team11.hackernews.api.value_event_listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.User;

import java.util.List;
import java.util.Map;

public class UserFromId {

    private Firebase mFirebase;

    public UserFromId(){
        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
    }

    public void getUser(final String username){
        mFirebase.child(HackerNewsAPI.USER + "/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // create the user object

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                User user = new User.Builder()
                        .id(username)
                        .delay((Integer) map.get("delay"))
                        .created((Long) map.get("created"))
                        .karma((Integer) map.get("karma"))
                        .about((String) map.get("about"))
                        .submitted((List<Long>) map.get("submitted"))
                        .build();

                ///need to run callback

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
