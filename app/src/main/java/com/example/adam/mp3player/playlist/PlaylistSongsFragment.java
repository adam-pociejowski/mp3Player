package com.example.adam.mp3player.playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.adam.mp3player.main.MainActivity;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistSongsFragment extends Fragment {
    @Bind(R.id.list_fragment_header) TextView textView;
    @Bind(R.id.show_popup_menu) Button popupMenuButton;
    @Bind(R.id.files_list_view) ListView listView;
    private MyListViewAdapter myListAdapter;
    private PlaylistActivity activity;
    private Playlist playlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_frg_list, container, false);
        ButterKnife.bind(this, view);
        playlist = activity.getPlaylist();
        textView.setText("Playlist - " + playlist.getPlaylistName()+" | amount: "+playlist.getPlaylistsSize());
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(activity, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_all_songs))) {
                            Intent i = new Intent(activity, MainActivity.class);
                            activity.startActivity(i);
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_my_playlists))) {
                            Intent i = new Intent(activity, PlaylistListActivity.class);
                            activity.startActivity(i);
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_add_playlist))) {
                        } else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_settings))) {
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        myListAdapter = new MyListViewAdapter(activity);
        listView.setAdapter(myListAdapter);
        return view;
    }


    class MyListViewAdapter extends BaseAdapter {
        private Context context;

        public MyListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.main_activity_list_fragment_listview_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.list_fragment_textView);
            Song song = playlist.getSongs().get(position);
            textView.setText((position + 1)+". "+song.getTitle());
            return customView;
        }


        @Override
        public int getCount() { return playlist.getSongs().size(); }

        @Override
        public Object getItem(int position) { return playlist.getSongs().get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }


    public void setActivity(PlaylistActivity activity) { this.activity = activity; }
}
