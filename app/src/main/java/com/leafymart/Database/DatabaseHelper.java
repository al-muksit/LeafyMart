package com.leafymart.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LeafyMart.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PROFILE = "profile";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_IMAGE = "image"; // Storing image as BLOB (byte array)

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PROFILE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_IMAGE + " BLOB)";
        db.execSQL(createTable);
        
        // Insert a default row if empty
        ContentValues values = new ContentValues();
        values.put(COL_NAME, "User Name");
        values.put(COL_EMAIL, "user@gmail.com");
        values.put(COL_PHONE, "0123456789");
        values.put(COL_ADDRESS, "City, Country");
        db.insert(TABLE_PROFILE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }

    public Cursor getProfileData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PROFILE + " WHERE " + COL_ID + " = 1", null);
    }

    public boolean updateProfile(String name, String email, String phone, String address, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_ADDRESS, address);
        if (image != null) {
            values.put(COL_IMAGE, image);
        }
        
        int result = db.update(TABLE_PROFILE, values, COL_ID + " = ?", new String[]{"1"});
        return result > 0;
    }
}