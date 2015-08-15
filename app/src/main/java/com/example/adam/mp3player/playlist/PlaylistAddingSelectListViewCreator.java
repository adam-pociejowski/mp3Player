package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.Song;
import com.example.adam.mp3player.player.Player;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-13.
 */
public class PlaylistAddingSelectListViewCreator {
    private ArrayList<Song> songsList;
    private final ListView listView;
    private ArrayList<Integer> selectedSongsIndexes = new ArrayList<>();
    private final MyPlaylistListViewAdapter myListAdapter;

    public PlaylistAddingSelectListViewCreator(final ArrayList<Song> songsList, final Activity activity) {
        this.songsList = songsList;
        final Context context = activity.getApplicationContext();

        myListAdapter = new MyPlaylistListViewAdapter(context);
        listView = (ListView) activity.findViewById(R.id.playlist_select_list);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!selectedSongsIndexes.contains(position)) selectedSongsIndexes.add(position);
                    else selectedSongsIndexes.remove((Object)position);
                    myListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - FileScannerListViewCreator.java");
                }
            }
        });
    }

    public SinglePlaylist getSelectedSongsAsPlaylist() {
        ArrayList<Song> selectedSongs = new ArrayList<>();
        for (int i : selectedSongsIndexes) selectedSongs.add(songsList.get(i));

        SinglePlaylist playlist = new SinglePlaylist();
        playlist.setSongs(selectedSongs);
        return playlist;
    }


    class MyPlaylistListViewAdapter extends BaseAdapter {
        private Context context;

        public MyPlaylistListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.playlist_adding_listview_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.playlist_adding_list_text);
            Song song = songsList.get(position);
            textView.setText(song.getTitle());

            if (selectedSongsIndexes.contains(position)) textView.setBackgroundColor(context.getResources().getColor(R.color.activeListItem));
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
