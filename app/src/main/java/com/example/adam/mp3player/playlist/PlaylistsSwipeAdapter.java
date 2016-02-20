package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.adam.mp3player.main.ListFragment;
import com.example.adam.mp3player.main.PlayerFragment;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.player.PlayerActivity;

public class PlaylistsSwipeAdapter extends FragmentStatePagerAdapter {
    private Activity activity;
    private PlayerActivity playerActivity;
    private Playlist playlist;

    public PlaylistsSwipeAdapter(FragmentManager fm, Activity activity, PlayerActivity playerActivity,
                                 Playlist playlist) {
        super(fm);
        this.activity = activity;
        this.playerActivity = playerActivity;
        this.playlist = playlist;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           // PlaylistSongsFragment fragment = new PlaylistSongsFragment();
           // fragment.setActivity(activity, playerActivity);
            ListFragment fragment = new ListFragment();
            fragment.setActivity(activity, playerActivity);
            fragment.setPlaylist(playlist);
            return fragment;
        }
        else if (position == 1) {
            PlayerFragment fragment = new PlayerFragment();
            playerActivity.setPlayerFragment(fragment);
            return fragment;
        }
        return null;
    }

    @Override
    public int getCount() { return 2; }
}
