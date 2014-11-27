package com.team11.hackernews.api.accessors;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.HackerNewsAPI;


public abstract class Accessor {

    protected Firebase mFirebase;
    protected boolean mCancelPendingCallbacks;

    protected Accessor() {
        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
        mCancelPendingCallbacks = false;
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }
}
