package com.team11.hackernews.api.value_event_listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;

import java.util.List;

public class TopStories {


    private boolean mCancelPendingCallbacks;
    private Firebase mFirebase;

    private List<Long> mStoryIds;
    private int mNextStoryIdx;
    private int mPageLength;

    public TopStories(int pageLength) {
        mCancelPendingCallbacks = false;

        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
        mNextStoryIdx = 0;
        mPageLength = pageLength;
    }

    private List<Long> getNextPage(){
        int endIdx = mNextStoryIdx + mPageLength;
        if (endIdx > mStoryIds.size()){
            endIdx = mStoryIds.size();
        }
        List<Long> storyIds = mStoryIds.subList(mNextStoryIdx, endIdx);
        mNextStoryIdx = endIdx;
        return storyIds;

    }

    public void getInitialStories(final TopStoriesCallbacks callbacks) {
        mFirebase.child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks){
                    return;
                }

                // Get all top stories in case they change order while trying to load the next page
                mStoryIds = dataSnapshot.getValue(new GenericTypeIndicator<List<Long>>() {
                });

                callbacks.getStoriesCallback(getNextPage());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks) {
                    return;
                }
            }
        });
    }

    public void getNextStories(TopStoriesCallbacks callbacks) {
        callbacks.getStoriesCallback(getNextPage());
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface TopStoriesCallbacks {
        public void getStoriesCallback(List<Long> stories);
    }
}
