package com.example.adam.mp3player.playlist.add_playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.database.DatabaseConnector;
import com.example.adam.mp3player.main.MainActivity;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.playlist.PlaylistListActivity;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AddPlaylistActivity extends AppCompatActivity {
    @Bind(R.id.add_playlist_accept_button) Button acceptButton;
    @Bind(R.id.add_playlist_list_view) ListView listView;
    @Bind(R.id.add_playlist_editText) EditText editText;
    @Bind(R.id.show_popup_menu) Button popupMenuButton;
    private MyListViewAdapter myListViewAdapter;
    private Boolean checkBoxesState[];
    private ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_playlist_activity);
        ButterKnife.bind(this);
        songs = Config.getSongsList();
        checkBoxesState = new Boolean[songs.size()];
        for (int i = 0; i < checkBoxesState.length; i++) checkBoxesState[i] = false;

        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(AddPlaylistActivity.this, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals(getResources().getString(R.string.menu_all_songs))) {
                            Intent i = new Intent(AddPlaylistActivity.this, MainActivity.class);
                            startActivity(i);
                        } else if (menuItem.getTitle().equals(getResources().getString(R.string.menu_my_playlists))) {
                        } else if (menuItem.getTitle().equals(getResources().getString(R.string.menu_add_playlist))) {
                        } else if (menuItem.getTitle().equals(getResources().getString(R.string.menu_settings))) {
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length() < 4) Toast.makeText(getApplicationContext(),
                        "Playlist name must have at least 4 characters", Toast.LENGTH_LONG).show();
                else {
                    ArrayList<Song> playlistSongs = new ArrayList<>();
                    int index = 0;
                    for (Song s : songs) if (checkBoxesState[index++]) playlistSongs.add(s);

                    if (playlistSongs.size() > 0) {
                        ArrayList<Playlist> playlists = Config.getPlaylists(getApplicationContext());
                        int lastId = 0;
                        if (playlists.size() > 0) {
                            lastId = playlists.get(playlists.size() - 1).getPlaylistId();
                        }
                        Playlist p = new Playlist(playlistSongs, ++lastId, editText.getText().toString());
                        playlists.add(p);
                        DatabaseConnector.getInstance().addToDatabase(p);
                        Config.setPlaylists(playlists);
                        Intent i = new Intent(AddPlaylistActivity.this, PlaylistListActivity.class);
                        startActivity(i);
                    }
                    else Toast.makeText(getApplicationContext(),
                            "Playlist must have at least 1 song", Toast.LENGTH_LONG).show();
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                return false;
            }
        });
        myListViewAdapter = new MyListViewAdapter(getApplicationContext());
        listView.setAdapter(myListViewAdapter);
    }


    class MyListViewAdapter extends BaseAdapter {
        private Context context;

        public MyListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView  = layoutInflater.inflate(R.layout.add_to_playlist_activity_list_view_item, parent, false);
            TextView textView = (TextView) customView.findViewById(R.id.add_playlist_item_textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!checkBoxesState[position]) checkBoxesState[position] = true;
                    else checkBoxesState[position] = false;
                    notifyDataSetChanged();
                }
            });
            final CheckBox checkBox = (CheckBox) customView.findViewById(R.id.add_playlist_checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!checkBoxesState[position]) checkBoxesState[position] = true;
                    else checkBoxesState[position] = false;
                }
            });
            checkBox.setChecked(checkBoxesState[position]);
            Song song = songs.get(position);
            textView.setText(song.getTitle());
            return customView;
        }


        @Override
        public int getCount() { return songs.size(); }

        @Override
        public Object getItem(int position) { return songs.get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }
}