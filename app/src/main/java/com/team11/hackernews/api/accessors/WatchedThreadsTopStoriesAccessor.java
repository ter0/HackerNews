package com.team11.hackernews.api.accessors;

import android.content.Context;
import android.database.Cursor;

import com.team11.hackernews.WatchedThreadsContentProvider;
import com.team11.hackernews.WatchedThreadsOpenHelper;
import com.team11.hackernews.api.data.Thread;

import java.util.ArrayList;
import java.util.List;

public class WatchedThreadsTopStoriesAccessor extends TopStoriesAccessor {
    Context mContext;

    public WatchedThreadsTopStoriesAccessor(Context context) {
        super();
        mContext = context;
        mStoryIds = new ArrayList<Long>();
    }

    @Override
    public void getNextThreads(int noOfThreads, final GetNextThreadsCallbacks callbacks) {
        Cursor cursor = mContext.getContentResolver().query(WatchedThreadsContentProvider.CONTENT_URI, new String[]{WatchedThreadsOpenHelper.KEY_THREAD_ID}, null, null, null);
        int column = cursor.getColumnIndex(WatchedThreadsOpenHelper.KEY_THREAD_ID);
        mStoryIds.clear();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(column);
            mStoryIds.add(id);
        }
        cursor.close();
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
