package com.saihtoo.foodrescueapp.util;

public class CartUtil {
    public static final String DATABASE_NAME = "food_rescue_app.cartDb";
    public static final String CART_TABLE_NAME = "cart_table";
    public static final int DATABASE_VERSION = 1;

    public static final String CART_ITEM_ID = "cart_item_id";
    public static final String CART_FOOD_ID = "cart_food_id";
    public static final String CART_ITEM_IMAGE = "cart_item_image";
    public static final String CART_ITEM_TITLE = "cart_item_title";
    public static final String CART_ITEM_DESCRIPTION = "cart_item_description";
    public static final String CART_ITEM_DATE = "food_date";
    public static final String CART_ITEM_QUANTITY = "food_quantity";

    public static final String SQL_CREATE_CART = "CREATE TABLE " +
            CART_TABLE_NAME + "(" +
            CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CART_FOOD_ID + " INTEGER," +
            CART_ITEM_IMAGE + " BLOB, " +
            CART_ITEM_TITLE + " TEXT, " +
            CART_ITEM_DESCRIPTION + " TEXT, " +
            CART_ITEM_DATE +  " INTEGER, " +
            CART_ITEM_QUANTITY + " INTEGER)";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DATABASE_NAME;
}
