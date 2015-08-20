package com.example.adam.mp3player.database;

import android.content.Context;
import android.util.Log;

/**
 * Created by Adam on 2015-08-15.
 */
public class Message {
    public static void message(Context context, String message) { Log.d("message", message); }

    public static void info(String info) { Log.d("info", info); }

}
