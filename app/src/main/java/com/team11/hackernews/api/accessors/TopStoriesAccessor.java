package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.data.Thread;

import java.util.List;

// This is actually top threads but HN calls them stories
public class TopStoriesAccessor extends Accessor {

    private List<Long> mStoryIds;
    private int mNextStoryIdx;

    public TopStoriesAccessor() {
        super();
        mNextStoryIdx = 0;
    }

    private List<Long> getNextPage(int noOfThreads) {
        int endIdx = mNextStoryIdx + noOfThreads;
        if (endIdx > mStoryIds.size()) {
            endIdx = mStoryIds.size();
        }
        List<Long> storyIds = mStoryIds.subList(mNextStoryIdx, endIdx);
        mNextStoryIdx = endIdx;
        return storyIds;

    }

    public void getNextThreads(final int noOfThreads, final GetNextThreadsCallbacks callbacks) {

        if (mStoryIds == null) {
            // If the stories are not downloaded yet, retrieve them
            Utils.getFirebaseInstance().child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }

                    // Get all top stories in case they change order while trying to load the next page
                    mStoryIds = (List<Long>) dataSnapshot.getValue();

                    // After we know we have the stories try getting the next page again
                    getNextThreads(noOfThreads, callbacks);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    callbacks.onError();
                }
            });
        } else {
            new ThreadAccessor().getMultipleStories(getNextPage(noOfThreads), new ThreadAccessor.GetMultipleStoriesCallbacks() {
                @Override
                public void onSuccess(List<Thread> stories) {
                    callbacks.onSuccess(stories);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public interface GetNextThreadsCallbacks {
        public void onSuccess(List<Thread> threads);

        public void onError();
    }
}
