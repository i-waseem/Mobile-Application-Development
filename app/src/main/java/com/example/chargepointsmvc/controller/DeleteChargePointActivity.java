package com.example.chargepointsmvc.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class DeleteChargePointActivity extends AppCompatActivity {
    private ListView chargePointsListView;
    private ArrayList<ChargePoint> chargePoints;
    private DBHelper dbHelper;
    private AdminChargePointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_charge_point);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Delete a Charge Point");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize components
        chargePointsListView = findViewById(R.id.chargePointsListView);
        chargePoints = new ArrayList<>();
        dbHelper = new DBHelper(this);

        // Fetch charge points from the database
        Cursor cursor = dbHelper.getAllChargePoints();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                String town = cursor.getString(cursor.getColumnIndex("town"));
                String county = cursor.getString(cursor.getColumnIndex("county"));
                String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
                String status = cursor.getString(cursor.getColumnIndex("chargeDeviceStatus"));
                String connectorID = cursor.getString(cursor.getColumnIndex("connectorID"));
                String connectorType = cursor.getString(cursor.getColumnIndex("connectorType"));

                // Add to the list
                chargePoints.add(new ChargePoint(referenceId, latitude, longitude, town, county, postcode, status, connectorID, connectorType));
            }
            cursor.close();
        }

        // Use AdminChargePointAdapter for admin-specific actions
        adapter = new AdminChargePointAdapter(this, chargePoints);
        chargePointsListView.setAdapter((ListAdapter) adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle back button
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) { // Handle logout menu item
            // Navigate back to main activity
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
