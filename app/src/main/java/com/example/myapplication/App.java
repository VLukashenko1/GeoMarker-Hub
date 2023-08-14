package com.example.myapplication;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.example.myapplication.data.prefManager.DataInitiator;
import com.example.myapplication.data.prefManager.FirstLaunchManger;
import com.example.myapplication.data.room.AppDatabase;
import com.example.myapplication.data.room.LatestSavedLocationDAO;
import com.example.myapplication.data.room.PointsDAO;

public class App extends Application {
    private static App instance;
    public static App getInstance() {
        return instance;
    }

    private FirstLaunchManger firstLaunchManger;
    private AppDatabase database;
    private PointsDAO pointsDAO;
    private LatestSavedLocationDAO latestSavedLocationDAO;
    public PointsDAO getPointsDAO() {
        return pointsDAO;
    }

    public LatestSavedLocationDAO getLatestSavedLocationDAO() {
        return latestSavedLocationDAO;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        changeTheme(AppCompatDelegate.MODE_NIGHT_NO);

        initRoom();
        initRetrofit();

        firstLaunchManger = new FirstLaunchManger(getApplicationContext());
        if (firstLaunchManger.isFirstLaunch()) {
            firstLaunchActions();
        }

    }

    void firstLaunchActions() {
        DataInitiator dataInitiator = new DataInitiator();
        dataInitiator.putInitialPointsToRoom();

        //todo  create First launch activity trip  ( marker types / app info )

        firstLaunchManger.setFirstLaunch(false);
    }

    void initRoom() {
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "points")
                .build();
        pointsDAO = database.pointsDAO();
        latestSavedLocationDAO = database.latestSavedLocationDAO();

    }

    public void changeTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    void initRetrofit() {
        // TODO: 10.08.2023
    }
}
