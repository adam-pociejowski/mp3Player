package com.example.adam.mp3player.files_scanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adam.mp3player.R;
import com.example.adam.mp3player.main.Song;
import com.example.adam.mp3player.mp3_player.Player;

import java.util.ArrayList;

import static android.graphics.Color.YELLOW;

/**
 * Created by Adam on 2015-08-10.
 */
public class ListViewCreator {
    private ArrayList<Song> songsList;
    private int selected = -1;

    public ListViewCreator(final ArrayList<Song> songsList, Activity activity) {
        this.songsList = songsList;
        final Context context = activity.getApplicationContext();

        final MyListViewAdapter myListAdapter = new MyListViewAdapter(context);
        final ListView listView = (ListView) activity.findViewById(R.id.files_list_view);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.d("List iteam clicked", "position: " + position + " | title: " + songsList.get(position).getTitle() +
                            " | songs amount: " + songsList.size() + " | previous: ");

                    Player.getInstance().playSong(songsList.get(position));
                    selected = position;
                    myListAdapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    Log.e("List item clicked error", e.getStackTrace()+" - ListViewCreator.java");
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
            View customView = layoutInflater.inflate(R.layout.list_fragment_element, parent, false);

            TextView textView = (TextView)customView.findViewById(R.id.list_fragment_textView);

            Song song = songsList.get(position);
            textView.setText(song.getTitle().substring(0, song.getTitle().length() - 4));
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
