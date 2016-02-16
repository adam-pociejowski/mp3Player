package com.example.adam.mp3player.playlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PlaylistsSwipeAdapter extends FragmentStatePagerAdapter {
    private PlaylistActivity activity;

    public PlaylistsSwipeAdapter(FragmentManager fm, PlaylistActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            PlaylistSongsFragment fragment = new PlaylistSongsFragment();
            fragment.setActivity(activity);
            return fragment;
        }
        else if (position == 1) {
            PlaylistPlayerFragment fragment = new PlaylistPlayerFragment();
            return fragment;
        }
        return null;
    }

    @Override
    public int getCount() { return 2; }
}
