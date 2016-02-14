package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerCommunicator;

/**
 * Created by Adam on 2015-08-15.
 */
public class PlaylistSongListViewCreator implements PlayerCommunicator {
    private SinglePlaylist playlist;
    private final ListView listView;
    private int selected = -1;
    private final MyPlaylistListViewAdapter myListAdapter;
    private Player player = Player.getInstance();

    public PlaylistSongListViewCreator(final SinglePlaylist playlist, final Activity activity) {
        this.playlist = playlist;
        final Context context = activity.getApplicationContext();

        player.setReference(this);
        myListAdapter = new MyPlaylistListViewAdapter(context);
        listView = (ListView) activity.findViewById(R.id.playlist_song_list_listView);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    player.playSong(playlist.getSongs().get(position));
                    if (selected != position) selected = position;
                    else Player.getInstance().stopSong();

                    myListAdapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - PlaylistSongListViewCreator.java");
                }
            }
        });
    }


    @Override
    public void nextSong() {
        selected++;
        player.playSong(playlist.getSongs().get(selected));
        myListAdapter.notifyDataSetChanged();
    }

    @Override
    public void previousSong() {

    }


    class MyPlaylistListViewAdapter extends BaseAdapter {
        private Context context;

        public MyPlaylistListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.playlist_song_list_list_view_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.playlist_song_list_text);
            Song song = playlist.getSongs().get(position);
            textView.setText(song.getTitle());

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
