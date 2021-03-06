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

    //add user to the table
    public long insertUser(UserItem user) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Util.USER_FULL_NAME, user.getFullName());
        cv.put(Util.USER_EMAIL, user.getEmail());
        cv.put(Util.USER_PHONE, user.getPhone());
        cv.put(Util.USER_ADDRESS, user.getAddress());
        cv.put(Util.USER_PASSWORD, user.getPassword());
        long newRowID = db.insert(Util.USER_TABLE_NAME, null, cv);
        db.close();
        return newRowID;
    }

    //add food to the table with current user id
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
        cv.put(Util.FOOD_IMAGE, food.getImageBitmap());
        long newRowID = db.insert(Util.FOOD_TABLE_NAME, null, cv);
        db.close();
        return newRowID;
    }

    //get userid by email & password
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

    //get userItem instance by userID
    public UserItem getUserByID(int userID) {
        db = this.getReadableDatabase();
        String fullName = null, emailAddress = null, address = null, phoneNumber = null, password = null;

        Cursor c = db.rawQuery("SELECT * FROM " + Util.USER_TABLE_NAME + " WHERE " + Util.USER_ID + "=? ",
                new String[]{String.valueOf(userID)});
        while (c.moveToNext()) {
            fullName = c.getString(c.getColumnIndex(Util.USER_FULL_NAME));
            emailAddress = c.getString(c.getColumnIndex(Util.USER_EMAIL));
            address = c.getString(c.getColumnIndex(Util.USER_ADDRESS));
            phoneNumber = c.getString(c.getColumnIndex(Util.USER_PHONE));
            password = c.getString(c.getColumnIndex(Util.USER_PASSWORD));
        }
        db.close();
        return new UserItem(fullName, emailAddress, phoneNumber, address, password);
    }

    //get foodItem instance by food id
    public FoodItem getFoodByID(int foodID) {
        db = this.getReadableDatabase();
        Bitmap bitmap = null;
        String title = null, description = null, date = null, time = null, quantity = null, location = null;

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery("SELECT * FROM " + Util.FOOD_TABLE_NAME + " WHERE " + Util.FOOD_ID + "=? ",
                new String[]{String.valueOf(foodID)});
        while (c.moveToNext()) {
            byte[] image = c.getBlob(c.getColumnIndex(Util.FOOD_IMAGE));
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            title = c.getString(c.getColumnIndex(Util.FOOD_TITLE));
            description = c.getString(c.getColumnIndex(Util.FOOD_DESCRIPTION));
            date = c.getString(c.getColumnIndex(Util.FOOD_DATE));
            time = c.getString(c.getColumnIndex(Util.FOOD_TIME));
            quantity = c.getString(c.getColumnIndex(Util.FOOD_QUANTITY));
            location = c.getString(c.getColumnIndex(Util.FOOD_LOCATION));
        }
        db.close();
        return new FoodItem(bitmap, title, description, date, time, quantity, location);
    }

    //get user id by email address and password (login details)
    public int getUserID(String username, String password) {
        db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.query(Util.USER_TABLE_NAME,
                new String[]{Util.USER_ID}, Util.USER_EMAIL + "=? and " + Util.USER_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        if (c.moveToFirst()) {
            db.close();
            return c.getInt(c.getColumnIndex(Util.USER_ID));
        } else {
            db.close();
            return -1;
        }
    }

    //get all the food items available in the food table
    public List<FoodItem> getAllFood() {
        List<FoodItem> foodItemList;
        db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.FOOD_TABLE_NAME;
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectAll, null);
        foodItemList = new ArrayList<>();
        while (c.moveToNext()) {
            FoodItem food = new FoodItem();
            food.setFoodID(c.getInt(c.getColumnIndex(Util.FOOD_ID)));
            food.setUserID(c.getString(c.getColumnIndex(Util.USER_ID)));
            food.setTitle(c.getString(c.getColumnIndex(Util.FOOD_TITLE)));
            food.setDescription(c.getString(c.getColumnIndex(Util.FOOD_DESCRIPTION)));
            food.setLocation(c.getString(c.getColumnIndex(Util.FOOD_LOCATION)));
            food.setTime(c.getString(c.getColumnIndex(Util.FOOD_TIME)));
            food.setQuantity(c.getString(c.getColumnIndex(Util.FOOD_QUANTITY)));
            byte[] Image = c.getBlob(c.getColumnIndex(Util.FOOD_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.length);
            food.setImage(bitmap);
            foodItemList.add(food);
        }
        db.close();
        return foodItemList;
    }

    //get a list of FoodItem instances specific to the current by using userID
    public List<FoodItem> getAllFoodByUser(int userID) {
        List<FoodItem> foodItemList;
        db = this.getReadableDatabase();
        String selectAllByUserID = "SELECT * FROM " + Util.FOOD_TABLE_NAME + " WHERE " + Util.USER_ID + "= ?";
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectAllByUserID, new String[]{String.valueOf(userID)});
        foodItemList = new ArrayList<>();
        while (c.moveToNext()) {
            FoodItem food = new FoodItem();
            food.setUserID(c.getString(c.getColumnIndex(Util.USER_ID)));
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

    //Delete a row by FoodID from Food Table
    public void deleteFoodByID(String foodID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.FOOD_TABLE_NAME, Util.FOOD_ID + "=?", new String[]{String.valueOf(foodID)});
        db.close();
    }

    //Delete rows by a list of FoodIDs from Food Table
    public void deleteFoodsByIDs(List<Integer> foodIDList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < foodIDList.size(); i++) {
            db.delete(Util.FOOD_TABLE_NAME, Util.FOOD_ID + "=?", new String[]{String.valueOf(foodIDList.get(i))});
        }
        db.close();
    }
}
