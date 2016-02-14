package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.FragmentCommunicator;

/**
 * Created by Adam on 2015-08-15.
 */
public class PlaylistSongListFragment extends Fragment {
    private FragmentCommunicator fragmentCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_songs_list_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCommunicator = (FragmentCommunicator)activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        new PlaylistSongListViewCreator(fragmentCommunicator.getChoosenPlaylist(), getActivity());
    }
}
