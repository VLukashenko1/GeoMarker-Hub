package com.example.myapplication.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.data.room.Point;
import com.example.myapplication.ui.EditNoteDialogFragment;
import com.example.myapplication.data.distance.PointListManager;
import com.example.myapplication.ui.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MapsFragment extends Fragment {
    MapsViewModel vm;
    private GoogleMap mMap;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static LatLng defaultCameraPosition = new LatLng(50.450606, 30.524798);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(MapsViewModel.class);

        if (vm.getCurrentLocation() != null) defaultCameraPosition = vm.getCurrentLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void observePoints() {
        PointListManager.getInstance().getPointsLiveData().observe(this, point -> {
            if (point != null) {
                Observable.just(vm.getMarkersFromPoints(point))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::putPointToMap);
            }
        });
    }

    private void putPointToMap(List<MarkerOptions> markerOptions) {
        if (mMap == null) return;
        mMap.clear();
        for (MarkerOptions marker : markerOptions) {
            mMap.addMarker(marker);
        }
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            observePoints();
            // Zoom level (2.0 = world, 15.0 = street level) -> 9
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultCameraPosition, 10.0f));

            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnMapClickListener(v -> System.out.println("CLICK CLICK"));
            mMap.setOnMarkerClickListener(marker -> {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15.0f));
                marker.showInfoWindow();
                return false;
            });
            mMap.setOnInfoWindowClickListener(window -> {
                EditNoteDialogFragment dialogFragment = new EditNoteDialogFragment(
                        new Point(window.getTitle(),
                                window.getPosition().latitude,
                                window.getPosition().longitude,
                                window.getSnippet(),""), false);
                dialogFragment.show(requireFragmentManager(), "CustomDialogFragment");
            });

        }
    };
}