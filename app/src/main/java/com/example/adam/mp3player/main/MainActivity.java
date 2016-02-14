package com.example.adam.mp3player.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.adam.mp3player.R;

public class MainActivity extends FragmentActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }

    /*
    @Override
    public void fragmentCallback(int id) {
        switch (id) {
            case R.id.playlist_add_playlist_button: setFragment(new PlaylistAddingSelectFragment(), playlistButton); break;
            case R.id.playlist_adding_button: setFragment(playlistFragment, playlistButton); break;
            case R.id.playlist_confirm_button: setFragment(new PlaylistAddingNameFragment(), playlistButton); break;
            case R.id.playlist_listView: setFragment(new PlaylistSongListFragment(), playlistButton); break;
            default: Log.e("fragmentCallback", "No referenced id error");
        }
    }

    @Override
    public void setChoosenPlaylist(SinglePlaylist choosenPlaylist) { this.choosenPlaylist = choosenPlaylist; }

    @Override
    public SinglePlaylist getChoosenPlaylist() { return choosenPlaylist; }
    */
}
