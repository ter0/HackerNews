package com.team11.hackernews.api;

import java.util.List;

public class Poll extends Thread {
    private List<Long> mPollOpts;

    public List<Long> getPollOpts() {
        return mPollOpts;
    }
    public void setPollOpts(List<Long> pollOpts) {
        mPollOpts = pollOpts;
    }
}
