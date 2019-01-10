package com.example.android.movieStage1.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

public class SharedPreferencesUtils {

    private static  final  String KEY_STATE = "keyState";
    private static final List<String> DEFAULT_VALUE = null;


    public static void saveListMovies(List<String> favouriteTitles , Context context){
        SharedPreferences sharedPreferencesUtils = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferencesUtils.edit();

        editor.putString(KEY_STATE, String.valueOf(favouriteTitles));
        editor.apply();
    }

    public static String getListMovies(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_STATE, String.valueOf(DEFAULT_VALUE));
    }


}
