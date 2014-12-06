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
        super.baseSetUp();
        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        //when the app is loaded externally, MAIN_FRAGMENT_KEY wont be present
        if (savedInstanceState == null && getIntent().getBundleExtra(MainFragment.MAIN_FRAGMENT_KEY) != null) {
            mMainFragment.restoreState(getIntent().getExtras());
        }
    }

    @Override
    public void loadWebView(String url) {
        Intent intent = new Intent(this, MainDualActivity.class);
        intent.putExtra(MainDualActivity.WEB_VIEW_URL, url);
        intent.putExtras(mMainFragment.saveState(new Bundle()));
        startActivity(intent);
        finish();
    }

    @Override
    public void loadCommentsView(Thread thread) {
        Intent intent = new Intent(this, MainDualActivity.class);
        intent.putExtra(MainDualActivity.THREAD, thread);
        intent.putExtras(mMainFragment.saveState(new Bundle()));
        startActivity(intent);
        finish();
    }

}
