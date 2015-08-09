package com.example.adam.mp3player;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-09.
 */
public class Config {
    private static volatile Config instance = null;
    private static final String MUSIC_INTERNAL_PATH = "/storage/emulated/0/Music";
    private static ArrayList<Song> songsList;

    private Config() {}

    public void setSongsList(ArrayList<Song> songsList) { this.songsList = songsList; }

    public static String getMusicInternalPath() { return MUSIC_INTERNAL_PATH; }

    public static ArrayList<Song> getSongsList() { return songsList; }

    public static synchronized Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
}
