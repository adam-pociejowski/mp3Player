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
import android.widget.AdapterView;
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
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistListActivity extends Activity {
    @Bind(R.id.show_popup_menu) Button popupMenuButton;
    @Bind(R.id.list_view_of_playlists) ListView listView;
    private MyListViewAdapter myListAdapter;
    private DatabaseConnector db;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_list_activity);
        ButterKnife.bind(this);
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(PlaylistListActivity.this, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_all_songs))) {
                            Intent i = new Intent(PlaylistListActivity.this, MainActivity.class);
                            startActivity(i);
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_my_playlists))) {
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_add_playlist))) {
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_settings))) {
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        db = DatabaseConnector.getInstance(getApplicationContext());
        playlists = db.getFromDatabase();
        Config.setPlaylists(playlists);

        myListAdapter = new MyListViewAdapter(this.getApplicationContext());
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent i = new Intent(PlaylistListActivity.this, PlaylistActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("playlistIndex", position);
                    i.putExtras(bundle);
                    startActivity(i);
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - ListFragmentListViewCreator.java");
                }
            }
        });
    }


    class MyListViewAdapter extends BaseAdapter {
        private Context context;

        public MyListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.playlist_activity_list_view_item, parent, false);
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
