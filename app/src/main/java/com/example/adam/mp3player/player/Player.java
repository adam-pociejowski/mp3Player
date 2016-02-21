package com.example.adam.mp3player.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.util.Log;
import com.example.adam.mp3player.model.Song;

public class Player extends MediaPlayer {
    private static volatile Player instance = null;
    private PlayerCommunicator reference = null;
    private Boolean active = false;
    private MusicHeadsetReciever receiver;
    private Boolean paused = false;

    private Player() {
        super();
        receiver = new MusicHeadsetReciever();
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

    public void registerReceiver(Activity activity, PlayerActivity playerActivity) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        activity.registerReceiver(receiver, filter);
        receiver.setPlayerActivity(playerActivity);
    }

    public void playSong(Song song) {
        try {
            reset();
            setDataSource(song.getAbsolutePath());
            prepare();
            active = true;
            start();
        }
        catch (Exception e) {
            Log.e("Playing song error", e.getMessage()+" - Player.java");
        }
    }

    public void seekForward(int millis) {
        int position = getCurrentPosition() + millis;
        if (position < getDuration()) seekTo(position);
    }

    public void seekBehind(int millis) {
        int position = getCurrentPosition() - millis;
        if (position > 0) seekTo(position);
    }


    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        active = false;
    }

    @Override
    public void pause() {
        super.pause();
        paused = true;
    }

    public void resume() {
        start();
        paused = false;
    }

    public void reset() {
        stop();
        paused = false;
        super.reset();
    }

    public MusicHeadsetReciever gerReciever() { return receiver; }

    public Boolean isActive() { return active; }

    public Boolean isPaused() { return paused; }

    public void setReference(PlayerCommunicator reference) { this.reference = reference; }

    public PlayerCommunicator getReference() { return reference; }

    public static synchronized Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }

    private class MusicHeadsetReciever extends BroadcastReceiver {
        private PlayerActivity activity;
        private String state = "";

        public void setPlayerActivity(PlayerActivity activity) { this.activity = activity; }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (!state.equals("")) activity.notifyHeadsetPlugStateChanged();
                state = Intent.ACTION_HEADSET_PLUG;
            }
        }
    }
}
