package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;

import java.util.List;

public class TopStoriesAccessor extends Accessor {

    private List<Long> mStoryIds;
    private int mNextStoryIdx;
    private int mPageLength;

    public TopStoriesAccessor(int pageLength) {
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

    public void getInitialStories(final GetTopStoriesCallbacks callbacks) {
        mFirebase.child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }

                // Get all top stories in case they change order while trying to load the next page
                mStoryIds = dataSnapshot.getValue(new GenericTypeIndicator<List<Long>>() {
                });

                callbacks.onSuccess(getNextPage());
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

    public void getNextStories(GetTopStoriesCallbacks callbacks) {
        callbacks.onSuccess(getNextPage());
    }

    public interface GetTopStoriesCallbacks {
        public void onSuccess(List<Long> stories);
        public void onError();
    }
}
