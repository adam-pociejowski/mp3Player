package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.adam.mp3player.R;
import com.example.adam.mp3player.database.DatabaseConnector;
import com.example.adam.mp3player.main.MainActivity;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import java.util.ArrayList;

public class PlaylistActivity extends Activity {
    private ArrayList<Song> songsList;
    private MyListViewAdapter myListAdapter;
    private ListView listView;
    private DatabaseConnector db;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Button popupMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        popupMenuButton = (Button)findViewById(R.id.show_popup_menu);
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(PlaylistActivity.this, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("All Songs")) {
                            Log.d("d", "Clicked");
                            Intent i = new Intent(PlaylistActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else if (menuItem.getTitle().equals("My Playlists")) {}
                        else if (menuItem.getTitle().equals("Add Playlist")) {}
                        else if (menuItem.getTitle().equals("Settings")) {}
                        return true;
                    }
                });
                popup.show();
            }
        });
        db = DatabaseConnector.getInstance(getApplicationContext());

        songsList = Config.getSongsList();
        ArrayList<Song> playlistSongs = new ArrayList<>();
        for (int i = 2; i < 5; i++) playlistSongs.add(songsList.get(i));

        Playlist playlist = new Playlist(playlistSongs, 2, "Fajna");
      //  db.addToDatabase(playlist);
        playlists = db.getFromDatabase();

        myListAdapter = new MyListViewAdapter(this.getApplicationContext());
        listView = (ListView)findViewById(R.id.list_view_of_playlists);
        listView.setAdapter(myListAdapter);
    }


    class MyListViewAdapter extends BaseAdapter {
        private Context context;

        public MyListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.playlist_list_view_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.playlist_list_text);
            Playlist playlist = playlists.get(position);
            textView.setText((position + 1) + ". " + playlist.getPlaylistName());
            return customView;
        }


        @Override
        public int getCount() { return playlists.size(); }

        @Override
        public Object getItem(int position) { return playlists.get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }
}
