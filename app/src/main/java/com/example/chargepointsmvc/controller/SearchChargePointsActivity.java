package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;
import com.example.chargepointsmvc.model.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SearchChargePointsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EditText searchQueryInput;
    private ListView searchResultsListView;
    private DBHelper dbHelper;
    private GoogleMap mMap;

    private ArrayList<ChargePoint> chargePoints;
    private ChargePointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_charge_points);

        // Initialize UI components
        searchQueryInput = findViewById(R.id.searchQueryInput);
        searchResultsListView = findViewById(R.id.searchResultsListView);
        dbHelper = new DBHelper(this);

        chargePoints = new ArrayList<>();
        adapter = new ChargePointAdapter(this, chargePoints);
        searchResultsListView.setAdapter(adapter);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search Charge Points");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Map setup
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Search listener
        searchQueryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchChargePoints(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(this, "Clicked: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false; // Allow default behavior
        });

        searchChargePoints(""); // Load all charge points initially
    }

    private void searchChargePoints(String query) {
        chargePoints.clear();
        if (mMap != null) mMap.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean hasMarkers = false;

        Cursor cursor = query.isEmpty() ? dbHelper.getAllChargePoints() : dbHelper.searchChargePoints(query);

        while (cursor.moveToNext()) {
            String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String town = cursor.getString(cursor.getColumnIndex("town"));
            String county = cursor.getString(cursor.getColumnIndex("county"));
            String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
            String chargeDeviceStatus = cursor.getString(cursor.getColumnIndex("chargeDeviceStatus"));
            String connectorID = cursor.getString(cursor.getColumnIndex("connectorID"));
            String connectorType = cursor.getString(cursor.getColumnIndex("connectorType"));

            // Handle null data
            town = town != null ? town : "N/A";
            county = county != null ? county : "N/A";
            postcode = postcode != null ? postcode : "N/A";
            chargeDeviceStatus = chargeDeviceStatus != null ? chargeDeviceStatus : "N/A";
            connectorID = connectorID != null ? connectorID : "N/A";
            connectorType = connectorType != null ? connectorType : "N/A";

            ChargePoint chargePoint = new ChargePoint(referenceId, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType);
            chargePoints.add(chargePoint);

            if (mMap != null) {
                LatLng location = new LatLng(latitude, longitude);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.custom_marker);
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 150, 150, false);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                mMap.addMarker(new MarkerOptions().position(location).title(referenceId).snippet("Town: " + town).icon(icon));
                builder.include(location);
                hasMarkers = true;
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();

        if (mMap != null && hasMarkers) {
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } else if (!hasMarkers) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}