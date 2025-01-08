package com.example.chargepointsmvc.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chargepointsmvc.model.DBHelper;
import com.example.chargepointsmvc.model.UserModel;

public class LoginController {
    private Context context;
    private DBHelper dbHelper;

    public LoginController(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context); // Initialize the database helper
    }

    public String validateUser(UserModel user) {
        if (user.getUsername() == null || user.getPassword() == null ||
                user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            Log.d("LoginController", "Invalid input: username or password is null/empty");
            return null;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT role FROM users WHERE username = ? AND password = ?",
                new String[]{user.getUsername(), user.getPassword()}
        );

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0); // Retrieve the role
            cursor.close();
            Log.d("LoginController", "User role: " + role);
            return role;
        } else {
            cursor.close();
            Log.d("LoginController", "User not found");
            return null; // User not found
        }
    }

}