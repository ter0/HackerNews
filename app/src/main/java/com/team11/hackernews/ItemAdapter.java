package com.team11.hackernews;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.data.AskHN;
import com.team11.hackernews.api.data.Job;
import com.team11.hackernews.api.data.Poll;
import com.team11.hackernews.api.data.Story;
import com.team11.hackernews.api.data.Thread;

import java.net.URL;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static final String THREAD_LIST_KEY = "THREAD_LIST";
    public static final String ITEM_ADAPTER_KEY = "ITEM_ADAPTER";

    Callbacks mCallbacks;
    ItemInteractionCallbacks mItemInteractionCallbacks;
    private ArrayList<Thread> mItemArrayList;
    private int mMaxBinded;

    public ItemAdapter(Callbacks callbacks, ItemInteractionCallbacks itemInteractionCallbacks) {
        mItemArrayList = new ArrayList<Thread>();
        mMaxBinded = 0;
        mCallbacks = callbacks;
        mItemInteractionCallbacks = itemInteractionCallbacks;
    }

    public void saveState(Bundle outState) {
        Bundle itemAdapterBundle = new Bundle();
        itemAdapterBundle.putParcelableArrayList(THREAD_LIST_KEY, mItemArrayList);
        outState.putBundle(ITEM_ADAPTER_KEY, itemAdapterBundle);
    }

    public void restoreState(Bundle inState) {
        Bundle itemAdapterBundle = inState.getBundle(ITEM_ADAPTER_KEY);
        mItemArrayList = itemAdapterBundle.getParcelableArrayList(THREAD_LIST_KEY);
    }

    public void add(Thread item) {
        mItemArrayList.add(item);
    }

    public void clear() {
        mItemArrayList.clear();
        mMaxBinded = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (mMaxBinded < i) {
            mMaxBinded = i;
            if (mMaxBinded == this.getItemCount() - 1) {
                mCallbacks.onReachedBottom();
            }
        }
        //TextView itemTitle = (TextView)viewHolder.findViewById(R.id.mTitle);

        Thread currentItem = mItemArrayList.get(i);

        viewHolder.mTitle.setText(currentItem.getTitle());
        viewHolder.mScore.setText(currentItem.getScore().toString());
        viewHolder.mBy.setText(currentItem.getBy());
        CharSequence timeAgo = getTime(currentItem.getTime());
        viewHolder.mTime.setText(timeAgo);
        Integer commentCount;
        String commentString;
        if (currentItem instanceof Story || currentItem instanceof Job) {
            URL URLObject;

            if (currentItem instanceof Story) {
                URLObject = ((Story) currentItem).getURL();
                // Story will have comments
                commentCount = currentItem.getKids().size();
                commentString = commentCount.toString() + " comments";
                viewHolder.mComments.setText(commentString);
            } else {
                URLObject = ((Job) currentItem).getURL();
                // A job posting will not (Replace comments with Job Posting
                viewHolder.mComments.setText("Job Posting");
            }

            String domainName = "";
            if (URLObject != null) {
                domainName = URLObject.getHost();
                if (domainName.startsWith("www.")) {
                    domainName = domainName.substring(4);
                }
                domainName = "(" + domainName + ")";
            }
            viewHolder.mDomain.setText(domainName);
        } else if (currentItem instanceof Poll) {
            viewHolder.mDomain.setText("User Poll");
            commentCount = currentItem.getKids().size();
            commentString = commentCount.toString() + " comments";
            viewHolder.mComments.setText(commentString);
        } else if (currentItem instanceof AskHN) {
            viewHolder.mDomain.setText("Ask HN");
            commentCount = currentItem.getKids().size();
            commentString = commentCount.toString() + " comments";
            viewHolder.mComments.setText(commentString);
        }

        //TODO: Check differences in binding web view to ASKHN and Polls

        /*viewHolder.mComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemInteractionCallbacks.loadCommentsView(mItemArrayList.get(i));
            }
        });*/
        if (currentItem instanceof Story) {
            //Stories require both a URL on title and link to comments
            viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = ((Story) mItemArrayList.get(i)).getURL().toString();
                    mItemInteractionCallbacks.loadWebView(url);
                }

            });
            viewHolder.mComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemInteractionCallbacks.loadCommentsView(mItemArrayList.get(i));
                }
            });
        } else if (currentItem instanceof Job) {
            //Jobs have no comments and a URL link in the title
            viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = ((Job) mItemArrayList.get(i)).getURL().toString();
                    mItemInteractionCallbacks.loadWebView(url);
                }
            });
            viewHolder.mComments.setOnClickListener(null);
        } else if ((currentItem instanceof AskHN) || (currentItem instanceof Poll)) {
            //AskHN and Polls both should open comments when either Title or Comments is pressed
            viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemInteractionCallbacks.loadCommentsView(mItemArrayList.get(i));
                }
            });
            viewHolder.mComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemInteractionCallbacks.loadCommentsView(mItemArrayList.get(i));
                }
            });
        }
    }

    private CharSequence getTime(long timeStamp) {
        timeStamp = timeStamp * 1000;
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(timeStamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        return timeAgo;
    }

    @Override
    public int getItemCount() {
        return mItemArrayList.size();
    }

    public interface Callbacks {
        public void onReachedBottom();
    }

    public interface ItemInteractionCallbacks {
        public void loadWebView(String url);

        public void loadCommentsView(Thread thread);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mTextView;
        public TextView mTitle;
        public TextView mScore;
        public TextView mBy;
        public TextView mTime;
        public TextView mDomain;
        public TextView mComments;

        public ViewHolder(LinearLayout v) {
            // each data item is just a string in this case
            super(v);
            mTextView = v;
            mTitle = (TextView) v.findViewById(R.id.mTitle);
            mScore = (TextView) v.findViewById(R.id.mScore);
            mBy = (TextView) v.findViewById(R.id.mBy);
            mTime = (TextView) v.findViewById(R.id.mTime);
            mDomain = (TextView) v.findViewById(R.id.mDomain);
            mComments = (TextView) v.findViewById(R.id.mComments);
        }
    }
}
