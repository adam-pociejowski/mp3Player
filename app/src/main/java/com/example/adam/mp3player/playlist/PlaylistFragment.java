package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.FragmentCommunicator;

/**
 * Created by Adam on 2015-08-12.
 */
public class PlaylistFragment extends Fragment {
    private FragmentCommunicator fragmentCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_playlist_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCommunicator = (FragmentCommunicator)activity;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button addButton = (Button)getActivity().findViewById(R.id.playlist_add_playlist_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.fragmentCallback(R.id.playlist_add_playlist_button);
            }
        });

        new PlaylistListViewCreator(getActivity(), fragmentCommunicator);
    }
}
