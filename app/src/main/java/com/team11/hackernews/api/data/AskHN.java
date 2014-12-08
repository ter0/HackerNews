package com.team11.hackernews.api.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AskHN extends Thread {

    public static final Parcelable.Creator<AskHN> CREATOR = new Parcelable.Creator<AskHN>() {
        public AskHN createFromParcel(Parcel in) {
            return new AskHN(in);
        }

        public AskHN[] newArray(int size) {
            return new AskHN[size];
        }
    };

    private AskHN(Parcel in) {
    }

    public AskHN() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public boolean hasComments() {
        return true;
    }

    @Override
    public boolean hasURL() {
        return false;
    }
}
