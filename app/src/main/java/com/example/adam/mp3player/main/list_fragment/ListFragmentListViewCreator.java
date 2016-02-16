package com.example.adam.mp3player.main.list_fragment;

import android.content.Context;
import android.content.Intent;
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
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerCommunicator;
import com.example.adam.mp3player.playlist.PlaylistListActivity;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListFragmentListViewCreator implements PlayerCommunicator {
    @Bind(R.id.files_list_view) ListView listView;
    @Bind(R.id.show_popup_menu) Button button;
    private ArrayList<Song> songsList;
    private static int selected;
    private MyListViewAdapter myListAdapter;
    private Player player = Player.getInstance();
    private MainActivity activity;

    public ListFragmentListViewCreator(final ArrayList<Song> songsList, final MainActivity activity, final View view) {
        this.songsList = songsList;
        this.activity = activity;
        ButterKnife.bind(this, view);
        final Context context = activity.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(activity, button);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_all_songs))) {}
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_my_playlists))) {
                            Intent i = new Intent(activity, PlaylistListActivity.class);
                            activity.startActivity(i);
                        }
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_add_playlist))) {}
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_settings))) {}
                        return true;
                    }
                });
                popup.show();
            }
        });

        myListAdapter = new MyListViewAdapter(context);
        listView.setAdapter(myListAdapter);
        player.setReference(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    player.stop();
                    player.playSong(songsList.get(position));
                    if (selected != position) {
                        activity.getPlayerFragment().setPlayingSong(songsList.get(position), position);
                        selected = position;
                    }
                    else {
                        activity.getPlayerFragment().stopSong();
                        player.stop();
                        selected = -1;
                    }
                    myListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - ListFragmentListViewCreator.java");
                }
            }
        });
    }


    @Override
    public synchronized void nextSong() {
        if (++selected >= songsList.size()) selected = songsList.size() - 1;
        player.playSong(songsList.get(selected));
        myListAdapter.notifyDataSetChanged();
        activity.getPlayerFragment().setPlayingSong(songsList.get(selected), selected);
    }


    @Override
    public synchronized void previousSong() {
        if (--selected < 0) selected = 0;
        player.playSong(songsList.get(selected));
        myListAdapter.notifyDataSetChanged();
        activity.getPlayerFragment().setPlayingSong(songsList.get(selected), selected);
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
            Song song = songsList.get(position);
            textView.setText((position + 1)+". "+song.getTitle());

            if (position == selected && player.isPlaying()) textView.setBackgroundColor(context.getResources().getColor(R.color.activeListItem));
            return customView;
        }


        @Override
        public int getCount() { return songsList.size(); }

        @Override
        public Object getItem(int position) { return songsList.get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }
}
