package com.team11.hackernews.api.accessors;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.team11.hackernews.api.Comment;
import com.team11.hackernews.api.HackerNewsAPI;
import com.team11.hackernews.api.Item;
import com.team11.hackernews.api.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentsAccessor extends Accessor{

    public interface GetChildCommentsCallbacks {
        public void onSuccess(List<Comment> comments);
        public void onError();
    }

    public void getChildComments(Item story, final GetChildCommentsCallbacks callbacks) {
        getComments(story.getKids(), 0, callbacks);
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

        for (long id : ids) {
            getComment(id, parentDepth + 1, new GetCommentCallbacks() {
                @Override
                public void onSuccess(Comment comment) {
                    if (mCancelPendingCallbacks) {
                        return;
                    }
                    comments.add(comment);

                    if (comments.size() != noOfKids) {
                        return;
                    }

                    callbacks.onSuccess(comments);
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

    private interface GetCommentCallbacks {
        public void onSuccess(Comment comment);
        public void onError();
    }

    private void getComment(long id, final int depth, final GetCommentCallbacks callbacks) {
        Utils.getFirebaseInstance().child(HackerNewsAPI.ITEM + "/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mCancelPendingCallbacks) {
                    return;
                }
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("type") != "comment") {
                    callbacks.onError();
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
                callbacks.onError();
            }
        });
    }
}
