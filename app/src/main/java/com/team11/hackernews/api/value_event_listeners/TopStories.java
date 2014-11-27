package com.team11.hackernews.api.value_event_listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;

import java.util.List;

public class TopStories {

    Callbacks mCallbacks;
    private boolean mCancelPendingCallbacks;
    private Firebase mFirebase;

    private List<String> mStoryIds;
    private int mNextStoryIdx;
    private int mPageLength;

    public TopStories(int pageLength, Callbacks callbacks) {
        mCallbacks = callbacks;
        mCancelPendingCallbacks = false;

        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
        mNextStoryIdx = 0;
        mPageLength = pageLength;
    }

    private List<String> getNextPage(){
        int endIdx = mNextStoryIdx = mNextStoryIdx + mPageLength;
        return mStoryIds.subList(mNextStoryIdx, endIdx);

    }

    public void getInitialStories() {
        mFirebase.child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks){
                    return;
                }

                // Get all top stories in case they change order while trying to load the next page
                mStoryIds = dataSnapshot.getValue(new GenericTypeIndicator<List<String>>() {
                });

                mCallbacks.addMessages(getNextPage(), true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks) {
                    return;
                }
            }
        });
    }

    public void getNextStories() {
        mCallbacks.addMessages(getNextPage(), false);
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface Callbacks {
        public void addMessages(List<String> messages, boolean firstPage);
    }
}
