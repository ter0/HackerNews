package com.team11.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team11.hackernews.api.Thread;
import com.team11.hackernews.api.accessors.TopStoriesAccessor;

import java.util.List;

public class MainFragment extends Fragment implements ItemAdapter.Callbacks {
    Callbacks mCallbacks;
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private TopStoriesAccessor mTopStoriesAccessor;
    private boolean mFinishedLoadingRefresh;
    private boolean mFinishedLoadingBottom;

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
        mFinishedLoadingRefresh = true;
        mFinishedLoadingBottom = true;
        mItemAdapter = new ItemAdapter(this);
        setHasOptionsMenu(true);
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mItemAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement MainFragment Callbacks.");
        }
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

    @Override
    public void onReachedBottom() {
        if (mFinishedLoadingBottom) {
            mFinishedLoadingBottom = false;
            mTopStoriesAccessor.getNextStories(new TopStoriesAccessor.GetTopStoriesCallbacks() {
                public void onSuccess(List<Thread> threads) {
                    if (threads.size() == 0) {
                        mCallbacks.showToast("No More Articles");
                    } else {
                        for (Thread thread : threads) {
                            mItemAdapter.add(thread);
                        }
                        mItemAdapter.notifyDataSetChanged();
                        mCallbacks.showToast("Loaded");
                    }

                    mFinishedLoadingBottom = true;
                    mCallbacks.supportInvalidateOptionsMenu();

                }

                @Override
                public void onError() {

                }
            });
        }
    }

    interface Callbacks {
        public void showToast(String text);

        public void supportInvalidateOptionsMenu();
    }
}
