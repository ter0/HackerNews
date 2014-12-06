package com.team11.hackernews.api.accessors;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsAccessor extends Accessor {

    public void getChildComments(Thread thread, final GetChildCommentsCallbacks callbacks) {
        getComments(thread.getKids(), 0, callbacks);
    }

    public void getChildComments(Comment parent, final GetChildCommentsCallbacks callbacks) {
        getComments(parent.getKids(), parent.getDepth(), callbacks);
    }

    private void getComments(List<Long> ids, int parentDepth, final GetChildCommentsCallbacks callbacks) {

        final int noOfKids = ids.size();

        final List<Comment> comments = new ArrayList<Comment>();

        if (noOfKids == 0) {
            callbacks.onSuccess(comments);
        }

        final HashMap<Long, Boolean> isDeleted = new HashMap<Long, Boolean>();

        for (long id : ids) {
            getComment(id, parentDepth + 1, new GetCommentCallbacks() {
                @Override
                public void onSuccess(Comment comment) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }

                    comments.add(comment);
                    isDeleted.put(comment.getId(), false);

                    runIfAllReturned();
                }

                @Override
                public void onDeleted(long id) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }

                    isDeleted.put(id, true);

                    runIfAllReturned();
                }

                private void runIfAllReturned() {
                    if (isDeleted.size() != noOfKids) {
                        return;
                    }

                    callbacks.onSuccess(comments);
                }

                @Override
                public void onWrongItemType(Utils.ItemType itemType) {
                    callbacks.onError();
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

    private void getComment(final long id, final int depth, final GetCommentCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                Boolean deleted = (Boolean) map.get("deleted");

                if ((deleted != null) && deleted) {
                    callbacks.onDeleted(id);
                    return;
                }

                Utils.ItemType itemType = Utils.getItemTypeFromString(map.get("type").toString());

                if (itemType != Utils.ItemType.Comment) {
                    callbacks.onWrongItemType(itemType);
                    return;
                }

                Comment comment = new Comment.Builder()
                        .by((String) map.get("by"))
                        .id((Long) map.get("id"))
                        .kids((ArrayList<Long>) map.get("kids"))
                        .time((Long) map.get("time"))
                        .text((String) map.get("text"))
                        .parent((Long) map.get("parent"))
                        .depth(depth)
                        .build();

                callbacks.onSuccess(comment);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                Log.d("Error", firebaseError.getMessage());
                callbacks.onError();
            }
        });
    }

    public interface GetChildCommentsCallbacks {
        public void onSuccess(List<Comment> comments);

        public void onError();
    }

    private interface GetCommentCallbacks {
        public void onSuccess(Comment comment);

        public void onDeleted(long id);

        public void onWrongItemType(Utils.ItemType itemType);

        public void onError();
    }
}
