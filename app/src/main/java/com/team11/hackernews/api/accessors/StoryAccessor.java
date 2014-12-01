package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
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

                URL url = null;
                try {
                    url = new URL((String) map.get("url"));
                } catch (MalformedURLException ignored) {
                }

                List<Long> kids = (List<Long>) map.get("kids");
                if (kids == null) {
                    kids = new ArrayList<Long>();
                }

                Story story = new Story();
                story.setBy((String) map.get("by"));
                story.setId((Long) map.get("id"));
                story.setKids(kids);
                story.setScore((Long) map.get("score"));
                story.setTime((Long) map.get("time"));
                story.setText((String) map.get("text"));
                story.setURL(url);
                story.setTitle((String) map.get("title"));

                callbacks.onSuccess(story);
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

        for (final long id: ids){
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
