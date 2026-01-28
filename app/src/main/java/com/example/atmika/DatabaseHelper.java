package com.example.atmika;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "house_price.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_LOCATIONS = "locations";
    private static final String TABLE_PREDICTIONS = "predictions";
    private static final String TABLE_CONTACTS = "contacts";

    // Common column names
    private static final String KEY_ID = "id";

    // LOCATIONS Table - column names
    private static final String KEY_LOCATION_NAME = "name";
    private static final String KEY_RATE_PER_SQFT = "rate_per_sqft";

    // PREDICTIONS Table - column names
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SQFT = "sqft";
    private static final String KEY_BHK = "bhk";
    private static final String KEY_BATHROOMS = "bathrooms";
    private static final String KEY_BALCONIES = "balconies";
    private static final String KEY_PARKING = "parking";
    private static final String KEY_FURNISHING = "furnishing";
    private static final String KEY_AGE = "age";
    private static final String KEY_FINAL_PRICE = "final_price";
    private static final String KEY_DATE = "date";

    // CONTACTS Table - column names
    private static final String KEY_CONTACT_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MESSAGE = "message";

    // Table Create Statements
    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE "
            + TABLE_LOCATIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_LOCATION_NAME + " TEXT," + KEY_RATE_PER_SQFT + " REAL)";

    private static final String CREATE_TABLE_PREDICTIONS = "CREATE TABLE "
            + TABLE_PREDICTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_LOCATION + " TEXT," + KEY_SQFT + " REAL," + KEY_BHK + " INTEGER,"
            + KEY_BATHROOMS + " INTEGER," + KEY_BALCONIES + " INTEGER,"
            + KEY_PARKING + " INTEGER," + KEY_FURNISHING + " TEXT,"
            + KEY_AGE + " INTEGER," + KEY_FINAL_PRICE + " REAL," + KEY_DATE + " TEXT)";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CONTACT_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT," + KEY_MESSAGE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_PREDICTIONS);
        db.execSQL(CREATE_TABLE_CONTACTS);
        prepopulateLocations(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREDICTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    private void prepopulateLocations(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_NAME, "Andheri");
        values.put(KEY_RATE_PER_SQFT, 8000);
        db.insert(TABLE_LOCATIONS, null, values);

        values.put(KEY_LOCATION_NAME, "Bandra");
        values.put(KEY_RATE_PER_SQFT, 12000);
        db.insert(TABLE_LOCATIONS, null, values);

        values.put(KEY_LOCATION_NAME, "Navi Mumbai");
        values.put(KEY_RATE_PER_SQFT, 6000);
        db.insert(TABLE_LOCATIONS, null, values);
    }

    public long addPrediction(String location, double sqft, int bhk, int bathrooms, int balconies, int parking, String furnishing, int age, double finalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION, location);
        values.put(KEY_SQFT, sqft);
        values.put(KEY_BHK, bhk);
        values.put(KEY_BATHROOMS, bathrooms);
        values.put(KEY_BALCONIES, balconies);
        values.put(KEY_PARKING, parking);
        values.put(KEY_FURNISHING, furnishing);
        values.put(KEY_AGE, age);
        values.put(KEY_FINAL_PRICE, finalPrice);
        values.put(KEY_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_PREDICTIONS, null, values);
    }

    public List<Prediction> getAllPredictions() {
        List<Prediction> predictions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PREDICTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Prediction p = new Prediction();
                p.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                p.setSqft(c.getDouble(c.getColumnIndex(KEY_SQFT)));
                p.setBhk(c.getInt(c.getColumnIndex(KEY_BHK)));
                p.setFinalPrice(c.getDouble(c.getColumnIndex(KEY_FINAL_PRICE)));
                p.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                predictions.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return predictions;
    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                locations.add(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return locations;
    }

    public double getRateForLocation(String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOCATIONS, new String[]{KEY_RATE_PER_SQFT}, KEY_LOCATION_NAME + "=?",
                new String[]{location}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            double rate = cursor.getDouble(cursor.getColumnIndex(KEY_RATE_PER_SQFT));
            cursor.close();
            return rate;
        }
        return 0;
    }

    public long addContact(String name, String email, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_MESSAGE, message);
        return db.insert(TABLE_CONTACTS, null, values);
    }
}
