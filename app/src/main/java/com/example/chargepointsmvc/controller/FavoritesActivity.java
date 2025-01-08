package com.example.chargepointsmvc.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Logout");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Handle back button

        // Initialize ListView and database
        ListView favoritesListView = findViewById(R.id.favoritesListView);
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<ChargePoint> favoriteChargePoints = new ArrayList<>();

        // Fetch favorite charge points
        Cursor cursor = dbHelper.getFavoriteChargePoints();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Fetch charge point data from cursor
                    String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
                    double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                    String town = cursor.getString(cursor.getColumnIndex("town"));
                    String county = cursor.getString(cursor.getColumnIndex("county"));
                    String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
                    String chargeDeviceStatus = cursor.getString(cursor.getColumnIndex("chargeDeviceStatus"));
                    String connectorID = cursor.getString(cursor.getColumnIndex("connectorID"));
                    String connectorType = cursor.getString(cursor.getColumnIndex("connectorType"));

                    // Create a ChargePoint object
                    ChargePoint chargePoint = new ChargePoint(
                            referenceId,
                            latitude,
                            longitude,
                            town,
                            county,
                            postcode,
                            chargeDeviceStatus,
                            connectorID,
                            connectorType
                    );
                    chargePoint.setFavorite(true); // Mark as favorite
                    favoriteChargePoints.add(chargePoint);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // Set the adapter for the ListView
        ChargePointAdapter adapter = new ChargePointAdapter(this, favoriteChargePoints);
        favoritesListView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Close the activity
    }
}