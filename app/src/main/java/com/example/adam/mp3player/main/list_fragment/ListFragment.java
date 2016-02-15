package com.example.adam.mp3player.main.list_fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.adam.mp3player.R;
import com.example.adam.mp3player.model.Config;
import com.example.adam.mp3player.model.FragmentCommunicator;
import com.example.adam.mp3player.model.Song;
import java.io.File;
import java.util.ArrayList;

public class ListFragment extends Fragment implements FragmentCommunicator {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        createList();
        TextView textView = (TextView)view.findViewById(R.id.list_fragment_header);
        textView.setText("Main Playlist | "+Config.getSongsAmount()+" songs");
        return view;
    }


    public void createList() {
        ArrayList<Song> songsList = Config.getInstance().getSongsList();
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
                        Log.d("Bitmap", "have "+file.getAbsolutePath());
                    }
                    else {
                        songsList.add(new Song(file.getName().substring(0, file.getName().length() - 4), file.getAbsolutePath(), null));
                        Log.d("Bitmap", "don't have "+file.getAbsolutePath());
                    }
                }
                songsList = getSortedSongs(songsList);
                Config.getInstance().setSongsList(songsList);
            } catch (Exception e) {
                Log.e("error", "Error while reading files from " + Config.getInstance().getMusicInternalPath() + " - FilesScannerFragment.java");
            }
        }
        new ListFragmentListViewCreator(Config.getSongsList(), getActivity(), view);
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
    public void fragmentCallback(int id) {}
}