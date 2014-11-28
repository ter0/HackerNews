package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.User;

import java.util.List;
import java.util.Map;

public class UserAccessor extends Accessor {

    public UserAccessor() {
        super();
    }

    public UserAccessor(Firebase firebase) {
        super(firebase);
    }

    public void getUser(final String username, final GetUserCallbacks callbacks){
        mFirebase.child(HackerNewsAPI.USER + "/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks){
                    return;
                }

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

                callbacks.onSuccess(user);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks){
                    return;
                }
                callbacks.onError();
            }
        });
    }

    public interface GetUserCallbacks {
        public void onSuccess(User user);
        public void onError();
    }
}
