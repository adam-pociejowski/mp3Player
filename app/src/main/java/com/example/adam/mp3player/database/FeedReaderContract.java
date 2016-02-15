package com.example.adam.mp3player.database;

import android.provider.BaseColumns;

public class FeedReaderContract {
    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "playlist";
        public static final String PLAYLIST_ID = "playlist_id";
        public static final String PLAYLIST_NAME = "playlist_name";
        public static final String SONG_PATH = "song_path";
    }
}
