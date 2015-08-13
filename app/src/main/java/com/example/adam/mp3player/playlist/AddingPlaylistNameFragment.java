package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.Config;
import com.example.adam.mp3player.main.FragmentCommunicator;

/**
 * Created by Adam on 2015-08-13.
 */
public class AddingPlaylistNameFragment extends Fragment {
    private FragmentCommunicator fragmentCommunicator;
    private SinglePlaylist playlist = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_add_playlist, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCommunicator = (FragmentCommunicator)activity;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Config.getInstance().getPlaylistReady()) playlist = Config.getInstance().getNewPlaylist();
        final EditText playlistName = (EditText)getActivity().findViewById(R.id.playlist_editName);

        Button addButton = (Button)getActivity().findViewById(R.id.playlist_adding_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = playlistName.getText().toString();

                if (name.length() > 3 && playlist != null) {
                    playlist.setPlaylistName(name);
                    fragmentCommunicator.fragmentCallback(R.id.playlist_adding_button);
                }
                else Toast.makeText(getActivity().getApplicationContext(), "Playlist name should have more than 3 letter", Toast.LENGTH_SHORT);
            }
        });
    }
}