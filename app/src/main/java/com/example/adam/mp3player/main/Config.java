package com.example.adam.mp3player.main;

import com.example.adam.mp3player.playlist.SinglePlaylist;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-09.
 */
public class Config {
    private static volatile Config instance = null;
    private static final String MUSIC_INTERNAL_PATH = "/storage/emulated/0/Music";
    private static ArrayList<Song> songsList;
    private SinglePlaylist newPlaylist;
    private static Boolean playlistReady = false;
    private ArrayList<SinglePlaylist> playlists = new ArrayList<>();

    private Config() {}

    public ArrayList<SinglePlaylist> getPlaylists() { return playlists; }

    public void addPlaylist(SinglePlaylist playlist) { playlists.add(playlist); }

    public void setPlaylistReady(Boolean playlistReady) { this.playlistReady = playlistReady; }

    public Boolean getPlaylistReady() { return playlistReady; }

    public void setNewPlaylist(SinglePlaylist newPlaylist) { this.newPlaylist = newPlaylist; }

    public SinglePlaylist getNewPlaylist() { return newPlaylist; }

    public void setSongsList(ArrayList<Song> songsList) { this.songsList = songsList; }

    public static String getMusicInternalPath() { return MUSIC_INTERNAL_PATH; }

    public static ArrayList<Song> getSongsList() { return songsList; }

    public static synchronized Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
}
