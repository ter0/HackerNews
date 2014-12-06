package com.team11.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team11.hackernews.api.data.Thread;

public class ThreadFragment extends Fragment {

    private CommentsView mCommentView;
    private Thread mStory;

    public ThreadFragment() {
        // Required empty public constructor
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
