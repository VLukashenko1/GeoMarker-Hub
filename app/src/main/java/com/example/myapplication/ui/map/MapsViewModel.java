package com.example.myapplication.ui.map;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.myapplication.data.distance.PointListManager;
import com.example.myapplication.data.distance.PointWithDistance;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsViewModel extends ViewModel {
    List<MarkerOptions> getMarkersFromPoints (List<PointWithDistance> points){

        List<MarkerOptions> markers = new ArrayList<>();
        for(PointWithDistance p : points){
            if (p.getPoint().note.isEmpty()){
                markers.add(new MarkerOptions()
                        .position(new LatLng(p.getPoint().lat, p.getPoint().lng))
                        .title(p.getPoint().name)
                        .snippet(p.getPoint().note)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_small)));
            }
            else {
                markers.add(new MarkerOptions()
                        .position(new LatLng(p.getPoint().lat, p.getPoint().lng))
                        .title(p.getPoint().name)
                        .snippet(p.getPoint().note)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_logo_three)));
            }

        }
        return markers;
    }

    LatLng getCurrentLocation(){
        if (PointListManager.getInstance().getCurrentLocation() != null && PointListManager.getInstance().getCurrentLocation().getValue().longitude != 0){
            return PointListManager.getInstance().getCurrentLocation().getValue();
        }
        else return null;
    }
}
