package com.team11.hackernews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Comment;
import com.team11.hackernews.api.Thread;
import com.team11.hackernews.api.accessors.CommentsAccessor;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> mCommentArrayList;
    private CommentsAccessor mCommentsAccessor;
    private Thread mThread;

    public CommentAdapter(Context context) {
        mCommentArrayList = new ArrayList<Comment>();
        mCommentsAccessor = new CommentsAccessor();
    }

    public void setThread(com.team11.hackernews.api.Thread thread) {
        mThread = thread;
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
        final Comment comment = mCommentArrayList.get(position);
        holder.itemView.setTag(R.id.comment_tag, comment.getDepth());
        holder.mAuthorTextView.setText(comment.getBy());
        holder.mTimeTextView.setText(getTime(comment.getTime()));
        holder.mCommentTextView.setText(Html.fromHtml(comment.getText()));
        if (comment.getKids() != null && comment.getKids().size() > 0) {
            holder.mShowMoreButton.setVisibility(View.VISIBLE);
            holder.mShowMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get kids for this comment
                    mCommentsAccessor.getChildComments(comment, new CommentsAccessor.GetChildCommentsCallbacks() {
                        @Override
                        public void onSuccess(List<Comment> comments) {
                            //insert directly underneath their parents entry
                            int newPosition = position;
                            for (Comment childComment : comments) {
                                addAfter(childComment, ++newPosition);
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });
        } else {
            holder.mShowMoreButton.setVisibility(View.GONE);
        }
    }

    private CharSequence getTime(long timeStamp) {
        timeStamp = timeStamp * 1000;
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(timeStamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        return timeAgo;
    }

    @Override
    public int getItemCount() {
        return mCommentArrayList.size();
    }


    /**
     * Add the given comment directly after the given position.
     * Subsequent entries will be pushed down the list.
     * If the position given is 5, the comment will be added at index 6
     *
     * @param comment  The comment to add
     * @param position The position to add after
     */
    public void addAfter(Comment comment, int position) {
        mCommentArrayList.add(position, comment);
    }

    public void refresh() {
        mCommentsAccessor.getChildComments(mThread, new CommentsAccessor.GetChildCommentsCallbacks() {
            @Override
            public void onSuccess(List<Comment> comments) {
                for (Comment comment : comments) {
                    CommentAdapter.this.add(comment);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onError() {
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTextView;
        public TextView mTimeTextView;
        public TextView mCommentTextView;
        public Button mShowMoreButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.comment_author);
            mTimeTextView = (TextView) itemView.findViewById(R.id.comment_time);
            mCommentTextView = (TextView) itemView.findViewById(R.id.comment_text);
            mShowMoreButton = (Button) itemView.findViewById(R.id.show_more_button);
        }
    }
}
