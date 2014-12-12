package com.team11.hackernews.api.monitors;

import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.accessors.CommentsAccessor;
import com.team11.hackernews.api.accessors.ThreadAccessor;
import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.data.User;


public class UserMonitor extends Monitor {

    private User mUser;

    public UserMonitor(User user) {
        super();
        mUser = user;
    }

    public void addListener(final UserMonitorCallbacks callbacks) {
        addListener(new AddListenerCallbacks() {

            @Override
            public void onNewItem(Long id) {
                new ThreadAccessor().getStory(id, new ThreadAccessor.GetStoryCallbacks() {
                    @Override
                    public void onSuccess(Thread thread) {
                        if (thread.getBy() == mUser.getId()) {
                            callbacks.onThreadUpdate(thread);
                        }
                    }

                    @Override
                    public void onDeleted(long id) {

                    }

                    @Override
                    public void onWrongItemType(Utils.ItemType itemType, long id) {
                        if (itemType == Utils.ItemType.Comment) {
                            CommentsAccessor commentsAccessor = new CommentsAccessor();
                            commentsAccessor.getComment(id, new CommentsAccessor.GetCommentCallbacks() {
                                @Override
                                public void onSuccess(final Comment comment) {
                                    getRootParent(comment.getId(), new GetRootParentCallbacks() {
                                        @Override
                                        public void onFound(Thread thread) {
                                            callbacks.onCommentUpdate(comment, thread);
                                        }
                                    });
                                }

                                @Override
                                public void onDeleted(long id) {

                                }

                                @Override
                                public void onWrongItemType(Utils.ItemType itemType, long id) {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(long id, Error error) {

                    }
                });
            }

        });
    }

    public interface UserMonitorCallbacks {
        public void onThreadUpdate(Thread thread);

        public void onCommentUpdate(Comment comment, Thread parentThread);
    }
}
