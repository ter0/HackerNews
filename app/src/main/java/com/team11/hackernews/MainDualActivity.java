package com.team11.hackernews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.accessors.StoryAccessor;
import com.team11.hackernews.api.data.Thread;

public class MainDualActivity extends MainBase {

    /**
     * Pass either of these to tell this what to put in it's container fragment
     */
    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static final String THREAD = "THREAD";
    StoryAccessor.GetStoryCallbacks storyCallbacks = new StoryAccessor.GetStoryCallbacks() {
        @Override
        public void onSuccess(Thread thread) {
            loadThread(thread);
        }

        @Override
        public void onDeleted(long id) {
        }

        @Override
        public void onWrongItemType(Utils.ItemType itemType, long id) {
        }

        @Override
        public void onError() {
        }
    };
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
        baseSetUp();
        //load website or comments requested
        String webViewUrl;
        Parcelable threadParcelable;
        if(savedInstanceState == null){
            webViewUrl = getIntent().getStringExtra(WEB_VIEW_URL);
            threadParcelable = getIntent().getParcelableExtra(THREAD);
        }else{
            webViewUrl = savedInstanceState.getString(WEB_VIEW_URL);
            threadParcelable = savedInstanceState.getParcelable(THREAD);
        }
        if (webViewUrl != null && threadParcelable != null) {
            throw new UnsupportedOperationException("Activity cannot take both WEB_VIEW_URL and THREAD extras");
        } else if (webViewUrl != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WebViewFragment.newInstance(webViewUrl))
                    .commit();
        } else if (threadParcelable != null) {
            ThreadFragment commentsFragment = ThreadFragment.newInstance(threadParcelable);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        } else if (getIntent().getData().getQueryParameter("id") != null) {
            int id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));
            //pending api update this WILL BREAK on non-story items
            new StoryAccessor().getStory(id, storyCallbacks);
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
            //if started from external url, no bundle will exist
            if (inputBundle.getBundle(MainFragment.MAIN_FRAGMENT_KEY) != null) {
                mMainFragment.restoreState(inputBundle);
            }
        } else {
            //if we're only showing the webView, we need to store this encase the screen rotates
            mMainFragmentBundle = inputBundle;
        }
    }

    public void loadThread(Thread thread) {
        if(thread.hasComments()) {
            ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        }else{
            try{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, WebViewFragment.newInstance(String.valueOf(thread.getURL())))
                        .commit();
            }catch(NoSuchFieldException e){
                //just don't load webView
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTwoPane) {
            mMainFragment.saveState(outState);
        } else {
            outState.putAll(mMainFragmentBundle);
        }
        //these should really be made their fragment responsibility, then i.e. webViewFrag.save(bundle) is called
        if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof WebViewFragment){
            String url = ((WebViewFragment)getSupportFragmentManager().findFragmentById(R.id.container)).getURL();
            outState.putString(WEB_VIEW_URL, url);
        }else if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof ThreadFragment){
            Thread thread = ((ThreadFragment)getSupportFragmentManager().findFragmentById(R.id.container)).getThread();
            outState.putParcelable(THREAD, thread);
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
