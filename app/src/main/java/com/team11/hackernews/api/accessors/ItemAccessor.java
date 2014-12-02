package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;
import com.team11.hackernews.api.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAccessor extends Accessor {

    public void getItem(long id, final GetItemCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                // TODO: Currently returns null as Item generation function was deprecated and this class is being deprecated soon
                callbacks.onSuccess(null);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                callbacks.onError();
            }
        });
    }

    public void getMultipleItems(final List<Long> ids, final GetMultipleItemsCallbacks callbacks) {
        final List<Item> items = new ArrayList<Item>();

        if (ids.size() == 0) {
            callbacks.onSuccess(items);
            return;
        }

        for (final long id: ids){
            getItem(id, new GetItemCallbacks() {
                @Override
                public void onSuccess(Item item) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    items.add(item);

                    if (items.size() == ids.size()){
                        callbacks.onSuccess(items);
                    }
                }

                @Override
                public void onError() {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    callbacks.onError();
                }
            });
        }
    }



    public void cancelPendingCallbacks() {
        mCancelPendingCallbacks = true;
    }

    public interface GetItemCallbacks {
        public void onSuccess(Item item);
        public void onError();
    }

    public interface GetMultipleItemsCallbacks {
        public void onSuccess(List<Item> items);
        public void onError();
    }
}
