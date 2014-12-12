package com.team11.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team11.hackernews.api.accessors.TopStoriesAccessor;
import com.team11.hackernews.api.data.Thread;

import java.util.List;

public class MainFragment extends Fragment implements ItemAdapter.Callbacks {

    public static final String MAIN_FRAGMENT_KEY = "MAIN_FRAGMENT";
    public static final String FIRST_ITEM_KEY = "FIRST_ITEM";

    ItemAdapter.ItemInteractionCallbacks mItemInteractionCallbacks;
    Callbacks mCallbacks;
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private TopStoriesAccessor mTopStoriesAccessor;
    private boolean mFinishedLoadingRefresh;
    private boolean mFinishedLoadingBottom;
    private int mRememberedPosition;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    public Bundle saveState(Bundle bundle) {
        Bundle mainBundle = new Bundle();
        mItemAdapter.saveState(mainBundle);
        int firstItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        Log.d("rem pos pre", String.valueOf(firstItem));
        mainBundle.putInt(FIRST_ITEM_KEY, firstItem);
        bundle.putBundle(MAIN_FRAGMENT_KEY, mainBundle);
        return bundle;
    }

    public void restoreState(Bundle savedInstanceState) {
        Bundle mainBundle = savedInstanceState.getBundle(MAIN_FRAGMENT_KEY);
        mItemAdapter.restoreState(mainBundle);
        mRememberedPosition = mainBundle.getInt(FIRST_ITEM_KEY);
        Log.d("restorre", String.valueOf(mRememberedPosition));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mItemAdapter);
        Log.d("(Integer)mRememberedPosition != null", String.valueOf((Integer)mRememberedPosition != null));
        if((Integer)mRememberedPosition != null) {
            Log.d("rem pos", String.valueOf(mRememberedPosition));
            mRecyclerView.scrollToPosition(mRememberedPosition);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            refresh();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement MainFragment.Callbacks.");
        }
        try {
            mItemInteractionCallbacks = (ItemAdapter.ItemInteractionCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemAdapter.ItemInteractionCallbacks.");
        }
        mItemAdapter = new ItemAdapter(this.getActivity(), this, mItemInteractionCallbacks);
        mFinishedLoadingRefresh = true;
        mFinishedLoadingBottom = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        if (refresh != null) {
            refresh.setVisible(mFinishedLoadingRefresh);
        }
    }

    public void refresh() {
        mCallbacks.supportInvalidateOptionsMenu();
        if (mTopStoriesAccessor != null) {
            mTopStoriesAccessor.cancelPendingCallbacks();
        }
        //avoids fetching items twice before refresh is complete
        //i.e. when all items fit on 1 screen, that would trigger a bottom-load otherwise
        mFinishedLoadingBottom = false;
        mTopStoriesAccessor = new TopStoriesAccessor();
        mTopStoriesAccessor.getNextThreads(getStoriesToFetchCount(), new TopStoriesAccessor.GetNextThreadsCallbacks() {
            @Override
            public void onSuccess(List<Thread> threads) {
                mItemAdapter.clear();
                for (Thread thread : threads) {
                    mItemAdapter.add(thread);
                }
                mItemAdapter.notifyDataSetChanged();
                mFinishedLoadingRefresh = true;
                mFinishedLoadingBottom = true;
                //fragment could be detached from activity by now
                if (isAdded()) {
                    mCallbacks.supportInvalidateOptionsMenu();
                    mCallbacks.showToast("Loaded");
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private int getStoriesToFetchCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int storiesToFetch = prefs.getInt(getString(R.string.stories_to_load), 10);
        storiesToFetch = storiesToFetch < 1 ? 10 : storiesToFetch;
        Log.d("", "fetch: " + storiesToFetch);
        return storiesToFetch;
    }

    @Override
    public void onReachedBottom() {
        mCallbacks.showToast("begin loading");
        if (mFinishedLoadingBottom) {
            mCallbacks.showToast("begin loading actualy");
            mFinishedLoadingBottom = false;
            mTopStoriesAccessor.getNextThreads(getStoriesToFetchCount(), new TopStoriesAccessor.GetNextThreadsCallbacks() {
                public void onSuccess(List<Thread> threads) {
                    mCallbacks.showToast("onsucess");
                    if (threads.size() == 0) {
                        if (isAdded()) {
                            mCallbacks.showToast("No More Articles");
                        }
                    } else {
                        for (Thread thread : threads) {
                            mItemAdapter.add(thread);
                        }
                        mItemAdapter.notifyDataSetChanged();
                        if (isAdded()) {
                            mCallbacks.showToast("Loaded");
                        }
                    }

                    mFinishedLoadingBottom = true;
                    if (isAdded()) {
                        mCallbacks.supportInvalidateOptionsMenu();
                    }

                }

                @Override
                public void onError() {
                    onReachedBottom();
                    mCallbacks.showToast("error getting stories");
                    Log.d("MainFragment", "Error getting next stories");
                }
            });
        }
    }

    interface Callbacks {
        public void showToast(String text);

        public void supportInvalidateOptionsMenu();
    }
}
