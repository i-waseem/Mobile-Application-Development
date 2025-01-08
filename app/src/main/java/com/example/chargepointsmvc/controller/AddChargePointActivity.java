package com.example.chargepointsmvc.controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

public class AddChargePointActivity extends AppCompatActivity {

    // Declaring variables for UI components and database helper

    private EditText etReferenceID, etLatitude, etLongitude, etTown, etCounty, etPostcode, etStatus;
    private Button btnSubmit;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge_point);

        // Setting up toolbar on top of every page. Same code for all pages
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enables back button
            getSupportActionBar().setTitle("Charge Point Details");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        // Initializing input fields for charge point details

        etReferenceID = findViewById(R.id.et_referenceID);
        etLatitude = findViewById(R.id.et_latitude);
        etLongitude = findViewById(R.id.et_longitude);
        etTown = findViewById(R.id.et_town);
        etCounty = findViewById(R.id.et_county);
        etPostcode = findViewById(R.id.et_postcode);
        etStatus = findViewById(R.id.et_status);
        btnSubmit = findViewById(R.id.btn_submit);


        // Setting up the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add a Charge Point");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Optional: show back button
        }
        // Initializing DBHelper
        dbHelper = new DBHelper(this);

        // Setting up Submit button click listener
        btnSubmit.setOnClickListener(v -> {
            // Getting input data from the fields
            String referenceID = etReferenceID.getText().toString();
            String latitude = etLatitude.getText().toString();
            String longitude = etLongitude.getText().toString();
            String town = etTown.getText().toString();
            String county = etCounty.getText().toString();
            String postcode = etPostcode.getText().toString();
            String status = etStatus.getText().toString();

            if (referenceID.isEmpty() || latitude.isEmpty() || longitude.isEmpty() || town.isEmpty() ||
                    county.isEmpty() || postcode.isEmpty() || status.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {

                // Saving the charge point details into the database

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("referenceID", referenceID);
                values.put("latitude", Double.parseDouble(latitude));
                values.put("longitude", Double.parseDouble(longitude));
                values.put("town", town);
                values.put("county", county);
                values.put("postcode", postcode);
                values.put("chargeDeviceStatus", status);

                db.insert("charge_points", null, values);
                Toast.makeText(this, "Charge Point Added Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            }
        });
    }

    // This is the logout functionality. Same code for all pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Inflate the menu
        return true;
    }
}