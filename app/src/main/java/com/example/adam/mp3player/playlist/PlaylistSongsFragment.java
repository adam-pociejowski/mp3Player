package com.example.adam.mp3player.playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.adam.mp3player.main.MainActivity;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerActivity;
import com.example.adam.mp3player.player.PlayerCommunicator;
import com.example.adam.mp3player.playlist.add_playlist.AddPlaylistActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistSongsFragment extends Fragment implements PlayerCommunicator {
    @Bind(R.id.list_fragment_header) TextView textView;
    @Bind(R.id.show_popup_menu) Button popupMenuButton;
    @Bind(R.id.files_list_view) ListView listView;
    private Player player = Player.getInstance();
    private MyListViewAdapter myListAdapter;
    private PlayerActivity playerActivity;
    private PlaylistActivity activity;
    private int selected;
    private Playlist playlist;


    public void setActivity(PlaylistActivity activity, PlayerActivity playerActivity) {
        this.activity = activity;
        this.playerActivity = playerActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_frg_list, container, false);
        ButterKnife.bind(this, view);
        selected = -1;
        player.reset();
        player.setReference(this);
        player.registerReceiver(activity, playerActivity);
        playlist = activity.getPlaylist();
        textView.setText(playlist.getPlaylistName()+" - "+playlist.getPlaylistsSize()+" songs");
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(activity, popupMenuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent i = null;
                        if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_all_songs)))
                            i = new Intent(activity, MainActivity.class);
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_my_playlists)))
                            i = new Intent(activity, PlaylistListActivity.class);
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_add_playlist)))
                            i = new Intent(activity, AddPlaylistActivity.class);
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_settings))) {}
                        activity.startActivity(i);
                        return true;
                    }
                });
                popup.show();
            }
        });

        myListAdapter = new MyListViewAdapter(activity);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (selected != position) {
                        player.stop();
                        player.playSong(playlist.getSongs().get(position));
                        activity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(position), position);
                        selected = position;
                    } else if (selected == position && !player.isPaused()) {
                        activity.getPlayerFragment().stopSong();
                        player.stop();
                        selected = -1;
                    } else player.resume();
                    myListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - ListFragmentListViewCreator.java");
                }
            }
        });
        return view;
    }

    @Override
    public void nextSong() {
       // Log.d("next", "nextSong");
        if (++selected >= playlist.getSongs().size()) selected = playlist.getSongs().size() - 1;
        player.playSong(playlist.getSongs().get(selected));
        myListAdapter.notifyDataSetChanged();
        activity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(selected), selected);
    }

    @Override
    public void previousSong() {
        if (--selected < 0) selected = 0;
        player.playSong(playlist.getSongs().get(selected));
        myListAdapter.notifyDataSetChanged();
        activity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(selected), selected);
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

            if (position == selected && player.isPlaying()) textView.setBackgroundColor(context.getResources().getColor(R.color.activeListItem));
            return customView;
        }


        @Override
        public int getCount() { return playlist.getSongs().size(); }

        @Override
        public Object getItem(int position) { return playlist.getSongs().get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }
}
