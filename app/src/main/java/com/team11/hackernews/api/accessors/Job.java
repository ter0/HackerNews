package com.team11.hackernews.api.accessors;

import android.os.Parcel;

import com.team11.hackernews.api.Thread;

import java.net.URL;

public class Job extends Thread{

    private URL mURL;

    public URL getURL() {
        return mURL;
    }

    public void setURL(URL url) {
        mURL = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
