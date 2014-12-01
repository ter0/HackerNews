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

    private Story(Builder builder) {
        mId = builder.mId;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mKids = builder.mKids;
        mURL = builder.mURL;
        mScore = builder.mScore;
        mTitle = builder.mTitle;
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

    public Long getId() {
        return mId;
    }

    public String getBy() {
        return mBy;
    }

    public long getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public List<Long> getKids() {
        return mKids;
    }

    public URL getURL() {
        return mURL;
    }

    public Long getScore() {
        return mScore;
    }

    public String getTitle() {
        return mTitle;
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

    public static final class Builder {
        private Long mId;
        private String mBy;
        private long mTime;
        private String mText;
        private List<Long> mKids;
        private URL mURL;
        private Long mScore;
        private String mTitle;

        public Builder() {
        }

        public Builder id(Long id) {
            this.mId = id;
            return this;
        }

        public Builder by(String by) {
            this.mBy = by;
            return this;
        }

        public Builder time(long time) {
            this.mTime = time;
            return this;
        }

        public Builder text(String text) {
            this.mText = text;
            return this;
        }

        public Builder kids(List<Long> kids) {
            this.mKids = kids;
            return this;
        }

        public Builder URL(URL URL) {
            this.mURL = URL;
            return this;
        }

        public Builder score(Long score) {
            this.mScore = score;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }
}
