package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class ViewChargePointsActivity extends AppCompatActivity {

    private ArrayList<ChargePoint> chargePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charge_points);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enables the back button
            getSupportActionBar().setHomeButtonEnabled(true);      // Makes the button clickable
        }

        // Initialize chargePoints list
        chargePoints = new ArrayList<>();

        // Fetch data from the database
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAllChargePoints();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                String town = cursor.getString(cursor.getColumnIndex("town"));
                String county = cursor.getString(cursor.getColumnIndex("county"));
                String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
                String chargeDeviceStatus = cursor.getString(cursor.getColumnIndex("chargeDeviceStatus"));
                String connectorID = cursor.getString(cursor.getColumnIndex("connectorID"));
                String connectorType = cursor.getString(cursor.getColumnIndex("connectorType"));

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
                chargePoints.add(chargePoint);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Initialize ListView
        ListView chargePointsListView = findViewById(R.id.chargePointsListView);
        ChargePointAdapter adapter = new ChargePointAdapter(this, chargePoints);
        chargePointsListView.setAdapter(adapter);

        // Initialize Filter and Sort Buttons
        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> showSortOptions());
    }

    private void showFilterDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomDialogTheme); // Apply custom theme
        builder.setTitle("Filter Charge Points");

        final EditText input = new EditText(this);
        input.setHint("Enter Town or Reference ID");
        input.setTextColor(getResources().getColor(android.R.color.white)); // Optional: Ensure input text is visible
        input.setHintTextColor(getResources().getColor(android.R.color.darker_gray)); // Optional: Set hint text color

        builder.setView(input);

        builder.setPositiveButton("FILTER", (dialog, which) -> {
            String filterQuery = input.getText().toString().trim();
            applyFilter(filterQuery);
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void applyFilter(String query) {
        ArrayList<ChargePoint> filteredList = new ArrayList<>();
        for (ChargePoint chargePoint : chargePoints) {
            if (chargePoint.getReferenceId().contains(query) || chargePoint.getTown().contains(query)) {
                filteredList.add(chargePoint);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        } else {
            ChargePointAdapter adapter = new ChargePointAdapter(this, filteredList);
            ListView chargePointsListView = findViewById(R.id.chargePointsListView);
            chargePointsListView.setAdapter(adapter);
        }
    }

    private void showSortOptions() {
        // Define the sorting options
        String[] options = {"By Town", "By Reference ID"};

        // Create an AlertDialog for sorting options
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("Sort By");

        // Create a custom adapter to apply white text color
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(android.R.color.white)); // Set text color to white
                return view;
            }
        };

        // Set the adapter to the dialog and handle option clicks
        builder.setAdapter(adapter, (dialog, which) -> {
            if (which == 0) {
                sortByTown();
            } else if (which == 1) {
                sortByReferenceID();
            }
        });

        // Show the dialog
        builder.show();
    }





    private void sortByTown() {
        chargePoints.sort((o1, o2) -> o1.getTown().compareToIgnoreCase(o2.getTown()));

        ChargePointAdapter adapter = new ChargePointAdapter(this, chargePoints);
        ListView chargePointsListView = findViewById(R.id.chargePointsListView);
        chargePointsListView.setAdapter(adapter);
    }

    private void sortByReferenceID() {
        chargePoints.sort((o1, o2) -> o1.getReferenceId().compareToIgnoreCase(o2.getReferenceId()));

        ChargePointAdapter adapter = new ChargePointAdapter(this, chargePoints);
        ListView chargePointsListView = findViewById(R.id.chargePointsListView);
        chargePointsListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu); // Ensure this points to the correct menu file
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handles back button
            return true;
        } else if (item.getItemId() == R.id.logout) { // Handles logout menu item
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Goes back to the previous activity in the stack
        finish(); // Closes the current activity
    }

}