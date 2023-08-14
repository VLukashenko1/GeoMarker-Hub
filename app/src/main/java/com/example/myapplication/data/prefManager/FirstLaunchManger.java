package com.example.myapplication.data.prefManager;

import static com.example.myapplication.Const.KEY_FIRST_LAUNCH;
import static com.example.myapplication.Const.KEY_SETTING_DISTANCE;
import static com.example.myapplication.Const.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.data.helpers.SettingManager;

public class FirstLaunchManger {
    private final SharedPreferences sharedPreferences;

    public FirstLaunchManger(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        getSavedSettings();
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch).apply();
    }
    private void getSavedSettings(){
        SettingManager.setPointShowDistance(sharedPreferences.getInt(KEY_SETTING_DISTANCE,100));
    }
}

