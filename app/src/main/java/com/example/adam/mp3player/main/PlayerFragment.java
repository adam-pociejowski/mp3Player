package com.example.adam.mp3player.main;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerFragment extends Fragment {
    @Bind(R.id.player_fragment_header) TextView textView;
    @Bind(R.id.album_image) ImageView image;
    @Bind(R.id.player_previousButton) Button previousButton;
    @Bind(R.id.player_nextButton) Button nextButton;
    @Bind(R.id.player_forwardButton) Button forwardButton;
    @Bind(R.id.player_behindButton) Button behindButton;
    @Bind(R.id.player_pauseButton) Button pauseButton;
    @Bind(R.id.player_seekBar) SeekBar seekBar;
    private View view;
    private Boolean playing;
    private Thread thread = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_activity_frg_player, container, false);
        ButterKnife.bind(this, view);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().stop();
                Player.getInstance().getReference().previousSong();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().stop();
                Player.getInstance().getReference().nextSong();
            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().seekForward(5000);
            }
        });
        behindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().seekBehind(5000);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Player.getInstance().isPlaying()) pause();
                else {
                    pauseButton.setBackground(getResources().getDrawable(R.drawable.pause_60p));
                    Player.getInstance().resume();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.getInstance().seekTo(seekBar.getProgress());
            }
        });
        return view;
    }


    public void pause() {
        pauseButton.setBackground(getResources().getDrawable(R.drawable.play_60p));
        Player.getInstance().pause();
    }


    public void setPlayingSong(Song song, int position) {
        if (song.getImage() != null) {
            Bitmap bitmap = song.getImage();
            double factor = (double)image.getHeight() / (double)image.getWidth();
            int leftMargin = 0, width = (int)(bitmap.getHeight()/factor);
            if (factor > 1.0) leftMargin = (bitmap.getWidth() - width) / 2;
            bitmap = Bitmap.createBitmap(bitmap, leftMargin, 0, width, bitmap.getHeight());
            bitmap = getResizedBitmap(bitmap, image.getWidth(), image.getHeight());
            image.setImageBitmap(bitmap);
            image.setAdjustViewBounds(true);
        }
        else image.setImageDrawable(view.getResources().getDrawable(R.drawable.default_album));
        pauseButton.setBackground(getResources().getDrawable(R.drawable.pause_60p));
        playing = false;
        if (thread != null) {
            try {
                thread.join();
                thread = null;
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
        textView.setText(song.getTitle());
        seekBar.setMax(Player.getInstance().getDuration());
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



    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public void stopSong() {
        pauseButton.setBackground(getResources().getDrawable(R.drawable.play_60p));
    }
}
