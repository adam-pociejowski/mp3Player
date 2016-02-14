package com.example.adam.mp3player.player;

import android.media.MediaPlayer;
import android.util.Log;
import com.example.adam.mp3player.model.Song;

public class Player {
    private MediaPlayer mediaPlayer;
    private static volatile Player instance = null;
    private PlayerCommunicator reference = null;

    private Player() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() < 1000) {
                    mediaPlayer.stop();
                    reference.nextSong();
                }
            }
        });
    }

    public void playSong(Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("Start", "Start Playing "+song.getTitle());
        }
        catch (Exception e) {
            Log.e("Playing song error", e.getMessage()+" - Player.java");
        }
    }

    public Boolean isPlaying() { return mediaPlayer.isPlaying(); }

    public void seekTo(int position) { mediaPlayer.seekTo(position); }

    public int getMax() { return mediaPlayer.getDuration(); }

    public int getCurrentPosition() { return mediaPlayer.getCurrentPosition(); }

    public void stopSong() { mediaPlayer.stop(); }

    public void pauseSong() { mediaPlayer.pause();  }

    public void resumeSong() { mediaPlayer.start(); }

    public void setReference(PlayerCommunicator reference) { this.reference = reference; }

    public PlayerCommunicator getReference() { return reference; }

    public static synchronized Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }
}
