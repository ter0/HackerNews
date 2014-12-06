package com.team11.hackernews.api.data;

import android.os.Parcel;

public class AskHN extends Thread {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public boolean hasComments(){
        return true;
    }

    @Override
    public boolean hasURL(){
        return false;
    }
}
