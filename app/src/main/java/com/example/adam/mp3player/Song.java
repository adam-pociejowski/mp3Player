package com.example.adam.mp3player;

/**
 * Created by Adam on 2015-08-09.
 */
public class Song {
    private String absolutePath;
    private String name;

    public Song(String name, String absolutePath) {
        this.name = name;
        this.absolutePath = absolutePath;
    }
}
