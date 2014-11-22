package com.team11.hackernews;

import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;
import com.team11.hackernews.api.value_event_listeners.ItemFromId;
import com.team11.hackernews.api.value_event_listeners.TopStories;

import java.util.ArrayList;
import java.util.List;

public class Refresh implements TopStories.Callbacks, ItemFromId.Callbacks{

    interface Callbacks{
        public void startRefresh();
    }

    Callbacks mCallbacks;
    Firebase mFirebase;
    ArrayList<Item> items;
    int mStoryCount;

    public Refresh(Callbacks callbacks, Firebase firebase){
        mCallbacks = callbacks;
        mFirebase = firebase;
    }

    protected Integer doInBackground(Void ... voids) {
        TopStories topStories = new TopStories(this);
        mFirebase.child(HackerNewsAPI.TOP_STORIES).addListenerForSingleValueEvent(topStories);
        return 0;
    }
    public void useMessages(List<String> idList){
        mStoryCount = idList.size();
        ItemFromId itemFromId = new ItemFromId(this);
        for (String id : idList) {
            mFirebase.child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(itemFromId);
        }
    }
    public void addItem(Item item){
        items.add(item);
    }
    public void itemFailed(){
        mStoryCount -= 1;
    }

    /*@Override
    protected void onPostExcute(){
        mItemAdapter.notifyDataSetChanged();
        if (mItemAdapter.getItemCount() == ITEMS_PER_PAGE) {
            finishedLoading = true;
            supportInvalidateOptionsMenu();
        }
    }*/

}
