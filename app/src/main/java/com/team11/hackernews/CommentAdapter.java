package com.team11.hackernews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> mCommentArrayList;

    public CommentAdapter(Context context) {
        mCommentArrayList = new ArrayList<Comment>();
    }

    public void add(Comment item) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Comment comment = mCommentArrayList.get(position);
        holder.itemView.setTag(R.id.comment_tag, comment.getDepth());
        holder.mAuthorTextView.setText(comment.getBy());
        holder.mTimeTextView.setText("" + comment.getTime());
        holder.mCommentTextView.setText(Html.fromHtml(comment.getText()));
    }

    @Override
    public int getItemCount() {
        return mCommentArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTextView;
        public TextView mTimeTextView;
        public TextView mCommentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.comment_author);
            mTimeTextView = (TextView) itemView.findViewById(R.id.comment_time);
            mCommentTextView = (TextView) itemView.findViewById(R.id.comment_text);
        }
    }
}
