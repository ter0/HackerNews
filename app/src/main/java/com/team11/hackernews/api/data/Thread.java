package com.team11.hackernews.api.data;

import android.os.Parcelable;

import java.util.List;

public abstract class Thread implements Parcelable {
    public static final String THREAD_PARCEL_KEY = "thread";
    protected Long mId;
    protected String mBy;
    protected long mTime;
    protected String mText;
    protected List<Long> mKids;
    protected Long mScore;
    protected String mTitle;

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

}
