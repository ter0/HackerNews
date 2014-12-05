package com.team11.hackernews;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.data.Thread;

public class MainDualActivity extends MainBase{

    /**
     * Pass either of these to tell this what to put in it's container fragment
     */
    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static final String THREAD = "THREAD";

    /*Fragment Displaying threads*/
    private MainFragment mMainFragment;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dual);
        String webViewUrl = getIntent().getStringExtra(WEB_VIEW_URL);
        Parcelable thread = getIntent().getParcelableExtra(THREAD);
        if(webViewUrl != null && thread != null){
            throw new UnsupportedOperationException("Activity cannot take both WEB_VIEW_URL and THREAD extras");
        }else if(webViewUrl != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WebViewFragment.newInstance(webViewUrl))
                    .commit();
        }else if(thread != null){
            ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        }
        if (findViewById(R.id.fragment_main) != null) {
            mTwoPane = true;
            mMainFragment = (MainFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        }
        baseSetUp();

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
