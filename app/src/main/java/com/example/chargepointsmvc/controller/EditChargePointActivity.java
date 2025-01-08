package com.example.chargepointsmvc.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

public class EditChargePointActivity extends AppCompatActivity {
    private EditText referenceIdInput, latitudeInput, longitudeInput, townInput, countyInput, postcodeInput, statusInput;
    private Button saveButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_charge_point);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Charge Point");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
        }

        // Initialize UI components
        referenceIdInput = findViewById(R.id.referenceIdInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        townInput = findViewById(R.id.townInput);
        countyInput = findViewById(R.id.countyInput);
        postcodeInput = findViewById(R.id.postcodeInput);
        statusInput = findViewById(R.id.statusInput);
        saveButton = findViewById(R.id.saveButton);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Get data from intent
        String referenceId = getIntent().getStringExtra("referenceId");
        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");
        String town = getIntent().getStringExtra("town");
        String county = getIntent().getStringExtra("county");
        String postcode = getIntent().getStringExtra("postcode");
        String status = getIntent().getStringExtra("status");

        // Populate fields with existing data
        referenceIdInput.setText(referenceId);
        latitudeInput.setText(latitude);
        longitudeInput.setText(longitude);
        townInput.setText(town);
        countyInput.setText(county);
        postcodeInput.setText(postcode);
        statusInput.setText(status);

        // Disable editing of the reference ID
        referenceIdInput.setEnabled(false);

        saveButton.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Debug referenceId
            if (referenceId == null || referenceId.isEmpty()) {
                Toast.makeText(this, "Reference ID is missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("latitude", latitudeInput.getText().toString());
            values.put("longitude", longitudeInput.getText().toString());
            values.put("town", townInput.getText().toString());
            values.put("county", countyInput.getText().toString());
            values.put("postcode", postcodeInput.getText().toString());
            values.put("chargeDeviceStatus", statusInput.getText().toString());

            // Debug ContentValues
            Log.d("EditChargePointActivity", "Updating with values: " + values.toString());

            int rowsUpdated = db.update("charge_points", values, "referenceID = ?", new String[]{referenceId});

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Charge point updated successfully", Toast.LENGTH_SHORT).show();
                // Force UI refresh or close activity
                finish();
            } else {
                Toast.makeText(this, "Failed to update charge point", Toast.LENGTH_SHORT).show();
                Log.d("EditChargePointActivity", "No rows updated for referenceID: " + referenceId);
            }
        });

    }

    // Create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Back button
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