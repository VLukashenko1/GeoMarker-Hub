package com.example.myapplication.data.distance;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.App;
import com.example.myapplication.data.helpers.SettingManager;
import com.example.myapplication.data.room.LatestSavedLocation;
import com.example.myapplication.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PointListManager {

    private static PointListManager pointListManager;
    private final LifecycleOwner lifecycleOwner;
    public static boolean isDistanceToPointExist = false;
    public static LatLng latestSavedLocation = new LatLng(0,0);
    private MutableLiveData<List<PointWithDistance>> pointsLiveData = new MutableLiveData<>();
    private MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();

    public PointListManager(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        pointListManager = this;
        getLatestSavedLocation();
        subscribe();
    }
    public MutableLiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }
    public MutableLiveData<List<PointWithDistance>> getPointsLiveData() {
        return pointsLiveData;
    }
    private void getLatestSavedLocation(){
        Runnable runnable = () -> {
            LatestSavedLocation lsv = App.getInstance().getLatestSavedLocationDAO().getLatestSavedLocation();
            if (lsv == null) return;
            latestSavedLocation = new LatLng(lsv.getLat(), lsv.getLng());
        };
        new Thread(runnable).start();
    }
    public void sort(int sortType) {
        pointsLiveData.postValue(new PointListHelper().sort(sortType, pointsLiveData.getValue()));
    }
    public String getStringDestinationByPoint(Point point) {
        if (pointsLiveData == null || pointsLiveData.getValue() == null) return "";

        for (PointWithDistance pointWithDistance : pointsLiveData.getValue()) {
            if (point.lat == pointWithDistance.getPoint().lat && point.lng == pointWithDistance.getPoint().lng) {
                return pointWithDistance.getDestinationString();
            }
        }
        return "";
    }

    public static PointListManager getInstance() {
        return pointListManager;
    }

    private void subscribe() {
        App.getInstance().getPointsDAO().getAllLiveData().observe(lifecycleOwner, points -> {
            if (points != null) {
                calculateDataForHolder(points);
            }
        });
        currentLocation.observe(lifecycleOwner, location -> {
            if (location != null && location.longitude != 0) {
                Runnable runnable = () -> {
                    List<Point> points = App.getInstance().getPointsDAO().getAll();
                    if (points != null) {
                        calculateDataForHolder(points);
                    }
                };
                new Thread(runnable);
            }
        });
        SettingManager.pointShowDistanceLiveData().observe(lifecycleOwner, value -> {
            if (value != null) {
                Runnable runnable = () -> {
                    List<Point> points = App.getInstance().getPointsDAO().getAll();
                    if (points != null) {
                        calculateDataForHolder(points);
                    }
                };
                new Thread(runnable).start();
            }
        });
    }

    private void calculateDataForHolder(List<Point> roomPointList) {
        PointListHelper pointListHelper = new PointListHelper();
        LatLng location = currentLocation.getValue();
        List<PointWithDistance> resultList;

        if (location != null && location.longitude != 0 && location.longitude != 0) {
            resultList = pointListHelper.getPointWithDestination(roomPointList, location);
            isDistanceToPointExist = true;
        } else if (latestSavedLocation != null && latestSavedLocation.latitude != 0) {
            resultList = pointListHelper.getPointWithDestination(roomPointList, latestSavedLocation);
            isDistanceToPointExist = true;
        } else {
            resultList = pointListHelper.getPointWithoutDestination(roomPointList);
            isDistanceToPointExist = false;
        }

        if (isDistanceToPointExist) {
            pointsLiveData.postValue(DistanceCalculator.getPointsCloserThenLimit(resultList));
        } else pointsLiveData.postValue(resultList);
    }

}
