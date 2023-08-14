package com.example.myapplication.ui;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.App;
import com.example.myapplication.data.room.LatestSavedLocation;
import com.google.android.gms.maps.model.LatLng;

public class MainActivityViewModel extends ViewModel {
    void insertLatestSavedLocation(LatLng latLng) {
        Runnable runnable = () -> {
            App.getInstance().getLatestSavedLocationDAO().insert(new LatestSavedLocation(1, latLng.latitude, latLng.longitude));
        };
        new Thread(runnable).start();
    }

}
