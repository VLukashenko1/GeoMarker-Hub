package com.example.myapplication.data.distance;

import android.location.Location;

import com.example.myapplication.data.helpers.SettingManager;
import com.example.myapplication.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

class DistanceCalculator {
    public static List<String> getDistanceStringList (List<Point> points, LatLng currentLocation){
        List<String> distance = new ArrayList<>();

        if (currentLocation != null){
            for (Point point:points){
                distance.add(getDistanceStringList(new LatLng(point.lat,point.lng), currentLocation));
            }
        }
        return distance;
    }
    public static List<Float> getDistanceFloat(List<Point> points, LatLng currentLocation){
        List<Float> distance = new ArrayList<>();

        if (currentLocation != null){
            for (Point point:points){
                distance.add(getDistanceFloat(new LatLng(point.lat,point.lng), currentLocation));
            }
        }
        return distance;
    }
    public static List<PointWithDistance> getPointsCloserThenLimit(List<PointWithDistance> points){
        if (!PointListManager.isDistanceToPointExist || SettingManager.getPointShowDistance() == 0) return points;

        List<PointWithDistance> pointCloserThenLimit = new ArrayList<>();
        int distanceForShow = SettingManager.getPointShowDistance() * 1000;
        for (PointWithDistance pointWithDistance : points){
            if (Math.round(pointWithDistance.getDestinationFloat()) < distanceForShow){
                pointCloserThenLimit.add(pointWithDistance);
            }
        }
        return pointCloserThenLimit;
    }
    private static float getDistanceFloat(LatLng point1, LatLng point2){
        Location location1 = new Location("point1");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);

        Location location2 = new Location("point2");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);

        return location1.distanceTo(location2);
    }
    private static String getDistanceStringList(LatLng point1, LatLng point2) {
        Location location1 = new Location("point1");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);

        Location location2 = new Location("point2");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);

        float distance = location1.distanceTo(location2);
        if (distance > 1000){
            distance = distance /1000;
            return Math.round(distance) + "km";
        }
        else return Math.round(distance) + "m";
    }

}
