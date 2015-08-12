package com.example.adam.mp3player.mp3_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.files_scanner.ListViewCreator;
import com.example.adam.mp3player.main.Song;

/**
 * Created by Adam on 2015-08-12.
 */
public class PlayerActivity extends Activity {
    private TextView textView;
    private SeekBar seekBar;
    private Boolean running = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("s", "OnCreate");

        setContentView(R.layout.player_activity);
        Intent intent = getIntent();
        Song song = (Song)intent.getParcelableExtra("song");

        textView = (TextView)findViewById(R.id.player_song_text);
        seekBar = (SeekBar)findViewById(R.id.player_seekBar);

        textView.setText(song.getTitle());
        seekBar.setMax(Player.getInstance().getMax());
        startRefreshingThread();
    }

    private void startRefreshingThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        setPosition(Player.getInstance().getCurrentPosition());
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    public void setPosition(final int position) {
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(position);
            }
        });
    }
}
