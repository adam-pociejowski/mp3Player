package com.example.adam.mp3player.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String absolutePath;
    private String title;
    private Bitmap image;
    private String timeAsString;





    public Song(String title, String absolutePath, Bitmap image, String timeAsString) {
        this.title = title;
        this.absolutePath = absolutePath;
        this.image = image;
        this.timeAsString = timeAsString;
    }

    public Song(String title, String absolutePath) {
        this.title = title;
        this.absolutePath = absolutePath;
    }

    public String getTimeAsString() { return timeAsString; }

    public String getTitle() { return title; }

    public String getAbsolutePath() { return absolutePath; }

    public Bitmap getImage() { return image; }

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
