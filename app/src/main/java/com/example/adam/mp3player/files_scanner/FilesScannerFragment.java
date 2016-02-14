package com.example.adam.mp3player.files_scanner;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.Config;
import com.example.adam.mp3player.main.Song;
import java.io.File;
import java.util.ArrayList;

public class FilesScannerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_files_scanner_fragment, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        createList();
    }


    public void createList() {
        ArrayList<Song> songsList = Config.getInstance().getSongsList();
        if (songsList == null) {
            Log.d("song", "song");
            songsList = new ArrayList<>();
            File directory = new File(Config.getInstance().getMusicInternalPath());
            try {
                File files[] = directory.listFiles();
                for (File file : files) {
                    songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath()));
                }
                songsList = getSortedSongs(songsList);
                Config.getInstance().setSongsList(songsList);
            }
            catch (Exception e) {
                Log.e("error", "Error while reading files from " + Config.getInstance().getMusicInternalPath() + " - FilesScannerFragment.java");
            }
        }
        new FileScannerListViewCreator(songsList, getActivity());
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
}
