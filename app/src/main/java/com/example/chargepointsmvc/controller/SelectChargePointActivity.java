package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class SelectChargePointActivity extends AppCompatActivity {
    private ListView chargePointsListView;
    private ArrayList<String[]> chargePointDetails;
    private DBHelper dbHelper;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_charge_point);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select a Charge Point");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve action from intent
        action = getIntent().getStringExtra("action");

        // Initialize components
        chargePointsListView = findViewById(R.id.chargePointsListView);
        chargePointDetails = new ArrayList<>();
        dbHelper = new DBHelper(this);

        // Fetch charge points from the database
        chargePointDetails = fetchChargePointsFromDatabase();

        if (chargePointDetails.isEmpty()) {
            Toast.makeText(this, "No charge points available", Toast.LENGTH_SHORT).show();
        }

        // Set custom adapter to the ListView
        AdminChargePointAdapter adapter = new AdminChargePointAdapter(chargePointDetails);
        chargePointsListView.setAdapter(adapter);
    }

    private ArrayList<String[]> fetchChargePointsFromDatabase() {
        ArrayList<String[]> chargePointDetails = new ArrayList<>();
        Cursor cursor = dbHelper.getAllChargePoints();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String referenceId = cursor.getString(cursor.getColumnIndex("referenceID"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                String town = cursor.getString(cursor.getColumnIndex("town"));
                String county = cursor.getString(cursor.getColumnIndex("county"));
                String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
                String status = cursor.getString(cursor.getColumnIndex("chargeDeviceStatus"));

                chargePointDetails.add(new String[]{referenceId, latitude, longitude, town, county, postcode, status});
            }
            cursor.close();
        }
        return chargePointDetails;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AdminChargePointAdapter extends ArrayAdapter<String[]> {
        public AdminChargePointAdapter(ArrayList<String[]> chargePointDetails) {
            super(SelectChargePointActivity.this, action.equals("edit") ? R.layout.list_item_edit_charge_point : R.layout.list_item_admin_charge_point, chargePointDetails);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(action.equals("edit") ? R.layout.list_item_edit_charge_point : R.layout.list_item_admin_charge_point, parent, false);
            }

            TextView referenceId = convertView.findViewById(R.id.referenceId);
            TextView town = convertView.findViewById(R.id.town);

            String[] chargePoint = getItem(position);
            if (chargePoint != null) {
                referenceId.setText("Ref: " + chargePoint[0]);
                town.setText("Town: " + chargePoint[3]);

                if ("edit".equals(action)) {
                    convertView.setOnClickListener(v -> {
                        Intent intent = new Intent(SelectChargePointActivity.this, EditChargePointActivity.class);
                        intent.putExtra("referenceId", chargePoint[0]);
                        intent.putExtra("latitude", chargePoint[1]);
                        intent.putExtra("longitude", chargePoint[2]);
                        intent.putExtra("town", chargePoint[3]);
                        intent.putExtra("county", chargePoint[4]);
                        intent.putExtra("postcode", chargePoint[5]);
                        intent.putExtra("status", chargePoint[6]);
                        startActivity(intent);
                    });
                } else if ("delete".equals(action)) {
                    Button deleteButton = convertView.findViewById(R.id.deleteButton);
                    deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(position, chargePoint));
                }
            }

            return convertView;
        }

        private void showDeleteConfirmationDialog(int position, String[] chargePoint) {
            // Custom AlertDialog using dialog layout
            View dialogView = LayoutInflater.from(SelectChargePointActivity.this)
                    .inflate(R.layout.dialog_confirm_deletion, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(SelectChargePointActivity.this, R.style.CustomDialogTheme)
                    .setView(dialogView)
                    .setCancelable(false);

            AlertDialog dialog = builder.create();

            // Set dialog elements
            TextView message = dialogView.findViewById(R.id.dialogMessage);
            Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
            Button confirmButton = dialogView.findViewById(R.id.dialogConfirmButton);

            message.setText("Are you sure you want to delete this charge point?");

            cancelButton.setOnClickListener(v -> dialog.dismiss());

            confirmButton.setOnClickListener(v -> {
                boolean deleted = dbHelper.deleteChargePoint(chargePoint[0]);
                if (deleted) {
                    chargePointDetails.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(SelectChargePointActivity.this, "Charge point deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectChargePointActivity.this, "Failed to delete charge point", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });

            dialog.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the charge points list
        chargePointDetails.clear();
        chargePointDetails.addAll(fetchChargePointsFromDatabase());
        ((ArrayAdapter<?>) chargePointsListView.getAdapter()).notifyDataSetChanged();
    }

}