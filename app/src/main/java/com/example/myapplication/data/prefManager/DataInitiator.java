package com.example.myapplication.data.prefManager;

import com.example.myapplication.App;

public class DataInitiator {
    public void putInitialPointsToRoom() {
        Runnable task = () -> App.getInstance().getPointsDAO().insertAll(InitialPoints.getInitialPoints());
        new Thread(task).start();
    }
}
