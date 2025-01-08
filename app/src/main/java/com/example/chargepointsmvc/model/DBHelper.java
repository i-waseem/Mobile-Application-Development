package com.example.chargepointsmvc.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.chargepointsmvc.R;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChargePoints.db";
    private static final int DB_VERSION = 3; // Updated version for favorites feature

    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "Creating database...");
        db.execSQL("CREATE TABLE IF NOT EXISTS charge_points (" +
                "referenceID TEXT PRIMARY KEY, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "town TEXT, " +
                "county TEXT, " +
                "postcode TEXT, " +
                "chargeDeviceStatus TEXT, " +
                "connectorID TEXT, " +
                "connectorType TEXT, " +
                "isFavorite INTEGER DEFAULT 0)");

        // Add the missing users table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)");

        // Dummy data for testing
        db.execSQL("INSERT INTO charge_points (referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType, isFavorite) " +
                "VALUES ('CP001', 53.123, -1.234, 'Town A', 'County A', 'AB12 3CD', 'In Service', 'C1', 'Type 2', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE charge_points ADD COLUMN isFavorite INTEGER DEFAULT 0");
        }

        // Add logic to create users table if upgrading
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)");

    }

    public void updateFavoriteStatus(String referenceId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        int favorite = isFavorite ? 1 : 0;
        db.execSQL("UPDATE charge_points SET isFavorite = ? WHERE referenceID = ?",
                new Object[]{favorite, referenceId});
    }

    public Cursor getFavoriteChargePoints() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType FROM charge_points WHERE isFavorite = 1", null);
    }

    public Cursor getAllChargePoints() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType FROM charge_points", null);
    }

    public Cursor searchChargePoints(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType FROM charge_points WHERE town LIKE ? OR referenceID LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        return db.rawQuery(sql, selectionArgs);
    }
    public boolean deleteChargePoint(String referenceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("charge_points", "referenceID = ?", new String[]{referenceId});
        return rowsAffected > 0; // Return true if rows were deleted
    }

    public void loadCSVToDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM charge_points");

        try {
            InputStream is = context.getResources().openRawResource(R.raw.chargepoints);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 9) {
                    String referenceID = columns[0].trim();
                    double latitude = Double.parseDouble(columns[1].trim());
                    double longitude = Double.parseDouble(columns[2].trim());
                    String town = columns[3].trim();
                    String county = columns[4].trim();
                    String postcode = columns[5].trim();
                    String chargeDeviceStatus = columns[6].trim();
                    String connectorID = columns[7].trim();
                    String connectorType = columns[8].trim();

                    db.execSQL("INSERT INTO charge_points (referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType, isFavorite) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)",
                            new Object[]{referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType});
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("DBHelper", "Error loading CSV", e);
        }
    }
}