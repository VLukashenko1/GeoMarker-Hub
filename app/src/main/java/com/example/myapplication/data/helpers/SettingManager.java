package com.example.myapplication.data.helpers;

import static com.example.myapplication.Const.KEY_SETTING_DISTANCE;
import static com.example.myapplication.Const.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.App;

public class SettingManager {
    private static MutableLiveData<Integer> showDistance = new MutableLiveData<>();
    private static int theme = 1;
    public static void setTheme(int theme) {
        SettingManager.theme = theme;
        App.getInstance().changeTheme(theme);
    }

    public static void setPointShowDistance(int distance){
        showDistance.postValue(distance);
        saveDistanceSetting(distance);
    }
    public static int getPointShowDistance(){
        return showDistance.getValue();
    }
    public static MutableLiveData<Integer> pointShowDistanceLiveData(){
        return showDistance;
    }


    public static void saveDistanceSetting(int distance) {
        SharedPreferences sharedPreferences = App.getInstance().getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(KEY_SETTING_DISTANCE, distance).apply();
    }
}
