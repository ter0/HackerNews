package com.team11.hackernews;

import android.content.Intent;
import android.os.Bundle;

import com.team11.hackernews.api.data.Thread;

public class MainActivity extends MainBase {

    /*Fragment Displaying threads*/
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        super.baseSetUp();
    }

    @Override
    public void loadWebView(String url) {
        Intent intent = new Intent(this, MainDualActivity.class);
        intent.putExtra(MainDualActivity.WEB_VIEW_URL, url);
        startActivity(intent);
    }

    @Override
    public void loadCommentsView(Thread thread) {
        Intent intent = new Intent(this, MainDualActivity.class);
        intent.putExtra(MainDualActivity.THREAD, thread);
        startActivity(intent);
    }

}
