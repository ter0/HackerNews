package com.team11.hackernews;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Story;

import java.util.ArrayList;
import java.net.URL;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Callbacks mCallbacks;
    private ArrayList<Story> mItemArrayList;
    private int mMaxBinded;

    public ItemAdapter(Callbacks callbacks) {
        mItemArrayList = new ArrayList<Story>();
        mMaxBinded = 0;
        mCallbacks = callbacks;
    }

    public void add(Story item) {
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mMaxBinded < i) {
            mMaxBinded = i;
            if (mMaxBinded == this.getItemCount() - 1) {
                mCallbacks.onReachedBottom();
            }
        }
        //TextView itemTitle = (TextView)viewHolder.findViewById(R.id.mTitle);
        viewHolder.mTitle.setText(mItemArrayList.get(i).getTitle());
        viewHolder.mScore.setText(mItemArrayList.get(i).getScore().toString());
        viewHolder.mBy.setText(mItemArrayList.get(i).getBy());
        CharSequence timeAgo = getTime(mItemArrayList.get(i).getTime());
        viewHolder.mTime.setText(timeAgo);
        URL URLObject = mItemArrayList.get(i).getURL();
        String domainName = "";
        //TODO: Waiting for askHN class to be created
        if(URLObject != null){
            domainName = URLObject.getHost();
        }
        viewHolder.mDomain.setText(domainName);
        Integer commentCount = mItemArrayList.get(i).getKids().size();
        String commentString = commentCount.toString() + " comments";
        viewHolder.mComments.setText(commentString);
    }
    private CharSequence getTime(long timeStamp){
        timeStamp = timeStamp*1000;
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
            mTitle = (TextView)v.findViewById(R.id.mTitle);
            mScore = (TextView)v.findViewById(R.id.mScore);
            mBy = (TextView)v.findViewById(R.id.mBy);
            mTime = (TextView)v.findViewById(R.id.mTime);
            mDomain = (TextView)v.findViewById(R.id.mDomain);
            mComments = (TextView)v.findViewById(R.id.mComments);
        }
    }
}
