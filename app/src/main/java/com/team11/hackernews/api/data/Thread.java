package com.team11.hackernews.api.data;

import android.os.Parcelable;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class Thread implements Parcelable {
    public static final String THREAD_PARCEL_KEY = "thread";
    protected Long mId;
    protected String mBy;
    protected long mTime;
    protected String mText;
    protected List<Long> mKids;
    protected Long mScore;
    protected String mTitle;
    protected URL mURL;

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

    public abstract boolean hasComments();
    public abstract boolean hasURL();
    public URL getURL()throws NoSuchFieldException{
        if(hasURL()){
            return mURL;
        }else{
            throw new NoSuchFieldException("This thread has no URL");
        }
    }

    public String getThreadLink(){
        return "https://news.ycombinator.com/item?id="+mId;
    }
}
