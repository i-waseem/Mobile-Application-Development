package com.example.chargepointsmvc.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Set the dynamic title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home Page");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Optional: show back button
        }
        listView = findViewById(R.id.listView);
        dbHelper = new DBHelper(this);

        // Fetch data from the database
        Cursor cursor = dbHelper.getAllChargePoints();
        ArrayList<String> data = new ArrayList<>();

        while (cursor.moveToNext()) {
            String chargePoint = "Reference: " + cursor.getString(1) + "\n" + // referenceID
                    "Location: " + cursor.getString(3) + ", " + cursor.getString(4) + "\n" + // town, county
                    "Status: " + cursor.getString(6); // chargeDeviceStatus
            data.add(chargePoint);
        }
        cursor.close();

        // Set up the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

}