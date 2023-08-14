package com.example.myapplication.data.distance;

import static com.example.myapplication.Const.SORT_BY_DESTINATION;
import static com.example.myapplication.Const.SORT_BY_NAME;
import static com.example.myapplication.Const.SORT_BY_NOTE_EXIST;
import static com.example.myapplication.Const.SORT_TYPE_REVERSE;

import com.example.myapplication.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class PointListHelper {

    public List<PointWithDistance> getPointWithDestination(List<Point> points, LatLng currentLocation) {
        List<String> destination = DistanceCalculator.getDistanceStringList(points, currentLocation);
        List<Float> destinationFloat = DistanceCalculator.getDistanceFloat(points, currentLocation);
        List<PointWithDistance> result = new ArrayList<>();
        for (int i = 0; i < destination.size(); i++) {
            result.add(new PointWithDistance(points.get(i), destination.get(i), destinationFloat.get(i)));
        }
        return result;
    }

    public List<PointWithDistance> getPointWithoutDestination(List<Point> points) {
        List<PointWithDistance> result = new ArrayList<>();
        for (Point point : points) {
            result.add(new PointWithDistance(point,"",0f));
        }
        return result;
    }

    public List<PointWithDistance> sort(int sortType, List<PointWithDistance> point) {

        if (sortType == SORT_BY_NAME) {
            point.sort(new PointsComparatorName());
            return point;
        } else if (sortType == SORT_BY_DESTINATION) {
            point.sort(new PointsComparatorDestination());
            return point;
        } else if (sortType == SORT_BY_NOTE_EXIST) {
            point.sort(new PointsComparatorExistNote());
            return point;
        } else if(sortType == SORT_TYPE_REVERSE){
            Collections.reverse(point);
            return point;
        } else return point;

    }


    private static class PointsComparatorDestination implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance point1, PointWithDistance point2) {
            return Float.compare(point1.getDestinationFloat(), point2.getDestinationFloat());
        }
    }
    private static class PointsComparatorName implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance pointWithDistance, PointWithDistance t1) {
            return pointWithDistance.getPoint().name.compareTo(t1.getPoint().name);
        }
    }
    private static class PointsComparatorExistNote implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance pointWithDistance, PointWithDistance t1) {
            return pointWithDistance.getPoint().note.compareTo(t1.getPoint().note);
        }
    }
}
