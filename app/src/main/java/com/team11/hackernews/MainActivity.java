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

import com.firebase.client.Firebase;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;
import com.team11.hackernews.api.value_event_listeners.ItemFromId;
import com.team11.hackernews.api.value_event_listeners.TopStories;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TopStories.Callbacks, ItemFromId.Callbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private RecyclerView mRecyclerView;
    private List<String> mTopStoriesList;
    private ItemAdapter mItemAdapter;
    private TopStories mTopStoriesEventListener;
    private ItemFromId mItemFromIdEventListener;
    private Firebase mFirebase;
    private int itemCount;
    private boolean finishedLoading;

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
        mItemAdapter = new ItemAdapter();

        mRecyclerView.setAdapter(mItemAdapter);

        mTopStoriesList = new ArrayList<String>();

        Firebase.setAndroidContext(this.getApplicationContext());
        mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
        mTopStoriesEventListener = new TopStories(this);
        mItemFromIdEventListener = new ItemFromId(this);

        finishedLoading = true;
        refresh();
    }

    @Override
    public void useMessages(final List<String> idList) {
        itemCount = idList.size();
        mTopStoriesList.clear();
        mItemAdapter.clear();
        mItemAdapter.notifyDataSetChanged();
        for (String id : idList) {
            mFirebase.child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(mItemFromIdEventListener);
        }
    }

    @Override
    public void addItem(Item item) {
        mItemAdapter.add(item);
        mItemAdapter.notifyDataSetChanged();
        if (mItemAdapter.getItemCount() == itemCount) {
                    finishedLoading = true;
                    supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void itemFailed() {
        itemCount--;
    }

    private void refresh() {
        finishedLoading = false;
        supportInvalidateOptionsMenu();
        mFirebase.child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(mTopStoriesEventListener);
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
        Log.d("on_prepare", "finished loading: " + finishedLoading);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        if (refresh != null) {
            refresh.setVisible(finishedLoading);
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
