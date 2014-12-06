package com.team11.hackernews.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

public class Job extends Thread {

    public static final Parcelable.Creator<Job> CREATOR = new Parcelable.Creator<Job>() {
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public Job(Parcel in) {
        try {
            mURL = new URL(in.readString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Job() {

    }

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
        dest.writeString(mURL.toString());
    }

    @Override
    public boolean hasComments(){
        return false;
    }

    @Override
    public boolean hasURL(){
        return true;
    }
}
