package com.example.adam.mp3player.player;

import android.media.MediaPlayer;
import android.util.Log;
import com.example.adam.mp3player.model.Song;

public class Player extends MediaPlayer {
    private static volatile Player instance = null;
    private PlayerCommunicator reference = null;

    private Player() {
        super();
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (getDuration() - getCurrentPosition() < 1000) {
                    stop();
                    reference.nextSong();
                }
            }
        });
    }

    public void playSong(Song song) {
        try {
            reset();
            setDataSource(song.getAbsolutePath());
            prepare();
            start();
            Log.d("Start", "Start Playing "+song.getTitle());
        }
        catch (Exception e) {
            Log.e("Playing song error", e.getMessage()+" - Player.java");
        }
    }

    public void resume() { start(); }

    public void setReference(PlayerCommunicator reference) { this.reference = reference; }

    public PlayerCommunicator getReference() { return reference; }

    public static synchronized Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }
}
