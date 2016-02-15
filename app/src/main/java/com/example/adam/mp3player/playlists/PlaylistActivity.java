package com.example.adam.mp3player.playlists;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Song;
import java.util.ArrayList;

public class PlaylistActivity extends Activity {
    private ArrayList<Song> songsList;
    private MyListViewAdapter myListAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        songsList = Config.getSongsList();
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
            Song song = songsList.get(position);
            textView.setText((position + 1)+". "+song.getTitle());
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
