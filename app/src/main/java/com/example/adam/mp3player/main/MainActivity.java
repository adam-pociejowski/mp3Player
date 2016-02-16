package com.example.adam.mp3player.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.list_fragment.ListFragment;
import com.example.adam.mp3player.main.player_fragment.PlayerFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    @Bind(R.id.main_activity_view_pager) ViewPager viewPager;
    ListFragment listFragment;
    PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(swipeAdapter);
    }

    public void setPlayerFragment(PlayerFragment playerFragment) { this.playerFragment = playerFragment; }

    public void setListFragment(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    public PlayerFragment getPlayerFragment() { return playerFragment; }
}
