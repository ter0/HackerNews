package com.team11.hackernews.api.data;

import java.util.List;

/**
 * A "User".
 * Analogous to the "User" in the HN API: https://github.com/HackerNews/API#users
 */
public class User {
    private String mId;
    private long mDelay;
    private long mCreated;
    private long mKarma;
    private String mAbout;
    private List<Long> mSubmitted;

    private User(Builder builder) {
        mId = builder.mId;
        mDelay = builder.mDelay;
        mCreated = builder.mCreated;
        mKarma = builder.mKarma;
        mAbout = builder.mAbout;
        mSubmitted = builder.mSubmitted;
    }

    public String getId() {
        return mId;
    }

    public long getDelay() {
        return mDelay;
    }

    public long getCreated() {
        return mCreated;
    }

    public long getKarma() {
        return mKarma;
    }

    public String getAbout() {
        return mAbout;
    }

    public List<Long> getSubmitted() {
        return mSubmitted;
    }

    public static final class Builder {
        private String mId;
        private long mDelay;
        private long mCreated;
        private long mKarma;
        private String mAbout;
        private List<Long> mSubmitted;

        public Builder() {
        }

        public Builder id(String id) {
            this.mId = id;
            return this;
        }

        public Builder delay(long delay) {
            this.mDelay = delay;
            return this;
        }

        public Builder created(long created) {
            this.mCreated = created;
            return this;
        }

        public Builder karma(long karma) {
            this.mKarma = karma;
            return this;
        }

        public Builder about(String about) {
            this.mAbout = about;
            return this;
        }

        public Builder submitted(List<Long> submitted) {
            this.mSubmitted = submitted;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
