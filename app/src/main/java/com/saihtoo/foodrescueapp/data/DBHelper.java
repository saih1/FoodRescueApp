package com.saihtoo.foodrescueapp.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.saihtoo.foodrescueapp.model.FoodItem;
import com.saihtoo.foodrescueapp.model.UserItem;
import com.saihtoo.foodrescueapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Util.SQL_CREATE_USER);
        db.execSQL(Util.SQL_CREATE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Util.SQL_DROP_TABLE);
        onCreate(db);
    }

    //INSERT METHODS
    //add user to the table
    public long insertUser(UserItem user) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Util.USER_FULL_NAME, user.getFullName());
        cv.put(Util.USER_EMAIL, user.getEmail());
        cv.put(Util.USER_PHONE, user.getPhone());
        cv.put(Util.USER_ADDRESS, user.getAddress());
        cv.put(Util.USER_PASSWORD, user.getPassword());
        long newRowID = db.insert(Util.DATABASE_NAME, null, cv);
        db.close();
        return newRowID;
    }

    //add food to the table with USER_ID for retrieval purposes
    public long insertFood(FoodItem food) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Util.USER_ID, food.getUserID());
        cv.put(Util.FOOD_TITLE, food.getTitle());
        cv.put(Util.FOOD_DESCRIPTION, food.getDescription());
        cv.put(Util.FOOD_DATE, food.getDate());
        cv.put(Util.FOOD_TIME, food.getTime());
        cv.put(Util.FOOD_QUANTITY, food.getQuantity());
        cv.put(Util.FOOD_LOCATION, food.getLocation());
        cv.put(Util.FOOD_IMAGE, food.getImage());
        long newRowID = db.insert(Util.DATABASE_NAME, null, cv);
        db.close();
        return newRowID;
    }

    //Get a user by user's email and password
    public int getUser(String username, String password) {
        db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.query(Util.USER_TABLE_NAME,
                new String[]{Util.USER_ID}, Util.USER_EMAIL + "=? and " + Util.USER_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        if (c.moveToFirst()) {
            db.close();
            return c.getCount();
        } else {
            db.close();
            return -1;
        }
    }

    public List<FoodItem> getAllFood() {
        List<FoodItem> foodItemList;
        db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.FOOD_TABLE_NAME;
        Cursor c = db.rawQuery(selectAll, null);

        foodItemList = new ArrayList<>();
        while (c.moveToNext()) {
            FoodItem food = new FoodItem();
            food.setUserID(c.getInt(c.getColumnIndex(Util.USER_ID)));
            food.setTitle(c.getString(c.getColumnIndex(Util.FOOD_TITLE)));
            food.setDescription(c.getString(c.getColumnIndex(Util.FOOD_DESCRIPTION)));
            byte[] Image = c.getBlob(c.getColumnIndex(Util.FOOD_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.length);
            food.setImage(bitmap);
            foodItemList.add(food);
        }
        db.close();
        return foodItemList;
    }

    public List<FoodItem> getAllFoodByUser(int userID) {
        List<FoodItem> foodItemList = new ArrayList<>();
        return foodItemList;
    }
}
