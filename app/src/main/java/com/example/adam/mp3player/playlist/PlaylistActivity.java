package com.example.adam.mp3player.playlist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.player_fragment.PlayerFragment;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistActivity extends FragmentActivity {
    @Bind(R.id.playlist_activity_view_pager) ViewPager viewPager;
    private PlayerFragment playerFragment;
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_activity);
        ButterKnife.bind(this);
        PlaylistsSwipeAdapter swipeAdapter = new PlaylistsSwipeAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(swipeAdapter);
        playlist = Config.getPlaylists(getApplicationContext()).get(getIntent().getExtras().getInt("playlistIndex"));
    }

    public void setPlayerFragment(PlayerFragment playerFragment) {
        this.playerFragment = playerFragment;
    }

    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    public Playlist getPlaylist() { return playlist; }
}
