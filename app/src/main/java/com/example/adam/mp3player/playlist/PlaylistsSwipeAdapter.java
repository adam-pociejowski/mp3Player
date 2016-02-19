package com.example.adam.mp3player.playlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adam.mp3player.main.PlayerFragment;
import com.example.adam.mp3player.player.PlayerActivity;

public class PlaylistsSwipeAdapter extends FragmentStatePagerAdapter {
    private PlaylistActivity activity;
    private PlayerActivity playerActivity;

    public PlaylistsSwipeAdapter(FragmentManager fm, PlaylistActivity activity, PlayerActivity playerActivity) {
        super(fm);
        this.activity = activity;
        this.playerActivity = playerActivity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            PlaylistSongsFragment fragment = new PlaylistSongsFragment();
            fragment.setActivity(activity, playerActivity);
            return fragment;
        }
        else if (position == 1) {
            PlayerFragment fragment = new PlayerFragment();
            activity.setPlayerFragment(fragment);
            return fragment;
        }
        return null;
    }

    @Override
    public int getCount() { return 2; }
}
