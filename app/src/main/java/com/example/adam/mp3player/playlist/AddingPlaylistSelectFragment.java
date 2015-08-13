package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.Config;
import com.example.adam.mp3player.main.FragmentCommunicator;
import com.example.adam.mp3player.main.Song;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-13.
 */
public class AddingPlaylistSelectFragment extends Fragment {
    private FragmentCommunicator fragmentCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_select_to_new_playlist, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ArrayList<Song> songsList = Config.getInstance().getSongsList();
        if (songsList == null) {
            songsList = new ArrayList<>();
            File directory = new File(Config.getInstance().getMusicInternalPath());
            try {
                File files[] = directory.listFiles();
                for (File file : files) songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath()));
                Config.getInstance().setSongsList(songsList);
            }
            catch (Exception e) {
                Log.e("error", "Error while reading files from " + Config.getInstance().getMusicInternalPath() + " - AddingPlaylistSelectFragment.java");
            }
        }
        final PlaylistAddingListViewCreator creator = new PlaylistAddingListViewCreator(songsList, getActivity());

        Button confirmButton = (Button)getActivity().findViewById(R.id.playlist_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.getInstance().setPlaylistReady(true);
                Config.getInstance().setNewPlaylist(creator.getSelectedSongsAsPlaylist());
                fragmentCommunicator.fragmentCallback(R.id.playlist_confirm_button);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCommunicator = (FragmentCommunicator)activity;
    }
}