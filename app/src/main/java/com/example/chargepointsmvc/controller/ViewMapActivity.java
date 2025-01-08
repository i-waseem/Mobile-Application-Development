package com.example.chargepointsmvc.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);



        // Initialize Back Button
        Button backButton = findViewById(R.id.backButton);

        // Set click listener to finish activity
        backButton.setOnClickListener(v -> finish());
        dbHelper = new DBHelper(this);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error initializing the Map Fragment!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            Toast.makeText(this, "Error initializing map", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean hasMarkers = false;

        // Fetch charge points from the database
        Cursor cursor = dbHelper.getAllChargePoints();
        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
            String town = cursor.getString(cursor.getColumnIndex("town"));

            // Add a marker for each charge point
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(referenceId)
                    .snippet("Town: " + town));

            builder.include(location);
            hasMarkers = true;
        }
        cursor.close();

        // Adjust camera to include all markers
        if (hasMarkers) {
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Adjust padding as needed
        } else {
            // Default view if no markers
            LatLng defaultLocation = new LatLng(53.8008, -1.5491); // Example: Leeds, UK
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
            Toast.makeText(this, "No charge points available to display", Toast.LENGTH_SHORT).show();
        }
    }
}