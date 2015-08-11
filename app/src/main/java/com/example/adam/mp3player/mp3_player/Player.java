package com.example.adam.mp3player.mp3_player;

import android.media.MediaPlayer;
import android.util.Log;
import com.example.adam.mp3player.main.Song;

/**
 * Created by Adam on 2015-08-11.
 */
public class Player {
    private MediaPlayer mediaPlayer;
    private static volatile Player instance = null;

    private Player() {
        mediaPlayer = new MediaPlayer();
    }

    public void playSong(Song song) {
        try {
            Log.e("Playis", song.getTitle()+" "+song.getAbsolutePath());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e) {
            Log.e("Playing song error", e.getMessage()+" - Player.java");
        }
    }

    public static synchronized Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }
}
