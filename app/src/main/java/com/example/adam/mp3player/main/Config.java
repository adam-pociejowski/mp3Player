package com.example.adam.mp3player.main;

import android.content.Context;
import android.util.Log;

import com.example.adam.mp3player.database.DatabaseAdapter;
import com.example.adam.mp3player.playlist.SinglePlaylist;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-09.
 */
public class Config {
    private static volatile Config instance = null;
    private static final String MUSIC_INTERNAL_PATH = "/storage/emulated/0/Music";
    private static SinglePlaylist temporaryPlaylist;
    private static ArrayList<Song> songsList;
    private ArrayList<SinglePlaylist> playlists = new ArrayList<>();

    private Config() {}

    public void setTemporaryPlaylist(SinglePlaylist temporaryPlaylist) { this.temporaryPlaylist = temporaryPlaylist; }

    public SinglePlaylist getTemporaryPlaylist() { return temporaryPlaylist; }

    public void addPlaylist(SinglePlaylist playlist) { playlists.add(playlist); }

    public void setSongsList(ArrayList<Song> songsList) { this.songsList = songsList; }

    public static String getMusicInternalPath() { return MUSIC_INTERNAL_PATH; }

    public static ArrayList<Song> getSongsList() { return songsList; }

    public ArrayList<SinglePlaylist> getPlaylists(Context context) {
        if (playlists.size() == 0) {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
            ArrayList<String> playlistNames = databaseAdapter.getAllPlaylists();
            int actualPlaylistCounter = 0;
            for (String playlistName : playlistNames) {
                playlists.add(new SinglePlaylist());
                playlists.get(actualPlaylistCounter).setPlaylistName(playlistName);
                ArrayList<String> songsPath = databaseAdapter.getAllPlaylistSongs(playlistName);
                for (String path : songsPath) {
                    for (Song song : songsList) {
                        if (song.getAbsolutePath().equals(path)) {
                            playlists.get(actualPlaylistCounter).addSong(song);
                            break;
                        }
                    }
                }
                actualPlaylistCounter++;
            }
        }
        return playlists;
    }

    public static synchronized Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
}
