package com.example.epictureapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;

public class AppPreferences {
    public static final String APP_PREFERENCES_FILE_NAME = "userdata";
    public static final String TOKEN = "test";

    private SharedPreferences preferences;

    public AppPreferences(Context context) {
        this.preferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }
}
