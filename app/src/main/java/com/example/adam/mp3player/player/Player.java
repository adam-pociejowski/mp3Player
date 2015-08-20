package com.example.adam.mp3player.player;

import android.media.MediaPlayer;
import android.util.Log;
import com.example.adam.mp3player.main.Song;

/**
 * Created by Adam on 2015-08-11.
 */
public class Player {
    private MediaPlayer mediaPlayer;
    private static volatile Player instance = null;
    private PlayerCommunicator reference = null;

    private Player() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                reference.notifyFromPlayer(true);
            }
        });
    }

    public void playSong(Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
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

    public void setReference(PlayerCommunicator reference) { this.reference = reference; }

    public static synchronized Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }
}
