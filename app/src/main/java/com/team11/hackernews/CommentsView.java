package com.team11.hackernews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.team11.hackernews.api.Item;


/**
 * TODO: document your custom view class.
 */
public class CommentsView extends FrameLayout {
    private RecyclerView mCommentsRecyclerView;
    private CommentAdapter mCommentAdapter;

    public CommentsView(Context context) {
        super(context);
        init(null, 0);
    }

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.comments_view, this);
        mCommentAdapter = new CommentAdapter();
        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.comments_view_recyclerview);
        mCommentsRecyclerView.setLayoutManager(new CommentLayoutManager(getContext()));
        mCommentsRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.add(new Item.Builder().build());
        mCommentAdapter.add(new Item.Builder().build());
        mCommentAdapter.add(new Item.Builder().build());
        mCommentAdapter.add(new Item.Builder().build());
        mCommentAdapter.add(new Item.Builder().build());

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CommentsView, defStyle, 0);

        a.recycle();
    }
}