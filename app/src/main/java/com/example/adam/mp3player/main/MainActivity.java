package com.example.adam.mp3player.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.PlayerActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements PlayerActivity {
    @Bind(R.id.main_activity_view_pager) ViewPager viewPager;
    ListFragment listFragment;
    PlayerFragment playerFragment;
    ArrayList<Song> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        createList();
        Playlist playlist = new Playlist(songsList, 0, "All Songs");
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), this, this, playlist);
        viewPager.setAdapter(swipeAdapter);
    }

    public void createList() {
        songsList = Config.getInstance().getSongsList();
        if (songsList == null) {
            songsList = new ArrayList<>();
            File directory = new File(Config.getInstance().getMusicInternalPath());
            try {
                File files[] = directory.listFiles();
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                for (File file : files) {
                    mmr.setDataSource(file.getAbsolutePath());
                    byte[] data = mmr.getEmbeddedPicture();
                    int time = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4),
                                file.getAbsolutePath(), bitmap, getTimeAsString(time)));
                    }
                    else songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4),
                            file.getAbsolutePath(), null, getTimeAsString(time)));
                }
                songsList = getSortedSongs(songsList);
                Config.getInstance().setSongsList(songsList);
            } catch (Exception e) {
                Log.e("error", "Error while reading files from " + Config.getInstance().getMusicInternalPath() + " - FilesScannerFragment.java");
            }
        }
    }


    private String getTimeAsString(int time) {
        String timeAsString = "";
        timeAsString += (time / 60000)+"";
        int seconds = (time % 60000)/1000;
        if (seconds < 10)  timeAsString += ":0"+seconds;
        else timeAsString += ":"+(time % 60000)/1000;
        return timeAsString;
    }


    private ArrayList<Song> getSortedSongs(ArrayList<Song> songs) {
        Song s;
        int i, j;

        for (j = 0; j < songs.size(); j++) {
            s = songs.get(j);
            for (i = j - 1; (i >= 0) && (songs.get(i).getTitle().compareTo(s.getTitle()) > 0); i--) {
                songs.set(i + 1, songs.get(i));
            }
            songs.set(i + 1, s);
        }
        return songs;
    }


    @Override
    public void notifyHeadsetPlugStateChanged() { playerFragment.pause(); }

    @Override
    public void setPlayerFragment(PlayerFragment playerFragment) { this.playerFragment = playerFragment; }

    @Override
    public void setListFragment(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    public PlayerFragment getPlayerFragment() { return playerFragment; }
}
