package com.example.adam.mp3player;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Adam on 2015-08-09.
 */
public class MenuFragment extends Fragment {
    private FragmentCommunicator fragmentCommunicator;
    private Button filesButton,
                   playlistButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.menu_fragment, container, false);

        filesButton = (Button)rootView.findViewById(R.id.files_button);
        playlistButton = (Button)rootView.findViewById(R.id.playlist_button);

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.respond("files");
            }
        });

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.respond("playlist");
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCommunicator = (FragmentCommunicator)activity;
    }

}
