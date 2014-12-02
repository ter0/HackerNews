package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.AskHN;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Poll;
import com.team11.hackernews.api.Thread;
import com.team11.hackernews.api.Story;
import com.team11.hackernews.api.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoryAccessor extends Accessor {

    public void getStory(long id, final GetStoryCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                Thread thread;

                Utils.ItemType itemType = Utils.getItemTypeFromString(map.get("type").toString());

                if (itemType == Utils.ItemType.Story) {

                    if (map.get("title").toString().startsWith("Ask HN:")){
                        // Ask HN thread
                        thread = new AskHN();

                    } else {

                        // Story thread
                        thread = new Story();

                        URL url = null;
                        try {
                            url = new URL((String) map.get("url"));
                        } catch (MalformedURLException ignored) {
                        }

                        ((Story) thread).setURL(url);
                    }

                } else if (itemType == Utils.ItemType.Poll) {

                    // Poll thread
                    thread = new Poll();

                    List<Long> pollOpts = (List<Long>) map.get("pollopts");
                    if (pollOpts == null) {
                        // TODO: this is when the poll has no options, possibly set the onError() to take an Error object
                        callbacks.onError();
                    }

                } else {

                    //TODO: the id provided does not point to a relevant item type, return proper error
                    callbacks.onError();
                    return;

                }

                // These fields are common to all thread types so we can parse them for whatever thread type is being requested
                List<Long> kids = (List<Long>) map.get("kids");
                if (kids == null) {
                    kids = new ArrayList<Long>();
                }

                thread.setBy((String) map.get("by"));
                thread.setId((Long) map.get("id"));
                thread.setKids(kids);
                thread.setScore((Long) map.get("score"));
                thread.setTime((Long) map.get("time"));
                thread.setText((String) map.get("text"));
                thread.setTitle((String) map.get("title"));

                callbacks.onSuccess(thread);
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

    public void getMultipleStories(final List<Long> ids, final GetMultipleStoriesCallbacks callbacks) {
        final List<Thread> threads = new ArrayList<Thread>();

        if (ids.size() == 0) {
            callbacks.onSuccess(threads);
            return;
        }

        for (final long id : ids) {
            getStory(id, new GetStoryCallbacks() {
                @Override
                public void onSuccess(Thread thread) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    threads.add(thread);

                    if (threads.size() == ids.size()) {
                        callbacks.onSuccess(threads);
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

    public interface GetStoryCallbacks {
        public void onSuccess(Thread thread);

        public void onError();
    }

    public interface GetMultipleStoriesCallbacks {
        public void onSuccess(List<Thread> threads);

        public void onError();
    }
}
