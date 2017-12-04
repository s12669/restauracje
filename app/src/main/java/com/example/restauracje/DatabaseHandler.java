package com.example.restauracje;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restauracje.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "projekt";

    // Monuments table name
    private static final String TABLE_RESTAURANTS = "restaurants";

    // Monuments Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STREET = "street";
    private static final String KEY_POSTAL_CODE = "postal_code";
    private static final String KEY_CITY = "city";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
//                + KEY_ID + " INTEGER PRIMARY KEY,"
//                + KEY_NAME + " TEXT,"
//                + KEY_DESCRIPTION + " TEXT,"
//                + KEY_STREET + " TEXT,"
//                + KEY_POSTAL_CODE + " TEXT,"
//                + KEY_CITY + " TEXT"
//                + ")";
//        db.execSQL(CREATE_RESTAURANTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new restaurant
    void addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, restaurant.getName());
        values.put(KEY_DESCRIPTION, restaurant.getDescription());
        values.put(KEY_STREET, restaurant.getStreet());
        values.put(KEY_CITY, restaurant.getCity());
        values.put(KEY_POSTAL_CODE, restaurant.getPostal_code());

        // Inserting Row
        db.insert(TABLE_RESTAURANTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single monument
    public Restaurant getRestaurant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESCRIPTION, KEY_STREET, KEY_CITY, KEY_POSTAL_CODE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Restaurant restaurant = new Restaurant(cursor.getString(1), cursor.getString(2));
        restaurant.setID(cursor.getInt(0));
        restaurant.setStreet(cursor.getString(3));
        restaurant.setCity(cursor.getString(4));
        restaurant.setPostal_code(cursor.getString(5));
        return restaurant;
    }

    // Getting All Monuments
    public List<Restaurant> getRestaurants() {
        SQLiteDatabase db = this.getWritableDatabase();



        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;


        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.setID(Integer.parseInt(cursor.getString(0)));
                restaurant.setName(cursor.getString(1));
                restaurant.setDescription(cursor.getString(2));
                restaurant.setStreet(cursor.getString(3));
                restaurant.setCity(cursor.getString(4));
                restaurant.setPostal_code(cursor.getString(5));
                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }

        // return monument list
        return restaurantList;
    }

    // Updating single restaurant
    public int updateRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, restaurant.getName());
        values.put(KEY_DESCRIPTION, restaurant.getDescription());
        values.put(KEY_STREET, restaurant.getStreet());
        values.put(KEY_CITY, restaurant.getCity());
        values.put(KEY_POSTAL_CODE, restaurant.getPostal_code());

        // updating row
        return db.update(TABLE_RESTAURANTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(restaurant.getID()) });
    }

    // Deleting single restaurant
    public void deleteRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_ID + " = ?",
                new String[] { String.valueOf(restaurant.getID()) });
        db.close();
    }

    public void deleteRestaurant(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }


    // Getting monuments Count
    public int getRestaurantsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
