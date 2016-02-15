package com.example.adam.mp3player.model;

import java.util.ArrayList;

public class Playlist {
    private int playlistId;
    private String playlistName;
    private ArrayList<Song> songs;

    public Playlist(ArrayList<Song> songs, int playlistId, String playlistName) {
        this.songs = songs;
        this.playlistName = playlistName;
        this.playlistId = playlistId;
    }

    public Playlist(int playlistId) {
        this.playlistId = playlistId;
        songs = new ArrayList<>();
    }

    public Playlist(int playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        songs = new ArrayList<>();
    }

    public int getPlaylistsSize() {
        return songs.size();
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}
