package com.team11.hackernews;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.monitors.ThreadMonitor;

import java.util.ArrayList;

public class WatcherService extends Service {
    private WatchedThreadsOpenHelper mWatchedThreadsOpenHelper;
    private ArrayList<ThreadMonitor> mThreadMonitorList;

    public WatcherService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WatcherService", "WatcherService started");
        mWatchedThreadsOpenHelper = new WatchedThreadsOpenHelper(this);
        mThreadMonitorList = new ArrayList<ThreadMonitor>();
        SQLiteDatabase db = mWatchedThreadsOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(WatchedThreadsOpenHelper.WATCHED_THREADS_TABLE_NAME,
                new String[]{WatchedThreadsOpenHelper.KEY_THREAD_ID},
                null,
                null,
                null,
                null,
                null);
        int threadIdIndex = cursor.getColumnIndex(WatchedThreadsOpenHelper.KEY_THREAD_ID);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(threadIdIndex);
            Log.d("WatcherService", "watching id: " + id);
            ThreadMonitor monitor = new ThreadMonitor(id);

            monitor.addListener(new ThreadMonitor.ThreadMonitorCallbacks() {
                @Override
                public void onThreadUpdate(com.team11.hackernews.api.data.Thread thread) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(WatcherService.this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Hacker News")
                                    .setContentText(thread.getTitle() + " updated");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }

                @Override
                public void onCommentUpdate(Comment comment, Thread parentThread) {
                    Log.d("WatcherService", "comment " + comment.getId() + " updated");
                }
            });
            mThreadMonitorList.add(monitor);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
