package com.team11.hackernews;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class WatchedThreadsContentProvider extends ContentProvider {
    private static final String TAG = "WatchedThreadsContentProvider";
    private static final String AUTHORITY = "com.team11.hackernews.providers.watchedthreads";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private WatchedThreadsOpenHelper mWatchedThreadsOpenHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //correct this to use selection arg
        SQLiteDatabase db = mWatchedThreadsOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + WatchedThreadsOpenHelper.WATCHED_THREADS_TABLE_NAME +
                 " WHERE "+ WatchedThreadsOpenHelper.KEY_THREAD_ID+"="+selectionArgs[0]);
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mWatchedThreadsOpenHelper.getWritableDatabase();
        String id = values.getAsString("id");
        db.execSQL("INSERT INTO " + WatchedThreadsOpenHelper.WATCHED_THREADS_TABLE_NAME +
                " ( " + WatchedThreadsOpenHelper.KEY_THREAD_ID + " ) " + " VALUES "
                + "(" + values.getAsString("id") + ")");
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("watched_threads" + "/" + id);
    }

    @Override
    public boolean onCreate() {
        mWatchedThreadsOpenHelper = new WatchedThreadsOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table
        queryBuilder.setTables(WatchedThreadsOpenHelper.WATCHED_THREADS_TABLE_NAME);

        SQLiteDatabase db = mWatchedThreadsOpenHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
