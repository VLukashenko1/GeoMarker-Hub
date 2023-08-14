package com.example.myapplication.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PointsDAO {

    @Query("SELECT * FROM Point")
    List<Point> getAll();

    @Query("SELECT * FROM Point")
    LiveData<List<Point>> getAllLiveData();

    @Insert
    void insertAll(List<Point> points);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Point point);

    @Delete
    void deletePoint(Point point);

    @Update
    void updatePoint(Point... points);
}
