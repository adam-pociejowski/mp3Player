package com.example.adam.mp3player.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-15.
 */
public class DatabaseAdapter {
    private DatabaseHelper databaseHelper;

    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertData(String songPath, String playlistName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseHelper.SONG_PATH, songPath);
        contentValues.put(databaseHelper.PLAYLIST_NAME, playlistName);
        long id = db.insert(databaseHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<String> getAllData() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] columns = { DatabaseHelper.ID, DatabaseHelper.SONG_PATH, DatabaseHelper.PLAYLIST_NAME };
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DatabaseHelper.ID);
            int index2 = cursor.getColumnIndex(DatabaseHelper.SONG_PATH);
            int index3 = cursor.getColumnIndex(DatabaseHelper.PLAYLIST_NAME);
            String row = cursor.getInt(index1)+" "+cursor.getString(index2)+" "+cursor.getString(index3);
            result.add(row);
        }
        return result;
    }

    public ArrayList<String> getAllPlaylistSongs(String playlistName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] columns = { DatabaseHelper.SONG_PATH };
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, DatabaseHelper.PLAYLIST_NAME+"='"+playlistName+"'", null, null, null, null);
        ArrayList<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SONG_PATH)));
        }
        return result;
    }

    public ArrayList<String> getAllPlaylists() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] columns = { DatabaseHelper.PLAYLIST_NAME };
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        ArrayList<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PLAYLIST_NAME)));
        }
        return result;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "playlist_database";
        private static final String TABLE_NAME = "PLAYLISTS";
        private static final int DATABASE_VERSION = 3;
        private static final String ID = "_id";
        private static final String SONG_PATH = "Song_Name";
        private static final String PLAYLIST_NAME = "Playlist_Name";
        private static final String CREATE_TABLE = "CREATE  TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SONG_PATH+" VARCHAR(255), "+PLAYLIST_NAME+" VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Message.message(context, "onCreate");
                db.execSQL(CREATE_TABLE);
            }
            catch (Exception e) {
                Message.message(context, e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "onUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }
            catch (Exception e) {
                Message.message(context, e.getMessage());
            }
        }
    }
}
