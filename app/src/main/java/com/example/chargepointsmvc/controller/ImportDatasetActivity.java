package com.example.chargepointsmvc.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportDatasetActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 1;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_dataset);

        dbHelper = new DBHelper(this);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Import Dataset");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Button setup
        Button selectFileButton = findViewById(R.id.selectFileButton);
        selectFileButton.setOnClickListener(v -> showImportDialog());
    }

    private void showImportDialog() {
        // Create a custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_import_dataset, null);
        builder.setView(dialogView);

        // Set up dialog buttons
        Button csvButton = dialogView.findViewById(R.id.dialogCsvButton);
        Button textButton = dialogView.findViewById(R.id.dialogTextButton);
        Button jsonButton = dialogView.findViewById(R.id.dialogJsonButton);

        // Show the dialog
        AlertDialog dialog = builder.create();

        // Handle button clicks
        csvButton.setOnClickListener(v -> {
            openFileChooser("text/csv");
            dialog.dismiss();
        });

        textButton.setOnClickListener(v -> {
            openFileChooser("text/plain");
            dialog.dismiss();
        });

        jsonButton.setOnClickListener(v -> {
            openFileChooser("application/json");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openFileChooser(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType); // Use the selected file type
        startActivityForResult(Intent.createChooser(intent, "Select a file"), FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                try {
                    String fileType = getContentResolver().getType(fileUri);
                    if (fileType != null) {
                        if (fileType.equals("text/csv")) {
                            importCSV(fileUri);
                        } else if (fileType.equals("text/plain")) {
                            importText(fileUri);
                        } else if (fileType.equals("application/json")) {
                            importJSON(fileUri);
                        } else {
                            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error processing file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Process CSV file
    private void importCSV(Uri fileUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(fileUri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        int successCount = 0, failureCount = 0;

        try {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) { // Adjust based on your dataset structure
                    ContentValues values = new ContentValues();
                    values.put("referenceID", data[0]);
                    values.put("latitude", Double.parseDouble(data[1]));
                    values.put("longitude", Double.parseDouble(data[2]));
                    values.put("town", data[3]);
                    values.put("county", data[4]);
                    values.put("postcode", data[5]);
                    values.put("chargeDeviceStatus", data[6]);
                    db.insert("charge_points", null, values);
                    successCount++;
                } else {
                    failureCount++;
                }
            }
            db.setTransactionSuccessful();
            Toast.makeText(this, "CSV Import Complete. Success: " + successCount + ", Failures: " + failureCount, Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
            db.close();
            reader.close();
            inputStream.close();
        }
    }

    // Process JSON file
    private void importJSON(Uri fileUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(fileUri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonData = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            jsonData.append(line);
        }

        reader.close();
        inputStream.close();

        // Parse JSON if needed, or display as is
        Toast.makeText(this, "JSON File Imported:\n" + jsonData.toString(), Toast.LENGTH_LONG).show();
    }

    // Process Text file
    private void importText(Uri fileUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(fileUri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder textData = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            textData.append(line).append("\n");
        }

        reader.close();
        inputStream.close();

        Toast.makeText(this, "Text File Imported:\n" + textData.toString(), Toast.LENGTH_LONG).show();
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
        super.onBackPressed();
        finish();
    }
}
