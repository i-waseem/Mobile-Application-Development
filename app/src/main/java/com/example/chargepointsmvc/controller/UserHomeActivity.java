package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;

public class UserHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
        }

        // Back Button Functionality
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize buttons
        Button searchChargePointsButton = findViewById(R.id.searchChargePointsButton);
        Button viewChargePointsButton = findViewById(R.id.viewChargePointsButton);
        Button viewMapButton = findViewById(R.id.viewMapButton);
        Button importDatasetButton = findViewById(R.id.importDatasetButton);

        // Navigate to SearchChargePointsActivity
        searchChargePointsButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, SearchChargePointsActivity.class);
            startActivity(intent);
        });

        // Navigate to ViewChargePointsActivity
        viewChargePointsButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, ViewChargePointsActivity.class);
            startActivity(intent);
        });

        // Navigate to MapActivity
        viewMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, MapActivity.class);
            startActivity(intent);
        });

        importDatasetButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, ImportDatasetActivity.class);
            startActivity(intent);

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            // Logout logic
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
        // Back to Login
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Close current activity
    }
}
