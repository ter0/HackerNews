package com.team11.hackernews;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.team11.hackernews.api.data.Comment;
import com.team11.hackernews.api.data.Thread;
import com.team11.hackernews.api.monitors.ThreadMonitor;

import java.util.ArrayList;

public class WatcherService extends Service implements Loader.OnLoadCompleteListener<Cursor> {
    private WatchedThreadsOpenHelper mWatchedThreadsOpenHelper;
    private ArrayList<ThreadMonitor> mThreadMonitorList;
    private CursorLoader mCursorLoader;

    public WatcherService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Log.d("WatcherService", "WatcherService started");
        mWatchedThreadsOpenHelper = new WatchedThreadsOpenHelper(this);
        mThreadMonitorList = new ArrayList<ThreadMonitor>();
        mCursorLoader = new CursorLoader(getApplicationContext(), WatchedThreadsContentProvider.CONTENT_URI, null, null, null, null);
        mCursorLoader.registerListener(0, this);
        mCursorLoader.startLoading();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We perform no IPC so this is not required
        throw new UnsupportedOperationException("This service provides no IPC");
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
        // we have the new data
        Log.d("WatcherService", "loader returned fresh data");
        mThreadMonitorList.clear();
        int threadIdIndex = cursor.getColumnIndex(WatchedThreadsOpenHelper.KEY_THREAD_ID);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(threadIdIndex);
            Log.d("WatcherService", "watching id: " + id);
            ThreadMonitor monitor = new ThreadMonitor(id);

            monitor.addListener(new ThreadMonitor.ThreadMonitorCallbacks() {
                @Override
                public void onThreadUpdate(com.team11.hackernews.api.data.Thread thread) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(WatcherService.this)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Hacker News")
                                    .setContentText(thread.getTitle() + " updated");
                    // pending intent is redirection using the deep-link
                    Intent resultIntent = new Intent(Intent.ACTION_VIEW);
                    resultIntent.setData(Uri.parse("http://news.ycombinator.com/item?id=" + thread.getId()));

                    PendingIntent pending = PendingIntent.getActivity(WatcherService.this, 0, resultIntent, 0);
                    notificationBuilder.setContentIntent(pending);

                    // using the same tag and Id causes the new notification to replace an existing one
                    mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), 0, notificationBuilder.build());
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
    public void onDestroy() {
        super.onDestroy();
        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }
}
