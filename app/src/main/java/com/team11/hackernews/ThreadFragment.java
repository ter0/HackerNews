package com.team11.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team11.hackernews.api.data.Thread;

public class ThreadFragment extends Fragment {

    private CommentsView mCommentView;
    private Thread mStory;
    private ShareActionProvider mShareActionProvider;

    public ThreadFragment() {
        // Required empty public constructor
    }

    public Thread getThread(){
        return mStory;
    }

    public static ThreadFragment newInstance(Parcelable thread) {
        ThreadFragment fragment = new ThreadFragment();
        Bundle args = new Bundle();
        args.putParcelable(Thread.THREAD_PARCEL_KEY, thread);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStory = getArguments().getParcelable(Thread.THREAD_PARCEL_KEY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_thread, container, false);
        ((TextView) rootView.findViewById(R.id.story_title)).setText(mStory.getTitle());
        mCommentView = (CommentsView) rootView.findViewById(R.id.comment_view);
        mCommentView.setThread(mStory);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_thread, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_thread_item_share);
        if(menuItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                    .setType("text/plain")
                    .setText(mStory.getThreadLink()).getIntent();
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
