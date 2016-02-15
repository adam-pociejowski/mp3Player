package com.example.adam.mp3player.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import java.util.ArrayList;

public class DatabaseConnector {
    private DatabaseHelper databaseHelper;
    private static volatile DatabaseConnector instance = null;

    private DatabaseConnector(Context context) {
        databaseHelper = new DatabaseHelper(context);
        //databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 1, 2);
    }


    public void addToDatabase(Playlist playlist) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        for (Song song : playlist.getSongs()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FeedReaderContract.FeedEntry.PLAYLIST_ID, playlist.getPlaylistId());
            contentValues.put(FeedReaderContract.FeedEntry.PLAYLIST_NAME, playlist.getPlaylistName());
            contentValues.put(FeedReaderContract.FeedEntry.SONG_PATH, song.getAbsolutePath());

            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, contentValues);
            Log.d("Database Added", song.getTitle());
        }
    }


    public ArrayList<Playlist> getFromDatabase() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = { FeedReaderContract.FeedEntry.PLAYLIST_ID, FeedReaderContract.FeedEntry.PLAYLIST_NAME,
                FeedReaderContract.FeedEntry.SONG_PATH};
        Cursor c = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, null, null, null,
                null, FeedReaderContract.FeedEntry.PLAYLIST_ID+" ASC");
        c.moveToFirst();
        ArrayList<Playlist> playlists = new ArrayList<>();
        int actualPlaylistId = 0;
        do {
            int playlistId = c.getInt(0);
            if (actualPlaylistId != playlistId) {
                String playlistName = c.getString(1);
                playlists.add(new Playlist(playlistId, playlistName));
                actualPlaylistId = playlistId;
            }
            Song song = Config.getSongByPath(c.getString(2));
            playlists.get(playlists.size() - 1).addSong(song);
            Log.d("Read", playlistId+" "+song.getTitle());
        } while (c.moveToNext());
        return playlists;
    }





    public static synchronized DatabaseConnector getInstance(Context context) {
        if (instance == null) instance = new DatabaseConnector(context);
        return instance;
    }


    public static synchronized DatabaseConnector getInstance() {
        return instance;
    }
}
