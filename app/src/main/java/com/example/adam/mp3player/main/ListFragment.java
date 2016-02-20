package com.example.adam.mp3player.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Playlist;
import com.example.adam.mp3player.model.Song;
import com.example.adam.mp3player.player.Player;
import com.example.adam.mp3player.player.PlayerActivity;
import com.example.adam.mp3player.player.PlayerCommunicator;
import com.example.adam.mp3player.playlist.PlaylistListActivity;
import com.example.adam.mp3player.playlist.add_playlist.AddPlaylistActivity;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements PlayerCommunicator {
    @Bind(R.id.files_list_view) ListView listView;
    @Bind(R.id.repeatButton) Button repeatButton;
    @Bind(R.id.shuffleButton) Button shuffleButton;
    @Bind(R.id.show_popup_menu) Button popupMenuButton;
    @Bind(R.id.list_fragment_header) TextView textView;
    @Bind(R.id.list_fragment_header_label) TextView label;
    private Player player = Player.getInstance();
    private ArrayList<Integer> shuffledSongs = new ArrayList<>();
    private MyListViewAdapter myListAdapter;
    private PlayerActivity playerActivity;
    private Boolean shuffle = false;
    private Boolean repeat = false;
    private Playlist playlist;
    private Activity activity;
    private int selected = -1;
    private View view;

    public void setActivity(Activity activity, PlayerActivity playerActivity) {
        this.activity = activity;
        this.playerActivity = playerActivity;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_activity_frg_list, container, false);
        ButterKnife.bind(this, view);
        selected = -1;
        player.reset();
        player.registerReceiver(activity, playerActivity);
        textView.setText(playlist.getPlaylistName());
        label.setText("0/"+playlist.getPlaylistsSize());
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(activity, popupMenuButton);
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
                        else if (menuItem.getTitle().equals(view.getResources().getString(R.string.menu_settings))) {
                        }
                        activity.startActivity(i);
                        return true;
                    }
                });
                popup.show();
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat) {
                    repeatButton.setBackground(getResources().getDrawable(R.drawable.repeat_inactive_100p));
                    repeat = false;
                }
                else {
                    repeatButton.setBackground(getResources().getDrawable(R.drawable.repeat_active_100p));
                    repeat = true;
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffle) {
                    shuffleButton.setBackground(getResources().getDrawable(R.drawable.shuffle_inactive_100p));
                    shuffle = false;
                }
                else {
                    shuffleButton.setBackground(getResources().getDrawable(R.drawable.shuffle_active_100p));
                    shuffleSongs();
                    shuffle = true;
                }
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
                        player.playSong(playlist.getSongs().get(position));
                        playerActivity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(position));
                        selected = position;
                        label.setText(selected+"/"+playlist.getPlaylistsSize());
                    } else if (selected == position && !player.isPaused()) {
                        playerActivity.getPlayerFragment().stopSong();
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


    @Override
    public synchronized void nextSong() {
        Log.d("Next Song", "shuffle "+shuffle+" | shuffled: "+shuffledSongs.size()+" | repeat: "+repeat);
        if (shuffle) {
            if (shuffledSongs.size() == 0 && repeat) shuffleSongs();
            else if (shuffledSongs.size() == 0 && !repeat) return;
            if (shuffledSongs.size() > 0) {
                selected = shuffledSongs.get(0);
                shuffledSongs.remove(0);
                player.playSong(playlist.getSongs().get(selected));
                myListAdapter.notifyDataSetChanged();
                playerActivity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(selected));
            }
        }
        else  {
            selected++;
            if (selected >= playlist.getSongs().size() && repeat) selected = 0;
            else if (selected >= playlist.getSongs().size() && !repeat) return;
            player.playSong(playlist.getSongs().get(selected));
            myListAdapter.notifyDataSetChanged();
            playerActivity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(selected));
        }
        label.setText(selected+"/"+playlist.getPlaylistsSize());
    }


    @Override
    public synchronized void previousSong() {
        if (--selected < 0) selected = 0;
        player.playSong(playlist.getSongs().get(selected));
        myListAdapter.notifyDataSetChanged();
        playerActivity.getPlayerFragment().setPlayingSong(playlist.getSongs().get(selected));
    }


    private void shuffleSongs() {
        Random random = new Random();
        shuffledSongs = new ArrayList<>();
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < playlist.getPlaylistsSize(); i++) tmp.add(i);

        for (int i = 0; i < playlist.getPlaylistsSize(); i++) {
            int generated = random.nextInt(tmp.size());
            int index = tmp.get(generated);
            tmp.remove(generated);
            shuffledSongs.add(index);
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
            View customView  = layoutInflater.inflate(R.layout.main_activity_list_fragment_listview_item, parent, false);
            TextView textView = (TextView)customView.findViewById(R.id.list_fragment_textView);
            TextView time = (TextView)customView.findViewById(R.id.list_fragment_songTime);
            LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.list_fragment_linearLayout);
            Song song = playlist.getSongs().get(position);
            textView.setText((position + 1)+". "+song.getTitle());
            time.setText(song.getTimeAsString());
            if (position == selected && player.isPlaying()) linearLayout.setBackgroundColor(context.getResources().getColor(R.color.activeListItem));
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