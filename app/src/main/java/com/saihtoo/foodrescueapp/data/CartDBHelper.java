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

import com.saihtoo.foodrescueapp.model.CartItem;
import com.saihtoo.foodrescueapp.util.CartUtil;
import com.saihtoo.foodrescueapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class CartDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public CartDBHelper(@Nullable Context context) {
        super(context, CartUtil.DATABASE_NAME, null, CartUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CartUtil.SQL_CREATE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CartUtil.SQL_DROP_TABLE);
        onCreate(db);
    }

    //addData
    public long insertCartItem(CartItem cart) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CartUtil.CART_FOOD_ID, cart.getFoodID());
        cv.put(CartUtil.CART_ITEM_TITLE, cart.getTitle());
        cv.put(CartUtil.CART_ITEM_DESCRIPTION, cart.getDescription());
        cv.put(CartUtil.CART_ITEM_DATE, cart.getDate());
        cv.put(CartUtil.CART_ITEM_QUANTITY, cart.getQuantity());
        cv.put(CartUtil.CART_ITEM_IMAGE, cart.getImageBitmap());
        long newRowID = db.insert(CartUtil.CART_TABLE_NAME, null, cv);
        db.close();
        return newRowID;
    }

    //getAllItem
    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItemList;
        db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + CartUtil.CART_TABLE_NAME;
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectAll, null);

        cartItemList = new ArrayList<>();
        while (c.moveToNext()) {
            CartItem cart = new CartItem();
            cart.setCartID(c.getInt(c.getColumnIndex(CartUtil.CART_ITEM_ID)));
            cart.setFoodID(c.getInt(c.getColumnIndex(CartUtil.CART_FOOD_ID)));

            cart.setTitle(c.getString(c.getColumnIndex(CartUtil.CART_ITEM_TITLE)));
            cart.setDescription(c.getString(c.getColumnIndex(CartUtil.CART_ITEM_DESCRIPTION)));
            cart.setDate(c.getString(c.getColumnIndex(CartUtil.CART_ITEM_DATE)));
            cart.setQuantity(c.getString(c.getColumnIndex(CartUtil.CART_ITEM_QUANTITY)));
            //adding Image
            byte[] Image = c.getBlob(c.getColumnIndex(CartUtil.CART_ITEM_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.length);
            cart.setImage(bitmap);
            cartItemList.add(cart);
        }
        db.close();
        return cartItemList;
    }

    //deleteTable
    public void eraseTable() {
        db = this.getWritableDatabase();
        db.delete(CartUtil.CART_TABLE_NAME, null, null);
        db.close();
    }

    //Check if table is empty
    public boolean dbIsEmpty() {
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CartUtil.CART_TABLE_NAME, null);
        if (c.moveToNext()) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    //Delete a row by FoodID from Cart Table
    public void deleteFoodByID(int foodID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CartUtil.CART_TABLE_NAME, CartUtil.CART_FOOD_ID + "=?", new String[]{String.valueOf(foodID)});
        db.close();
    }
}
