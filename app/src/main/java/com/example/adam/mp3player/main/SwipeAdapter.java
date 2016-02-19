package com.example.adam.mp3player.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adam.mp3player.player.PlayerActivity;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;
    private PlayerActivity playerActivity;

    public SwipeAdapter(FragmentManager fm, MainActivity activity, PlayerActivity playerActivity) {
        super(fm);
        this.activity = activity;
        this.playerActivity = playerActivity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ListFragment fragment = new ListFragment();
            fragment.setActivity(activity, playerActivity);
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
