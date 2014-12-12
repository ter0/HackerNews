package com.team11.hackernews.api.accessors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.team11.hackernews.R;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.data.User;

import java.util.List;

public class WatchedUserAccessor extends TopStoriesAccessor {

    private final Context mContext;
    private User mUser;
    private String mUserStr;

    public WatchedUserAccessor(Context context) {
        mContext = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String user = prefs.getString(mContext.getString(R.string.monitored_user), null);
        if (user == null) {
            Toast.makeText(mContext, "You must define a user in the settings page!", Toast.LENGTH_LONG).show();
        }
        ((Activity) mContext).setTitle(user);
        mUserStr = user;
    }

    @Override
    public void getNextThreads(final int noOfThreads, final GetNextThreadsCallbacks callbacks) {
        if (mUser == null) {
            new UserAccessor().getUser(mUserStr, new UserAccessor.GetUserCallbacks() {
                @Override
                public void onSuccess(User user) {
                    mUser = user;
                    mStoryIds = user.getSubmitted();
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

                @Override
                public void onError() {

                }

                @Override
                public void onUserNotFound() {
                    Toast.makeText(mContext, "You must define a valid user in the settings page!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mStoryIds = mUser.getSubmitted();
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
}
