package com.team11.hackernews.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Story implements Parcelable {

    public static final String STORY_PARCEL_KEY = "story";
    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
    private Long mId;
    private String mBy;
    private long mTime;
    private String mText;
    private List<Long> mKids;
    private URL mURL;
    private Long mScore;
    private String mTitle;

    public Story(){}

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

    public Long getId() {
        return mId;
    }
    public void setId(long id) {
        mId = id;
    }

    public String getBy() {
        return mBy;
    }
    public void setBy(String by) {
        mBy = by;
    }

    public long getTime() {
        return mTime;
    }
    public void setTime(long time) {
        mTime = time;
    }

    public String getText() {
        return mText;
    }
    public void setText(String text) {
        mText = text;
    }

    public List<Long> getKids() {
        return mKids;
    }
    public void setKids(List<Long> kids) {
        mKids = kids;
    }

    public URL getURL() {
        return mURL;
    }
    public void setURL(URL url) {
        mURL = url;
    }

    public Long getScore() {
        return mScore;
    }
    public void setScore(long score) {
        mScore = score;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
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
