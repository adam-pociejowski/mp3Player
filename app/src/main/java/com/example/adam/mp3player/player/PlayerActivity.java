package com.example.adam.mp3player.player;

import com.example.adam.mp3player.main.ListFragment;
import com.example.adam.mp3player.main.PlayerFragment;

public interface PlayerActivity {
    void notifyHeadsetPlugStateChanged();
    void setListFragment(ListFragment fragment);
    void setPlayerFragment(PlayerFragment fragment);
    PlayerFragment getPlayerFragment();
}
