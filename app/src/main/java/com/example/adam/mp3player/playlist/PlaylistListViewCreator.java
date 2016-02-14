package com.example.adam.mp3player.playlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.FragmentCommunicator;

import java.util.ArrayList;

/**
 * Created by Adam on 2015-08-13.
 */
public class PlaylistListViewCreator {
    private ArrayList<SinglePlaylist> playlists;
    private ListView listView = null;
    private MyPlaylistListViewAdapter myListAdapter = null;

    public PlaylistListViewCreator(final Activity activity, final FragmentCommunicator fragmentCommunicator) {
        final Context context = activity.getApplicationContext();
        playlists = Config.getInstance().getPlaylists(activity.getApplicationContext());

        if (playlists.size() > 0) {
            myListAdapter = new MyPlaylistListViewAdapter(context);
            listView = (ListView) activity.findViewById(R.id.playlist_listView);
            listView.setAdapter(myListAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    fragmentCommunicator.setChoosenPlaylist(playlists.get(position));
                    fragmentCommunicator.fragmentCallback(R.id.playlist_listView);
                }
                catch (Exception e) {}
            }
        });
    }


    class MyPlaylistListViewAdapter extends BaseAdapter {
        private Context context;

        public MyPlaylistListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View customView  = layoutInflater.inflate(R.layout.playlist_list_view_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.playlist_list_text);
            SinglePlaylist playlist = playlists.get(position);
            textView.setText(playlist.getPlaylistName());

            return customView;
        }

        @Override
        public int getCount() { return playlists.size(); }

        @Override
        public Object getItem(int position) { return playlists.get(position); }

        @Override
        public long getItemId(int position) { return position; }
    }
}
