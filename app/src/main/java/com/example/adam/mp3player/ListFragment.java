package com.example.adam.mp3player;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-09.
 */
public class ListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    public void stateChanged(String data) {
        if (data.equals("files")) {
            ArrayList<Song> songsList = Config.getInstance().getSongsList();
            if (songsList == null) {
                songsList = new ArrayList<Song>();
                File directory = new File(Config.getInstance().getMusicInternalPath());
                try {
                    File files[] = directory.listFiles();
                    for (File file : files) songsList.add(new Song(file.getName(), file.getAbsolutePath()));

                    Config.getInstance().setSongsList(songsList);
                }
                catch (Exception e) {
                    Log.e("error", "Error while reading files from "+Config.getInstance().getMusicInternalPath()+" - ListFragment.java");
                }
            }
        }
        else if (data.equals("playlist")) {

        }
    }
}
