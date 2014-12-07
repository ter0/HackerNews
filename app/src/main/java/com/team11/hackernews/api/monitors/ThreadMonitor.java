package com.team11.hackernews.api.monitors;

import com.team11.hackernews.api.Utils;
import com.team11.hackernews.api.accessors.CommentsAccessor;
import com.team11.hackernews.api.accessors.StoryAccessor;
import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.data.Thread;

public class ThreadMonitor extends Monitor{

    private Thread mThread;

    public ThreadMonitor(Thread thread) {
        super();
        mThread = thread;
    }

    public void addListener(final ThreadMonitorCallbacks callbacks) {

        addListener(new AddListenerCallbacks() {

            @Override
            public void onNewItem(Long id) {

                new CommentsAccessor().getComment(id, new CommentsAccessor.GetCommentCallbacks() {
                    @Override
                    public void onSuccess(final Comment comment) {
                        getRootParent(comment.getId(), new GetRootParentCallbacks() {

                            @Override
                            public void onFound(Thread thread) {
                                if (thread.getId() == mThread.getId()) {
                                    callbacks.onCommentUpdate(comment, thread);
                                }
                            }

                        });
                    }

                    @Override
                    public void onDeleted(long id) {

                    }

                    @Override
                    public void onWrongItemType(Utils.ItemType itemType, long id) {
                        new StoryAccessor().getStory(id, new StoryAccessor.GetStoryCallbacks() {
                            @Override
                            public void onSuccess(Thread thread) {
                                callbacks.onThreadUpdate(thread);
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

                    @Override
                    public void onError() {

                    }
                });



            }
        });
    }

    public interface ThreadMonitorCallbacks {
        public void onThreadUpdate(Thread thread);
        public void onCommentUpdate(Comment comment, Thread parentThread);
    }
}
