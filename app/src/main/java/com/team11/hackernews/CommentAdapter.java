package com.team11.hackernews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Item;

import java.util.ArrayList;
import java.util.Random;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Item> mCommentArrayList;

    public CommentAdapter() {
        mCommentArrayList = new ArrayList<Item>();
    }

    public void add(Item item) {
        mCommentArrayList.add(item);
    }

    public void clear() {
        mCommentArrayList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_view_comment_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mCommentArrayList.get(position);
        // item.getDepth();
        holder.itemView.setTag(R.id.comment_tag, new Random().nextInt(2));
        holder.mAuthorTextView.setText(item.getBy());
        holder.mScoreTextView.setText(item.getScore() + " points");
        holder.mTimeTextView.setText("" + item.getTime());
        holder.mCommentTextView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return mCommentArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTextView;
        public TextView mScoreTextView;
        public TextView mTimeTextView;
        public TextView mCommentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.comment_author);
            mScoreTextView = (TextView) itemView.findViewById(R.id.comment_score);
            mTimeTextView = (TextView) itemView.findViewById(R.id.comment_time);
            mCommentTextView = (TextView) itemView.findViewById(R.id.comment_text);
        }
    }
}
