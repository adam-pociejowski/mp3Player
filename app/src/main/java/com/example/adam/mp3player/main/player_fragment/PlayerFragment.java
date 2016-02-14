package com.example.adam.mp3player.main.player_fragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;

public class PlayerFragment extends Fragment {
    private static View view;
    private static TextView textView;
    private static SeekBar seekBar;
    private static Song playingSong;
    private static Boolean playing;
    private static Button previousButton, nextButton, pauseButton;
    private static Thread thread = null;
    private static ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        textView = (TextView)view.findViewById(R.id.player_fragment_header);
        image = (ImageView)view.findViewById(R.id.album_image);
        previousButton = (Button)view.findViewById(R.id.player_previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().stopSong();
                Player.getInstance().getReference().previousSong();
            }
        });
        nextButton = (Button)view.findViewById(R.id.player_nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().stopSong();
                Player.getInstance().getReference().nextSong();
            }
        });
        pauseButton = (Button)view.findViewById(R.id.player_pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Player.getInstance().isPlaying()) {
                    pauseButton.setText(">");
                    Player.getInstance().pauseSong();
                }
                else {
                    pauseButton.setText("||");
                    Player.getInstance().resumeSong();
                }
            }
        });

        seekBar = (SeekBar)view.findViewById(R.id.player_seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.getInstance().seekTo(seekBar.getProgress());
            }
        });
        return view;
    }


    public static void setPlayingSong(Song song, int position) {
        if (song.getImage() != null) {
            Bitmap bitmap = getResizedBitmap(song.getImage(), image.getWidth(), image.getHeight());
            image.setImageBitmap(bitmap);
            image.setAdjustViewBounds(true);
        }
        else image.setImageDrawable(view.getResources().getDrawable(R.drawable.default_album));
        pauseButton.setText("||");
        playing = false;
        if (thread != null) {
            try {
                thread.join();
                thread = null;
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        playingSong = song;
        textView.setText((position + 1) + ". " + song.getTitle());
        seekBar.setMax(Player.getInstance().getMax());
        playing = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (playing) {
                    try {
                        seekBar.setProgress(Player.getInstance().getCurrentPosition());
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                }
            }
        });
        thread.start();
    }



    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public static void stopSong() {
        pauseButton.setText(">");
    }
}
