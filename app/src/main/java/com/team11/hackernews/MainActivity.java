package com.team11.hackernews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.accessors.ThreadAccessor;
import com.team11.hackernews.api.data.Thread;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainFragment.Callbacks,
        ItemAdapter.ItemInteractionCallbacks, WebViewFragment.OnFragmentInteractionListener {

    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static final String THREAD = "THREAD";
    static final String SCROLL_POSITION = "SCROLL_POSITION";
    CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;
    ViewPager mViewPager;
    boolean mTwoPane;
    ThreadAccessor.GetStoryCallbacks storyCallbacks = new ThreadAccessor.GetStoryCallbacks() {
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
        public void onError(long id, Error error) {
        }
    };
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MainFragment mMainFragment;
    private Bundle mSavedInstanceState;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this.getApplicationContext());
        if (findViewById(R.id.fragment_main) != null) {
            mTwoPane = true;
            mMainFragment = (MainFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        } else {
            mTwoPane = false;
            mCustomFragmentPagerAdapter =
                    new CustomFragmentPagerAdapter(
                            getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mCustomFragmentPagerAdapter);
            mMainFragment = mCustomFragmentPagerAdapter.getMainFragment();
        }
        mSavedInstanceState = savedInstanceState;
        if (getIntent().getData() != null && getIntent().getData().getQueryParameter("id") != null) {
            int id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));
            new ThreadAccessor().getStory(id, storyCallbacks);
        }

        //setup drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (mNavigationDrawerFragment == null) {
            throw new IllegalStateException("MainBase activity layouts need a R.id.navigation_drawer element");
        }
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Start service to watch users/threads
        Intent intent = new Intent(this, WatcherService.class);
        this.startService(intent);
    }

    public void loadThread(Thread thread) {
        if (thread.hasComments()) {
            this.loadCommentsView(thread);
        } else {
            try {
                loadWebView(String.valueOf(thread.getURL()));
            } catch (NoSuchFieldException e) {
                //just don't load webView
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTwoPane) {
            saveTwoPane(outState);
        } else {
            saveSinglePane(outState);
        }
    }

    private void saveTwoPane(Bundle outState) {
        mMainFragment.saveState(outState);
        //these should really be made their fragment responsibility, then i.e. webViewFrag.save(bundle) is called
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof WebViewFragment) {
            String url = ((WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getURL();
            outState.putString(WEB_VIEW_URL, url);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof ThreadFragment) {
            Thread thread = ((ThreadFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getThread();
            outState.putParcelable(THREAD, thread);
        }
    }

    private void saveSinglePane(Bundle outState) {
        outState.putInt(SCROLL_POSITION, mViewPager.getCurrentItem());
        mCustomFragmentPagerAdapter.saveMainFragment(outState);
        if (mCustomFragmentPagerAdapter.getCount() == 2) {
            if (mCustomFragmentPagerAdapter.mCurrentSecondPage == CustomFragmentPagerAdapter.SecondPage.WEBVIEW) {
                String url = mCustomFragmentPagerAdapter.getWebUrl();
                outState.putString(WEB_VIEW_URL, url);
            } else if (mCustomFragmentPagerAdapter.mCurrentSecondPage == CustomFragmentPagerAdapter.SecondPage.COMMENTS) {
                Thread thread = mCustomFragmentPagerAdapter.getThread();
                outState.putParcelable(THREAD, thread);
            }
        }
    }

    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof MainFragment && mSavedInstanceState != null) {
            MainFragment mainFragment = (MainFragment) fragment;
            mainFragment.restoreState(mSavedInstanceState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mTwoPane) {
            restoreTwoPane(savedInstanceState);
        } else {
            restoreSinglePane(savedInstanceState);
        }
    }

    private void restoreTwoPane(Bundle bundle) {
        String webViewUrl = bundle.getString(WEB_VIEW_URL);
        Thread threadParcelable = bundle.getParcelable(THREAD);
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
        }
    }

    private void restoreSinglePane(Bundle bundle) {
        String webViewUrl = bundle.getString(WEB_VIEW_URL);
        Thread threadParcelable = bundle.getParcelable(THREAD);
        if (webViewUrl != null) {
            mCustomFragmentPagerAdapter.setWebViewFragment(webViewUrl);
        } else if (threadParcelable != null) {
            mCustomFragmentPagerAdapter.setThreadFragment(threadParcelable);
        }
        mViewPager.setCurrentItem(bundle.getInt(SCROLL_POSITION));
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openExternalApps(Uri uri) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //todo: make a call to the MainFragment to change the thread type it's displaying
    }

    @Override
    public void setState(MainFragment.State state) {
        mMainFragment.setState(state);
    }

    @Override
    public void loadWebView(String url) {
        if (mTwoPane) {
            Log.d("webview", "twoPane");
            WebViewFragment webViewFragment = WebViewFragment.newInstance(url);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, webViewFragment)
                    .commit();
        } else {
            Log.d("webview", "!twoPane");
            mCustomFragmentPagerAdapter.setWebViewFragment(url);
            mViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void loadCommentsView(com.team11.hackernews.api.data.Thread thread) {
        if (mTwoPane) {
            Log.d("loadComments", "twoPane");
            ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        } else {
            Log.d("loadComments", "!twoPane");
            mCustomFragmentPagerAdapter.setThreadFragment(thread);
            mViewPager.setCurrentItem(2);
        }
    }
}
