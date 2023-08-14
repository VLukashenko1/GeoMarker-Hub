package com.example.myapplication.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LatestSavedLocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LatestSavedLocation latestSavedLocation);

    @Query("SELECT * FROM locationSave WHERE id =1")
    LatestSavedLocation getLatestSavedLocation();

}
