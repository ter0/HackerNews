package com.team11.hackernews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.data.Thread;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainFragment.Callbacks,
        WebViewFragment.OnFragmentInteractionListener, ItemAdapter.ItemInteractionCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /*Fragment Displaying threads*/
    private MainFragment mMainFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mTitle = getTitle();
        if (findViewById(R.id.container) != null) {
            mTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WebViewFragment.newInstance("http://www.placeholder.com"))
                    .commit();
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        Firebase.setAndroidContext(this.getApplicationContext());

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //todo: make a call to the MainFragment to change the thread type it's displaying
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadWebView(String url) {
        if (mTwoPane) {
            WebViewFragment webViewFragment = WebViewFragment.newInstance(url);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, webViewFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(WebViewFragment.URL_EXTRA, url);
            startActivity(intent);
        }
    }

    @Override
    public void loadCommentsView(Thread thread) {
        if(mTwoPane){
            ThreadFragment commentsFragment = ThreadFragment.newInstance(thread);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, commentsFragment)
                    .commit();
        }else {
            Intent intent = new Intent(this, ThreadActivity.class);
            intent.putExtra(Thread.THREAD_PARCEL_KEY, thread);
            startActivity(intent);
        }
    }

}
