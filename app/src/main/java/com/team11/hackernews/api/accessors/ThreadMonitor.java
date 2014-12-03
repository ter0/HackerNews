package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreadMonitor {

    private Thread mThread;
    private Query mQuery;
    private List<ValueEventListener> mListeners;

    public ThreadMonitor(Thread thread) {
        mThread = thread;
        mQuery = Utils.getFirebaseInstance().child(HackerNewsAPI.UPDATES);
        mListeners = new ArrayList<ValueEventListener>();
    }

    public void removeListeners() {
        for (ValueEventListener listener : mListeners) {
            mQuery.removeEventListener(listener);
        }
        mListeners.clear();
    }

    public void addListener(final ThreadMonitorCallbacks callbacks){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> rootMap = (Map<String, Object>) dataSnapshot.getValue();
                List<Long> newItems = (List<Long>) rootMap.get("items");

                for (long id : newItems) {
                    getParent(id, new GetParentCallbacks() {
                        @Override
                        public void onFound(Thread thread) {
                            if (thread.getId() == mThread.getId()) {
                                callbacks.onWatchedItemAdded(null, thread);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        mQuery.addValueEventListener(listener);
        mListeners.add(listener);
    }

    private void getParent(long id, final GetParentCallbacks callbacks) {
        new StoryAccessor().getStory(id, new StoryAccessor.GetStoryCallbacks() {
            @Override
            public void onSuccess(Thread thread) {
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
                getParent(id, callbacks);
            }

            @Override
            public void onError() {
            }
        });
    }

    public interface GetParentCallbacks {
        public void onFound(Thread thread);
    }

    public interface ThreadMonitorCallbacks {
        public void onWatchedItemAdded(Comment comment, Thread parentThread);
    }
}
