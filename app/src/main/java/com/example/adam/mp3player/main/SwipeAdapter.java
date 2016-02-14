package com.example.adam.mp3player.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adam.mp3player.main.list_fragment.ListFragment;
import com.example.adam.mp3player.main.player_fragment.PlayerFragment;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new ListFragment();
        else if (position == 1) return new PlayerFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
