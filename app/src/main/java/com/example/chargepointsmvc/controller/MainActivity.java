package com.example.chargepointsmvc.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.DBHelper;
import com.example.chargepointsmvc.model.UserModel;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton, registerButton;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("LOGIN");
        }

        // Load the database and CSV data
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.loadCSVToDatabase(); // Load data from the CSV file

        // Initialize UI components
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.registerButton);

        // Initialize Controller
        loginController = new LoginController(this);

        // Set up Register button click listener
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up Login button click listener
        loginButton.setOnClickListener(v -> {
            // Create a UserModel object with the entered data
            UserModel user = new UserModel(
                    usernameInput.getText().toString(),
                    passwordInput.getText().toString(),
                    null // Role is not required here
            );

            // Validate the user and get the role
            String role = loginController.validateUser(user);

            if (role == null) {
                // Invalid login
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else if (role.equals("admin")) {
                // Navigate to AdminHomeActivity
                Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            } else if (role.equals("user")) {
                // Navigate to UserHomeActivity
                Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}