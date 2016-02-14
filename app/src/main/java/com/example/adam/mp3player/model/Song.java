package com.example.adam.mp3player.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adam on 2015-08-09.
 */
public class Song implements Parcelable {
    private String absolutePath;
    private String title;

    public Song(String title, String absolutePath) {
        this.title = title;
        this.absolutePath = absolutePath;
    }

    public String getTitle() { return title; }

    public String getAbsolutePath() { return absolutePath; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(absolutePath);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    private Song(Parcel in) {
        title = in.readString();
        absolutePath = in.readString();
    }
}
