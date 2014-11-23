package com.team11.hackernews;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team11.hackernews.api.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<Item> mItemArrayList;
    private int mMaxBinded;

    public interface Callbacks{
        public void reachedBottom();
    }

    Callbacks mCallbacks;

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
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(mMaxBinded < i){
            mMaxBinded = i;
            if(mMaxBinded == this.getItemCount()-1){
                mCallbacks.reachedBottom();
            }
        }
        viewHolder.mTextView.setText(mItemArrayList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            // each data item is just a string in this case
            super(v);
            mTextView = v;
        }
    }
}
