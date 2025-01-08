package com.example.chargepointsmvc.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private RadioButton adminRadioButton, userRadioButton;
    private Button registerButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Ensure no default ActionBar conflicts
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register"); // Set correct title
        }


        // Initialize UI components
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        adminRadioButton = findViewById(R.id.adminRadioButton);
        userRadioButton = findViewById(R.id.userRadioButton);
        registerButton = findViewById(R.id.registerButton);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Handle register button click
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String role = adminRadioButton.isChecked() ? "admin" : "user";

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Check if username already exists
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                return;
            }
            cursor.close();

            // Insert user into database
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("role", role);

            long result = db.insert("users", null, values);
            db.close();

            if (result == -1) {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Back button ID
            finish(); // Close the current activity and return to the previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}