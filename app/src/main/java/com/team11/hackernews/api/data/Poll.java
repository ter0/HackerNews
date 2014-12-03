package com.team11.hackernews.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Poll extends Thread {
    public static final Parcelable.Creator<Poll> CREATOR = new Parcelable.Creator<Poll>() {
        public Poll createFromParcel(Parcel in) {
            return new Poll(in);
        }

        public Poll[] newArray(int size) {
            return new Poll[size];
        }
    };
    private List<Long> mPollOpts;

    public Poll() {
    }

    public Poll(Parcel in) {
        mPollOpts = new ArrayList<Long>();
        in.readList(mPollOpts, List.class.getClassLoader());
    }

    public List<Long> getPollOpts() {
        return mPollOpts;
    }

    public void setPollOpts(List<Long> pollOpts) {
        mPollOpts = pollOpts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mPollOpts);
    }
}
