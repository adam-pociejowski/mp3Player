package com.example.adam.mp3player.playlist;

import com.example.adam.mp3player.main.Song;
import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-13.
 */
public class SinglePlaylist {
    private ArrayList<Song> songs = new ArrayList<>();
    private String playlistName = "";

    public void setSongs(ArrayList<Song> songs) { this.songs = songs; }

    public void setPlaylistName(String playlistName) { this.playlistName = playlistName; }

    public ArrayList<Song> getSongs() { return songs; }

    public String getPlaylistName() { return playlistName; }

    public void addSong(Song song) { songs.add(song); }
}
