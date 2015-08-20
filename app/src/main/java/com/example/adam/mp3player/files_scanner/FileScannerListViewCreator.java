package com.example.adam.mp3player.files_scanner;

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
import com.example.adam.mp3player.database.Message;
import com.example.adam.mp3player.main.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerCommunicator;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-10.
 */
public class FileScannerListViewCreator implements PlayerCommunicator {
    private ArrayList<Song> songsList;
    private static int selected;
    private ListView listView;
    private MyListViewAdapter myListAdapter;
    private Player player = Player.getInstance();

    public FileScannerListViewCreator(final ArrayList<Song> songsList, final Activity activity) {
        this.songsList = songsList;
        final Context context = activity.getApplicationContext();

        myListAdapter = new MyListViewAdapter(context);
        listView = (ListView) activity.findViewById(R.id.files_list_view);
        listView.setAdapter(myListAdapter);
        player.setReference(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    player.playSong(songsList.get(position));
                    if (selected != position) selected = position;
                    else player.stopSong();

                    myListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("List item clicked error", e.getMessage() + " - FileScannerListViewCreator.java");
                }
            }
        });
    }

    @Override
    public void notifyFromPlayer(Boolean status) {
        if (status) {
            selected++;
            player.playSong(songsList.get(selected));
            myListAdapter.notifyDataSetChanged();
        }
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
