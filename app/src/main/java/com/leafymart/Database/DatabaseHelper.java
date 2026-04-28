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
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_CART = "cart";

    // Common columns
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    // Profile columns
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_IMAGE = "image";

    // Favorites/Cart columns
    public static final String COL_PLANT_NAME = "plant_name";
    public static final String COL_PLANT_PRICE = "plant_price";
    public static final String COL_PLANT_IMAGE = "plant_image";
    public static final String COL_PLANT_RATING = "plant_rating";
    public static final String COL_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Profile table
        String createProfileTable = "CREATE TABLE " + TABLE_PROFILE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_IMAGE + " BLOB)";
        db.execSQL(createProfileTable);

        // Favorites table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLANT_NAME + " TEXT, " +
                COL_PLANT_PRICE + " TEXT, " +
                COL_PLANT_IMAGE + " INTEGER, " +
                COL_PLANT_RATING + " TEXT)";
        db.execSQL(createFavoritesTable);

        // Cart table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLANT_NAME + " TEXT, " +
                COL_PLANT_PRICE + " TEXT, " +
                COL_PLANT_IMAGE + " INTEGER, " +
                COL_QUANTITY + " INTEGER)";
        db.execSQL(createCartTable);

        // Insert a default row for profile
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Favorites Methods
    public boolean addFavorite(String name, String price, int image, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLANT_NAME, name);
        values.put(COL_PLANT_PRICE, price);
        values.put(COL_PLANT_IMAGE, image);
        values.put(COL_PLANT_RATING, rating);
        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    public void removeFavorite(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COL_PLANT_NAME + " = ?", new String[]{name});
    }

    public boolean isFavorite(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, COL_PLANT_NAME + " = ?", new String[]{name}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Cursor getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FAVORITES, null);
    }

    // Cart Methods
    public boolean addToCart(String name, String price, int image, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLANT_NAME, name);
        values.put(COL_PLANT_PRICE, price);
        values.put(COL_PLANT_IMAGE, image);
        values.put(COL_QUANTITY, quantity);
        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART, null);
    }

    public void removeFromCart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Existing Profile Methods...
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