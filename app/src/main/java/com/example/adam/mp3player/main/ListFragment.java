package com.example.adam.mp3player.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerActivity;
import com.example.adam.mp3player.player.PlayerCommunicator;
import com.example.adam.mp3player.playlist.PlaylistListActivity;
import com.example.adam.mp3player.playlist.add_playlist.AddPlaylistActivity;
import java.io.File;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements PlayerCommunicator {
    @Bind(R.id.files_list_view) ListView listView;
    @Bind(R.id.show_popup_menu) Button button;
    @Bind(R.id.list_fragment_header) TextView textView;
    private Player player = Player.getInstance();
    private MyListViewAdapter myListAdapter;
    private PlayerActivity playerActivity;
    private ArrayList<Song> songsList;
    private MainActivity activity;
    private int selected = -1;
    private View view;

    public void setActivity(MainActivity activity, PlayerActivity playerActivity) {
        this.activity = activity;
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_activity_frg_list, container, false);
        ButterKnife.bind(this, view);
        createList();
        selected = -1;
        player.reset();
        player.registerReceiver(activity, playerActivity);
        textView.setText("Main Playlist | "+Config.getSongsAmount()+" songs");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(activity, button);
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

        myListAdapter = new MyListViewAdapter(activity.getApplicationContext());
        listView.setAdapter(myListAdapter);
        player.setReference(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (selected != position) {
                        player.stop();
                        player.playSong(songsList.get(position));
                        activity.getPlayerFragment().setPlayingSong(songsList.get(position), position);
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


    public void createList() {
        songsList = Config.getInstance().getSongsList();
        if (songsList == null) {
            songsList = new ArrayList<>();
            File directory = new File(Config.getInstance().getMusicInternalPath());
            try {
                File files[] = directory.listFiles();
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                for (File file : files) {
                    mmr.setDataSource(file.getAbsolutePath());
                    byte[] data = mmr.getEmbeddedPicture();
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath(), bitmap));
                    }
                    else songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath(), null));
                }
                songsList = getSortedSongs(songsList);
                Config.getInstance().setSongsList(songsList);
            } catch (Exception e) {
                Log.e("error", "Error while reading files from " + Config.getInstance().getMusicInternalPath() + " - FilesScannerFragment.java");
            }
        }
    }


    private ArrayList<Song> getSortedSongs(ArrayList<Song> songs) {
        Song s;
        int i, j;

        for (j = 0; j < songs.size(); j++) {
            s = songs.get(j);
            for (i = j - 1; (i >= 0) && (songs.get(i).getTitle().compareTo(s.getTitle()) > 0); i--) {
                songs.set(i + 1, songs.get(i));
            }
            songs.set(i + 1, s);
        }
        return songs;
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
