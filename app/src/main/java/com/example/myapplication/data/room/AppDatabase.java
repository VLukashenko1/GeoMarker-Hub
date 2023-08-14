package com.example.myapplication.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Point.class, LatestSavedLocation.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PointsDAO pointsDAO();
    public abstract LatestSavedLocationDAO latestSavedLocationDAO();
}
