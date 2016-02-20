package com.example.adam.mp3player.playlist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.ListFragment;
import com.example.adam.mp3player.main.PlayerFragment;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.player.PlayerActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistActivity extends FragmentActivity implements PlayerActivity {
    @Bind(R.id.playlist_activity_view_pager) ViewPager viewPager;
    private PlayerFragment playerFragment;
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_activity);
        ButterKnife.bind(this);
        playlist = Config.getPlaylists(getApplicationContext()).get(getIntent().getExtras().getInt("playlistIndex"));
        PlaylistsSwipeAdapter swipeAdapter = new PlaylistsSwipeAdapter(getSupportFragmentManager(), this, this, playlist);
        viewPager.setAdapter(swipeAdapter);
    }

    @Override
    public void setPlayerFragment(PlayerFragment playerFragment) { this.playerFragment = playerFragment; }

    @Override
    public PlayerFragment getPlayerFragment() { return playerFragment; }

    @Override
    public void notifyHeadsetPlugStateChanged() { playerFragment.pause(); }

    @Override
    public void setListFragment(ListFragment fragment) {}
}