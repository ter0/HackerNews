package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.data.PollOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PollOptionAccessor extends Accessor {

    public void getPollOptions(final List<Long> ids, final GetPollOptionsCallbacks callbacks) {
        final List<PollOption> pollOpts = new ArrayList<PollOption>();

        if (ids.size() == 0) {
            callbacks.onSuccess(pollOpts);
            return;
        }

        for (final long id : ids) {
            getPollOption(id, new GetPollOptionCallbacks() {
                @Override
                public void onSuccess(PollOption pollOpt) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    pollOpts.add(pollOpt);

                    if (pollOpts.size() == ids.size()) {
                        callbacks.onSuccess(pollOpts);
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

    private void getPollOption(long id, final GetPollOptionCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                Utils.ItemType itemType = Utils.getItemTypeFromString(map.get("type").toString());
                if (itemType != Utils.ItemType.Pollopt) {
                    callbacks.onError();
                }

                PollOption pollOption = new PollOption.Builder()
                        .by((String) map.get("by"))
                        .id((Long) map.get("id"))
                        .parent((Long) map.get("parent"))
                        .score((Long) map.get("score"))
                        .text((String) map.get("text"))
                        .time((Long) map.get("time"))
                        .build();

                callbacks.onSuccess(pollOption);
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

    public interface GetPollOptionsCallbacks {
        public void onSuccess(List<PollOption> pollOpts);

        public void onError();
    }

    public interface GetPollOptionCallbacks {
        public void onSuccess(PollOption pollOpt);

        public void onError();
    }


}
