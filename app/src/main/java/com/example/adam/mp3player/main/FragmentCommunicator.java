package com.example.adam.mp3player.main;

import com.example.adam.mp3player.playlist.SinglePlaylist;

/**
 * Created by Adam on 2015-08-13.
 */
public interface FragmentCommunicator {
    void fragmentCallback(int id);
    void setChoosenPlaylist(SinglePlaylist choosenPlaylist);
    SinglePlaylist getChoosenPlaylist();
}
