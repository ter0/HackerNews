package com.team11.hackernews.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Story extends Thread {

    public static final String STORY_PARCEL_KEY = "story";
    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    private URL mURL;

    public Story() {
    }

    private Story(Parcel in) {
        mId = in.readLong();
        mBy = in.readString();
        mTime = in.readLong();
        mText = in.readString();
        mKids = new ArrayList<Long>();
        in.readList(mKids, List.class.getClassLoader());
        try {
            mURL = new URL(in.readString());
        } catch (MalformedURLException ignored) {
        }
        mScore = in.readLong();
        mTitle = in.readString();
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
        dest.writeLong(mId);
        dest.writeString(mBy);
        dest.writeLong(mTime);
        dest.writeString(mText);
        dest.writeList(mKids);
        dest.writeString(mURL.toString());
        dest.writeLong(mScore);
        dest.writeString(mTitle);
    }
}
