package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class ChargePointDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_point_details);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enables back button
            getSupportActionBar().setTitle("Charge Point Details");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Log.d("Debug", "Reference ID: " + getIntent().getStringExtra("referenceId"));

        // Get data from intent
        String referenceId = getIntent().getStringExtra("referenceId");
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        String town = getIntent().getStringExtra("town");
        String county = getIntent().getStringExtra("county");
        String postcode = getIntent().getStringExtra("postcode");
        String chargeDeviceStatus = getIntent().getStringExtra("chargeDeviceStatus");
        String connectorID = getIntent().getStringExtra("connectorID");
        String connectorType = getIntent().getStringExtra("connectorType");

        // Check for null or missing data
        town = town != null ? town : "N/A";
        county = county != null ? county : "N/A";
        postcode = postcode != null ? postcode : "N/A";
        chargeDeviceStatus = chargeDeviceStatus != null ? chargeDeviceStatus : "N/A";
        connectorID = connectorID != null ? connectorID : "N/A";
        connectorType = connectorType != null ? connectorType : "N/A";

        // Validate latitude and longitude
        if (latitude < -90 || latitude > 90) {
            latitude = 0.0; // Default value for invalid latitude
        }
        if (longitude < -180 || longitude > 180) {
            longitude = 0.0; // Default value for invalid longitude
        }

        // Set data to TextViews
        ((TextView) findViewById(R.id.referenceIdText)).setText(makeTitleBold("Reference ID:", referenceId));
        ((TextView) findViewById(R.id.latitudeText)).setText(makeTitleBold("Latitude:", String.format("%.6f", latitude)));
        ((TextView) findViewById(R.id.longitudeText)).setText(makeTitleBold("Longitude:", String.format("%.6f", longitude)));
        ((TextView) findViewById(R.id.townText)).setText(makeTitleBold("Town:", town));
        ((TextView) findViewById(R.id.countyText)).setText(makeTitleBold("County:", county));
        ((TextView) findViewById(R.id.postcodeText)).setText(makeTitleBold("Postcode:", postcode));
        ((TextView) findViewById(R.id.chargeDeviceStatusText)).setText(makeTitleBold("Charge Device Status:", chargeDeviceStatus));
        ((TextView) findViewById(R.id.connectorIdText)).setText(makeTitleBold("Connector ID:", connectorID));
        ((TextView) findViewById(R.id.connectorTypeText)).setText(makeTitleBold("Connector Type:", connectorType));
    }

    // Helper method to make part of a text bold
    private SpannableStringBuilder makeTitleBold(String title, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder(title + " " + value);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Inflate the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            // Navigate back to main screen
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}