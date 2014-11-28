package com.team11.hackernews;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team11.hackernews.api.Item;

import java.util.ArrayList;
import java.util.Date;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Callbacks mCallbacks;
    private ArrayList<Item> mItemArrayList;
    private int mMaxBinded;

    public ItemAdapter(Callbacks callbacks) {
        mItemArrayList = new ArrayList<Item>();
        mMaxBinded = 0;
        mCallbacks = callbacks;
    }

    public void add(Item item) {
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

        TextView mTitle = (TextView)viewHolder.mTextView.findViewById(R.id.mTitle);
        TextView mScore = (TextView)viewHolder.mTextView.findViewById(R.id.mScore);
        TextView mBy = (TextView)viewHolder.mTextView.findViewById(R.id.mBy);
        TextView mTime = (TextView)viewHolder.mTextView.findViewById(R.id.mTime);
        mTitle.setText(mItemArrayList.get(i).getTitle());
        mScore.setText(mItemArrayList.get(i).getScore().toString());
        mBy.setText(mItemArrayList.get(i).getBy());
        String hoursAgo = getTime(mItemArrayList.get(i).getTime());
        mTime.setText(hoursAgo);
    }
    private String getTime(long timeStamp){
        String hoursAgo = DateUtils.getRelativeTimeSpanString(1417176774).toString();
        return hoursAgo;
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

        public ViewHolder(LinearLayout v) {
            // each data item is just a string in this case
            super(v);
            mTextView = v;
        }
    }
}
