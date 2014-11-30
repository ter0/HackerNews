package com.team11.hackernews.api.accessors;

public abstract class Accessor {

    protected boolean mCancelPendingCallbacks;

    protected Accessor() {
        mCancelPendingCallbacks = false;
    }

    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }
}
