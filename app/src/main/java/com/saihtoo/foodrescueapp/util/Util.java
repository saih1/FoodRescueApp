package com.saihtoo.foodrescueapp.util;

public class Util {
    //DATABASE
    public static final String DATABASE_NAME = "food_rescue_app.db";
    public static final String USER_TABLE_NAME = "user_table";
    public static final String FOOD_TABLE_NAME = "food_table";
    public static final int DATABASE_VERSION = 1;

    //FOOD TABLE
    public static final String FOOD_ID = "food_id";
    public static final String FOOD_IMAGE = "food_image";
    public static final String FOOD_TITLE = "food_title";
    public static final String FOOD_DESCRIPTION = "food_description";
    public static final String FOOD_DATE = "food_date";
    public static final String FOOD_TIME = "food_time";
    public static final String FOOD_QUANTITY = "food_quantity";
    public static final String FOOD_LOCATION = "food_location";

    //USER TABLE
    public static final String USER_ID = "user_id";
    public static final String USER_FULL_NAME = "user_full_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_PASSWORD = "user_password";

    //SQL
    //CREATE USER TABLE
    public static final String SQL_CREATE_USER = "CREATE TABLE " +
            USER_TABLE_NAME + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_FULL_NAME + " TEXT," +
            USER_EMAIL + " TEXT," +
            USER_PHONE + " TEXT, " +
            USER_ADDRESS + " TEXT, " +
            USER_PASSWORD + " TEXT)";

    //CREATE FOOD TABLE
    public static final String SQL_CREATE_FOOD = "CREATE TABLE " +
            FOOD_TABLE_NAME + "(" +
            FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_ID + " TEXT," +
            FOOD_IMAGE + " BLOB, " +
            FOOD_TITLE + " TEXT, " +
            FOOD_DESCRIPTION + " TEXT, " +
            FOOD_DATE +  " INTEGER, " +
            FOOD_TIME + " INTEGER, " +
            FOOD_QUANTITY + " INTEGER, " +
            FOOD_LOCATION + " TEXT)";

    //DROP TABLE
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DATABASE_NAME;
}
