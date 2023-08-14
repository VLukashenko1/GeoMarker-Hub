package com.example.myapplication.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.helpers.SettingManager;
import com.example.myapplication.data.distance.PointListManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    MainActivityViewModel vm;
    public NavController navController;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.myapplication.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        new PointListManager(this); // init points and their holder

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_map)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        checkAccessToLocation();

    }

    public void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            currentLocation = new LatLng(latitude, longitude);

                            vm.insertLatestSavedLocation(currentLocation);
                            PointListManager.getInstance().getCurrentLocation().postValue(currentLocation);
                        } else {
                            makeText("Location failure");
                        }
                    })
                    .addOnFailureListener(this, locationCallback -> {
                    });
        } catch (SecurityException e) {
            makeText(getString(R.string.permission_deny));
        }

    }


    void checkAccessToLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with location-related operations
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();

            } else {
                // Permission denied, handle accordingly (show a message, disable location-related features, etc.)
                makeSnackbar();
            }
        }

    }


    public void makeText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void makeSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), getString(R.string.app_need_access_to_location_for_work), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        snackbar.setAction("Okay", v -> {
            checkAccessToLocation();
            snackbar.dismiss();
        });
    }

    @Override
    protected void onDestroy() {
        //save latest location before exist

        SettingManager.saveDistanceSetting(SettingManager.getPointShowDistance());

        super.onDestroy();
    }
}