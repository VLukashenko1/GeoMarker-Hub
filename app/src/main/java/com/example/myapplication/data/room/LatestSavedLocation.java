package com.example.myapplication.data.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locationSave")
public class LatestSavedLocation implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lng")
    private double lng;

    public LatestSavedLocation(int id, double lat, double lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    protected LatestSavedLocation(Parcel in) {
        id = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<LatestSavedLocation> CREATOR = new Creator<LatestSavedLocation>() {
        @Override
        public LatestSavedLocation createFromParcel(Parcel in) {
            return new LatestSavedLocation(in);
        }

        @Override
        public LatestSavedLocation[] newArray(int size) {
            return new LatestSavedLocation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
