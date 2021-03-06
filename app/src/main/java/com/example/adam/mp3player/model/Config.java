package com.example.adam.mp3player.model;

import android.content.Context;

import com.example.adam.mp3player.database.DatabaseConnector;

import java.util.ArrayList;

public class Config {
    private static volatile Config instance = null;
    private static final String MUSIC_INTERNAL_PATH = "/storage/emulated/0/Music";
    private static ArrayList<Song> songsList;
    private static ArrayList<Playlist> playlists = null;
    private static DatabaseConnector db;


    public static Song getSongByPath(String path) {
        for (Song song : songsList) {
            if (song.getAbsolutePath().equals(path)) return song;
        }
        return null;
    }

    public static ArrayList<Playlist> getPlaylists(Context context) {
        if (playlists == null) {
            db = DatabaseConnector.getInstance(context);
            playlists = db.getFromDatabase();
        }
        return playlists;
    }

    public static DatabaseConnector getDb() { return db; }

    public static void setPlaylists(ArrayList<Playlist> playlists) { Config.playlists = playlists; }

    public void setSongsList(ArrayList<Song> songsList) { this.songsList = songsList; }

    public static String getMusicInternalPath() { return MUSIC_INTERNAL_PATH; }

    public static ArrayList<Song> getSongsList() { return songsList; }

    public static int getSongsAmount() { return songsList.size(); }

    public static synchronized Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    private Config() {}
}
