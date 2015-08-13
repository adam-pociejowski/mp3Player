package com.example.adam.mp3player.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.files_scanner.FilesScannerFragment;
import com.example.adam.mp3player.playlist.AddingPlaylistFragment;
import com.example.adam.mp3player.playlist.PlaylistFragment;

public class MainActivity extends ActionBarActivity implements FragmentCommunicator {
    private FilesScannerFragment filesScannerFragment;
    private PlaylistFragment playlistFragment;
    private Toolbar toolbar;
    private Button filesButton,
                   playlistButton,
                   previousButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        filesButton = (Button)findViewById(R.id.files_button);
        previousButton = filesButton;
        playlistButton = (Button)findViewById(R.id.playlist_button);

        filesScannerFragment = new FilesScannerFragment();
        playlistFragment = new PlaylistFragment();
        setFragment(filesScannerFragment, filesButton);

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(filesScannerFragment, filesButton);
                filesButton.setBackgroundColor(getResources().getColor(R.color.activeButton));
            }
        });

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(playlistFragment, playlistButton);
            }
        });
    }

    private void setFragment(Fragment fragment, Button button) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.my_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
        getFragmentManager().executePendingTransactions();

        previousButton.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
        button.setBackgroundColor(getResources().getColor(R.color.activeButton));
        previousButton = button;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fragmentCallback(int id) {
        switch (id) {
            case R.id.playlist_add_playlist_button: setFragment(new AddingPlaylistFragment(), playlistButton); break;
            case R.id.playlist_adding_button: setFragment(playlistFragment, playlistButton); break;
            default: Log.e("fragmentCallback", "No referenced id error");
        }
    }
}
