package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;

import java.util.ArrayList;

public class AdminChargePointAdapter extends AppCompatActivity {
    public AdminChargePointAdapter(DeleteChargePointActivity deleteChargePointActivity, ArrayList<ChargePoint> chargePoints) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the back button
            getSupportActionBar().setHomeButtonEnabled(true);      // Make it clickable
        }

        // Initialize Buttons
        Button addChargePointButton = findViewById(R.id.addChargePointButton);
        Button editChargePointButton = findViewById(R.id.editChargePointButton);
        Button deleteChargePointButton = findViewById(R.id.deleteChargePointButton);
        Button viewChargePointsButton = findViewById(R.id.viewChargePointsButton);
        Button searchChargePointsButton = findViewById(R.id.searchChargePointsButton);
        Button viewMapButton = findViewById(R.id.viewMapButton);

        // Check for null references
        if (addChargePointButton == null || editChargePointButton == null || deleteChargePointButton == null ||
                viewChargePointsButton == null || searchChargePointsButton == null || viewMapButton == null) {
            Log.e("AdminHomeActivity", "One or more buttons are null. Check XML IDs.");
            return;
        }

        // Set Button Click Listeners
        addChargePointButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, AddChargePointActivity.class);
            startActivity(intent);
        });

        editChargePointButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, SelectChargePointActivity.class);
            startActivity(intent);
        });

        deleteChargePointButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, SelectChargePointActivity.class);
            startActivity(intent);
        });

        viewChargePointsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, ViewChargePointsActivity.class);
            startActivity(intent);
        });

        searchChargePointsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, ChargePoint.class);
            startActivity(intent);
        });

        viewMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminChargePointAdapter.this, MapActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Inflate the logout menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Toolbar back button
            onBackPressed(); // Use the default back logic
            return true;
        } else if (item.getItemId() == R.id.logout) { // Logout option
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Back navigation logic for Admin Home
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}