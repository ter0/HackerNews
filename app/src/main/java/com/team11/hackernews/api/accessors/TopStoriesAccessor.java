package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.data.Thread;

import java.util.List;

public class TopStoriesAccessor extends Accessor {

    private List<Long> mStoryIds;
    private int mNextStoryIdx;
    private int mPageLength;

    public TopStoriesAccessor(int pageLength) {
        super();
        mNextStoryIdx = 0;
        mPageLength = pageLength;
    }

    private List<Long> getNextPage() {
        int endIdx = mNextStoryIdx + mPageLength;
        if (endIdx > mStoryIds.size()) {
            endIdx = mStoryIds.size();
        }
        List<Long> storyIds = mStoryIds.subList(mNextStoryIdx, endIdx);
        mNextStoryIdx = endIdx;
        return storyIds;

    }

    public void getInitialStories(final GetTopStoriesCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }

                // Get all top stories in case they change order while trying to load the next page
                mStoryIds = (List<Long>) dataSnapshot.getValue();

                new ThreadAccessor().getMultipleStories(getNextPage(), new ThreadAccessor.GetMultipleStoriesCallbacks() {
                    @Override
                    public void onSuccess(List<Thread> threads) {
                        callbacks.onSuccess(threads);
                    }

                    @Override
                    public void onError() {

                    }
                });
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

    public void getNextStories(final GetTopStoriesCallbacks callbacks) {
        new ThreadAccessor().getMultipleStories(getNextPage(), new ThreadAccessor.GetMultipleStoriesCallbacks() {
            @Override
            public void onSuccess(List<Thread> stories) {
                callbacks.onSuccess(stories);
            }

            @Override
            public void onError() {

            }
        });
    }

    public interface GetTopStoriesCallbacks {
        public void onSuccess(List<Thread> threads);

        public void onError();
    }
}
