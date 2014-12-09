package com.team11.hackernews;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchedThreadsOpenHelper extends SQLiteOpenHelper {

    public static final String WATCHED_THREADS_TABLE_NAME = "watched_threads";
    public static final String KEY_THREAD_ID = "thread_id";
    private static final String WATCHED_THREADS_TABLE_CREATE =
            "CREATE TABLE " + WATCHED_THREADS_TABLE_NAME + " (" +
                    KEY_THREAD_ID + " INTEGER);";
    private static final int DATABASE_VERSION = 3;

    WatchedThreadsOpenHelper(Context context) {
        super(context, WATCHED_THREADS_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WATCHED_THREADS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing fancy for now, just drop the table
        db.execSQL("DROP TABLE IF EXISTS " + WATCHED_THREADS_TABLE_NAME);
        onCreate(db);
    }
}
