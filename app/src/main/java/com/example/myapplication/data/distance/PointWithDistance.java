package com.example.myapplication.data.distance;

import com.example.myapplication.data.room.Point;

public class PointWithDistance {
    private Point point;
    private String destinationString;

    private float destinationFloat;

    public float getDestinationFloat() {
        return destinationFloat;
    }

    public void setDestinationFloat(float destinationFloat) {
        this.destinationFloat = destinationFloat;
    }

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }

    public String getDestinationString() {
        return destinationString;
    }
    public void setDestinationString(String destinationString) {
        this.destinationString = destinationString;
    }

    public PointWithDistance(Point point, String destinationString, Float destinationFloat) {
        this.point = point;
        this.destinationString = destinationString;
        this.destinationFloat = destinationFloat;
    }
}
