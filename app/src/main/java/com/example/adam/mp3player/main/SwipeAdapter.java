package com.example.adam.mp3player.main;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adam.mp3player.main.list_fragment.ListFragment;
import com.example.adam.mp3player.main.player_fragment.PlayerFragment;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;

    public SwipeAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ListFragment fragment = new ListFragment();
            fragment.setActivity(activity);
            activity.setListFragment(fragment);
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
    public int getCount() {
        return 2;
    }
}
