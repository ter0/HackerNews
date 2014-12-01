package com.team11.hackernews;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Story;
import com.team11.hackernews.api.Thread;

import java.net.URL;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Callbacks mCallbacks;
    private ArrayList<Thread> mItemArrayList;
    private int mMaxBinded;

    public ItemAdapter(Callbacks callbacks) {
        mItemArrayList = new ArrayList<Thread>();
        mMaxBinded = 0;
        mCallbacks = callbacks;
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
        if (currentItem instanceof Story){
            Story currentStory = (Story) currentItem;
            URL URLObject = currentStory.getURL();
            String domainName = "";
            if (URLObject != null) {
                domainName = URLObject.getHost();
            }
            if(domainName.startsWith("www.")){
                domainName = domainName.substring(4);
            }
            if(domainName != ""){
                domainName = "(" + domainName + ")";
            }
            viewHolder.mDomain.setText(domainName);
        }

        //TODO: set use cases for other thread types a la AskHN and Polls

        Integer commentCount = currentItem.getKids().size();
        String commentString = commentCount.toString() + " comments";
        viewHolder.mComments.setText(commentString);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DummyStoryView.class);
                intent.putExtra(Story.STORY_PARCEL_KEY, (Story)mItemArrayList.get(i));
                v.getContext().startActivity(intent);
            }
        });
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
