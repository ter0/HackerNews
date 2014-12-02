package com.team11.hackernews.api.accessors;

import android.os.Parcel;

import com.team11.hackernews.api.Thread;

public class Job extends Thread{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
