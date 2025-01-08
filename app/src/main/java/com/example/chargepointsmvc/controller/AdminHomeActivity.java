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

public class AdminHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Buttons
        Button addChargePointButton = findViewById(R.id.addChargePointButton);
        Button editChargePointButton = findViewById(R.id.editChargePointButton);
        Button deleteChargePointButton = findViewById(R.id.deleteChargePointButton);
        Button viewChargePointsButton = findViewById(R.id.viewChargePointsButton);
        Button searchChargePointsButton = findViewById(R.id.searchChargePointsButton);
        Button viewMapButton = findViewById(R.id.viewMapButton);
        Button importDatasetButton = findViewById(R.id.importDatasetButton);

        // Set Button Click Listeners
        addChargePointButton.setOnClickListener(v -> navigateTo(AddChargePointActivity.class));
        editChargePointButton.setOnClickListener(v -> navigateTo(SelectChargePointActivity.class, "edit"));
        deleteChargePointButton.setOnClickListener(v -> navigateTo(SelectChargePointActivity.class, "delete"));
        viewChargePointsButton.setOnClickListener(v -> navigateTo(ViewChargePointsActivity.class));
        searchChargePointsButton.setOnClickListener(v -> navigateTo(SearchChargePointsActivity.class));
        viewMapButton.setOnClickListener(v -> navigateTo(MapActivity.class));
        importDatasetButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ImportDatasetActivity.class);
            startActivity(intent);
        });
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(AdminHomeActivity.this, activityClass);
        startActivity(intent);
    }

    private void navigateTo(Class<?> activityClass, String action) {
        Intent intent = new Intent(AdminHomeActivity.this, activityClass);
        intent.putExtra("action", action); // Pass the action to differentiate behavior
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Inflate the logout menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Toolbar back button
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) { // Logout option
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}