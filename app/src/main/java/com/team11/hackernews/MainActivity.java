package com.team11.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.Thread;
import com.team11.hackernews.api.Story;
import com.team11.hackernews.api.accessors.TopStoriesAccessor;

import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ItemAdapter.Callbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private TopStoriesAccessor mTopStoriesAccessor;
    private boolean mFinishedLoadingRefresh;
    private boolean mFinishedLoadingBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new ItemAdapter(this);

        mRecyclerView.setAdapter(mItemAdapter);

        Firebase.setAndroidContext(this.getApplicationContext());

        mFinishedLoadingRefresh = true;
        mFinishedLoadingBottom = true;
        refresh();
    }

    private void refresh() {
        supportInvalidateOptionsMenu();
        if (mTopStoriesAccessor != null) {
            mTopStoriesAccessor.cancelPendingCallbacks();
        }
        //avoids fetching items twice before refresh is complete
        //i.e. when all items fit on 1 screen, that would trigger a bottom-load otherwise
        mFinishedLoadingBottom = false;
        mTopStoriesAccessor = new TopStoriesAccessor(20);
        mTopStoriesAccessor.getInitialStories(new TopStoriesAccessor.GetTopStoriesCallbacks() {
            @Override
            public void onSuccess(List<Thread> threads) {
                mItemAdapter.clear();
                for (Thread thread : threads) {
                    mItemAdapter.add(thread);
                }
                mItemAdapter.notifyDataSetChanged();
                mFinishedLoadingRefresh = true;
                mFinishedLoadingBottom = true;
                supportInvalidateOptionsMenu();

                Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onReachedBottom() {
        if (mFinishedLoadingBottom) {
            mFinishedLoadingBottom = false;
            mTopStoriesAccessor.getNextStories(new TopStoriesAccessor.GetTopStoriesCallbacks() {
                @Override
                public void onSuccess(List<Thread> threads) {
                    if (threads.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No More Articles", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Thread thread : threads) {
                            mItemAdapter.add(thread);
                        }
                        mItemAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
                    }

                    mFinishedLoadingBottom = true;
                    supportInvalidateOptionsMenu();

                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("on_prepare", "finished loading: " + mFinishedLoadingRefresh);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        if (refresh != null) {
            refresh.setVisible(mFinishedLoadingRefresh);
        }
        return super.onPrepareOptionsMenu(menu);
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

        if (id == R.id.action_refresh) {
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
