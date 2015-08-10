package com.example.adam.mp3player.main;

/**
 * Created by Adam on 2015-08-09.
 */
public class Song {
    private String absolutePath;
    private String title;

    public Song(String title, String absolutePath) {
        this.title = title;
        this.absolutePath = absolutePath;
    }

    public String getTitle() { return title; }

    public String getAbsolutePath() { return absolutePath; }
}
