package com.example.adam.mp3player.files_scanner;

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
 * Created by Adam on 2015-08-10.
 */
public class FileScannerListViewCreator {
    private ArrayList<Song> songsList;
    private int selected = -1;
    final ListView listView;

    public FileScannerListViewCreator(final ArrayList<Song> songsList, final Activity activity) {
        this.songsList = songsList;
        final Context context = activity.getApplicationContext();

        final MyListViewAdapter myListAdapter = new MyListViewAdapter(context);
        listView = (ListView) activity.findViewById(R.id.files_list_view);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Player.getInstance().playSong(songsList.get(position));
                    if (selected != position) selected = position;
                    else Player.getInstance().stopSong();

                    myListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - FileScannerListViewCreator.java");
                }
                Intent myIntent = new Intent("com.example.adam.mp3player.mp3_player.PlayerActivity");
                myIntent.putExtra("song", songsList.get(position));
                activity.startActivity(myIntent);
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

            View customView  = layoutInflater.inflate(R.layout.main_files_scanner_fragment_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.list_fragment_textView);
            Song song = songsList.get(position);
            textView.setText(song.getTitle());

            if (position == selected) textView.setBackgroundColor(context.getResources().getColor(R.color.activeListItem));
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