package com.team11.hackernews.api.accessors;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;
import com.team11.hackernews.api.Utils;

import java.util.List;
import java.util.Map;

public class ContentMonitor extends Accessor {

    public void addStoryMonitor(final List<Long> storiesToWatch, final StoryMonitorCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.UPDATES).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                final Item newItem = Utils.generateItemFromMap(map);

                getRootStory(newItem, new GetRootStoryCallbacks() {
                    @Override
                    public void onFound(Item parentStory) {
                        if (storiesToWatch.contains(parentStory.getId())) {
                            callbacks.onWatchedContentAdded(newItem, parentStory);
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addUserMonitor(final List<String> usersToWatch, final UserMonitorCallbacks callbacks){
        Utils.getFirebaseInstance().child(HackerNewsAPI.UPDATES).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                Item item = Utils.generateItemFromMap(map);

                if (usersToWatch.contains(item.getBy())) {
                    callbacks.onWatchedContentAdded(item);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getRootStory(Item item, final GetRootStoryCallbacks callbacks) {
        ItemAccessor itemAccessor = new ItemAccessor();

        if (item.getType() == Item.Type.Story) {
            callbacks.onFound(item);
            return;
        }

        itemAccessor.getItem(item.getParent(), new ItemAccessor.GetItemCallbacks() {
            @Override
            public void onSuccess(Item item) {
                getRootStory(item, callbacks);
            }

            @Override
            public void onError() {

            }
        });
    }

    public interface GetRootStoryCallbacks {
        public void onFound(Item item);
    }

    public interface StoryMonitorCallbacks {
        public void onWatchedContentAdded(Item newItem, Item parentStory);
    }

    public interface UserMonitorCallbacks {
        public void onWatchedContentAdded(Item addedItem);
    }

}
