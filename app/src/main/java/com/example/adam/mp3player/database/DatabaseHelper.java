package com.example.adam.mp3player.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
PLAYLIST_ID INT
SONG_PATH TEXT
*/


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String SQL_CREATE_QUERY = "CREATE TABLE "+ FeedReaderContract.FeedEntry.TABLE_NAME+
            " ("+ FeedReaderContract.FeedEntry.PLAYLIST_ID+" INTEGER," +
            FeedReaderContract.FeedEntry.PLAYLIST_NAME+" TEXT,"+
            FeedReaderContract.FeedEntry.SONG_PATH +" TEXT )";
    public static final String SQL_DROP_QUERY = "DROP TABLE IF EXISTS "+
            FeedReaderContract.FeedEntry.TABLE_NAME;
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "playlist.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_QUERY);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
