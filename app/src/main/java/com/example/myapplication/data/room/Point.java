package com.example.myapplication.data.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Point implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lng")
    public double lng;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "photoLink")
    public String photoLink;

    protected Point(Parcel in) {
        uId = in.readInt();
        name = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        note = in.readString();
        photoLink = in.readString();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public Point(String name, double lat, double lng, String note, String photoLink) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.note = note;
        this.photoLink = photoLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(uId);
        parcel.writeString(name);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(note);
        parcel.writeString(photoLink);
    }
}
