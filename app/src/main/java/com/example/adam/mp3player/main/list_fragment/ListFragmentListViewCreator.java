package com.example.adam.mp3player.main.list_fragment;

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
import com.example.adam.mp3player.main.player_fragment.PlayerFragment;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerCommunicator;
import java.util.ArrayList;

public class ListFragmentListViewCreator implements PlayerCommunicator {
    private ArrayList<Song> songsList;
    private static int selected;
    private ListView listView;
    private MyListViewAdapter myListAdapter;
    private Player player = Player.getInstance();

    public ListFragmentListViewCreator(final ArrayList<Song> songsList, final Activity activity, View view) {
        this.songsList = songsList;
        final Context context = activity.getApplicationContext();
        myListAdapter = new MyListViewAdapter(context);
        listView = (ListView)view.findViewById(R.id.files_list_view);
        listView.setAdapter(myListAdapter);
        player.setReference(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.d("Clicked", position+" "+songsList.get(position).getTitle());
                    player.stopSong();
                    player.playSong(songsList.get(position));
                    if (selected != position) {
                        PlayerFragment.setPlayingSong(songsList.get(position), position);
                        selected = position;
                    }
                    else {
                        PlayerFragment.stopSong();
                        player.stopSong();
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
        PlayerFragment.setPlayingSong(songsList.get(selected), selected);
        Log.d("Next", selected + " " + songsList.get(selected).getTitle());
    }


    @Override
    public synchronized void previousSong() {
        if (--selected < 0) selected = 0;
        player.playSong(songsList.get(selected));
        myListAdapter.notifyDataSetChanged();
        PlayerFragment.setPlayingSong(songsList.get(selected), selected);
        Log.d("Previous", selected + " " + songsList.get(selected).getTitle());
    }


    class MyListViewAdapter extends BaseAdapter {
        private Context context;

        public MyListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.list_fragment_listview_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.list_fragment_textView);
            Song song = songsList.get(position);
            textView.setText(song.getTitle());

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
