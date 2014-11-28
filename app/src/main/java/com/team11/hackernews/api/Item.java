package com.team11.hackernews.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An "Item". This can be a story, comment, job, Ask HN and even a poll.
 * Analogous to the "Item" in the HN API: https://github.com/HackerNews/API#items
 */
public class Item {
    private Long mId;
    private boolean mDeleted;
    private Type mType;
    private String mBy;
    private long mTime;
    private String mText;
    private boolean mDead;
    private long mParent;
    private List<Long> mKids;
    private URL mURL;
    private Long mScore;
    private String mTitle;
    private ArrayList<Long> mParts;

    private Item(Builder builder) {
        mId = builder.mId;
        mDeleted = builder.mDeleted;
        mType = builder.mType;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mDead = builder.mDead;
        mParent = builder.mParent;
        mKids = builder.mKids;
        mURL = builder.mURL;
        mScore = builder.mScore;
        mTitle = builder.mTitle;
        mParts = builder.mParts;
    }

    public static Type getTypeFromString(String type) {
        if (type.equalsIgnoreCase("story")) {
            return Type.Story;
        } else if (type.equalsIgnoreCase("job")) {
            return Type.Job;
        } else if (type.equalsIgnoreCase("poll")) {
            return Type.Poll;
        } else if (type.equalsIgnoreCase("pollopt")) {
            return Type.Pollopt;
        } else if (type.equalsIgnoreCase("comment")) {
            return Type.Comment;
        } else {
            throw new IllegalStateException("Invalid Item type: " + type);
        }
    }

    public Long getId() {
        return mId;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public Type getType() {
        return mType;
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

    public boolean isDead() {
        return mDead;
    }

    public long getParent() {
        return mParent;
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

    public ArrayList<Long> getParts() {
        return mParts;
    }

    public enum Type {
        Job,
        Story,
        Comment,
        Poll,
        Pollopt
    }

    public static final class Builder {
        private Long mId;
        private boolean mDeleted;
        private Type mType;
        private String mBy;
        private long mTime;
        private String mText;
        private boolean mDead;
        private long mParent;
        private List<Long> mKids;
        private URL mURL;
        private Long mScore;
        private String mTitle;
        private ArrayList<Long> mParts;

        public Builder() {
        }

        public Builder id(Long id) {
            this.mId = id;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.mDeleted = deleted;
            return this;
        }

        public Builder type(Type type) {
            this.mType = type;
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

        public Builder dead(boolean dead) {
            this.mDead = dead;
            return this;
        }

        public Builder parent(long parent) {
            this.mParent = parent;
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

        public Builder parts(ArrayList<Long> parts) {
            this.mParts = parts;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
