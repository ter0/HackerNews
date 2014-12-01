package com.team11.hackernews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CommentLayoutManager extends LinearLayoutManager {

    private Context mContext;

    public CommentLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    public CommentLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mContext = context;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        //indent the view based on the childs depth
        Object tag = child.getTag(R.id.comment_tag);
        if (tag == null) {
            throw new IllegalStateException("View MUST set it's depth using R.id.comment_tag or whatever");
        }
        int depth = (Integer) tag;
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.comment_padding);
        child.setPadding(padding + 100 * (depth - 1), 0, 0, 0);
    }
}
