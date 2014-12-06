package com.team11.hackernews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.team11.hackernews.api.data.Thread;

public class MainDualActivity extends MainBase {

    /**
     * Pass either of these to tell this what to put in it's container fragment
     */
    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static final String THREAD = "THREAD";

    /*Fragment Displaying threads*/
    private MainFragment mMainFragment;

    //used to store MainFragment details whilst layout is portrait
    //should be null as long as MainFragment is being shown
    private Bundle mMainFragmentBundle;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dual);
        //load website or comments requested
        String webViewUrl = getIntent().getStringExtra(WEB_VIEW_URL);
        Parcelable thread = getIntent().getParcelableExtra(THREAD);
        if (webViewUrl != null && thread != null) {
            throw new UnsupportedOperationException("Activity cannot take both WEB_VIEW_URL and THREAD extras");
        } else if (webViewUrl != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WebViewFragment.newInstance(webViewUrl))
                    .commit();
        } else if (thread != null) {
            ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        }
        Bundle inputBundle;
        if (savedInstanceState == null) {
            inputBundle = getIntent().getExtras();
        } else {
            inputBundle = savedInstanceState;
        }
        //load menu if horizontal
        if (findViewById(R.id.fragment_main) != null) {
            mTwoPane = true;
            mMainFragment = (MainFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            mMainFragment.restoreState(inputBundle);
        } else {
            //if we're only showing the webView, we need to store this encase the screen rotates
            mMainFragmentBundle = inputBundle;
        }
        baseSetUp();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTwoPane) {
            mMainFragment.saveState(outState);
        } else {
            outState.putAll(mMainFragmentBundle);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        if (mTwoPane) {
            intent.putExtras(mMainFragment.saveState(new Bundle()));
        } else {
            intent.putExtras(mMainFragmentBundle);
        }
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void loadWebView(String url) {
        WebViewFragment webViewFragment = WebViewFragment.newInstance(url);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, webViewFragment)
                .commit();
    }

    @Override
    public void loadCommentsView(Thread thread) {
        ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, commentsFragment)
                .commit();
    }

}
