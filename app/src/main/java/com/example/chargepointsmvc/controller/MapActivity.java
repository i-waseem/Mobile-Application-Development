package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Map View"); // Toolbar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
        }

        dbHelper = new DBHelper(this); // Initialize DBHelper

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

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
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    // Create the menu for the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Inflate menu with logout option
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Back button in the toolbar
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) { // Logout menu item
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
