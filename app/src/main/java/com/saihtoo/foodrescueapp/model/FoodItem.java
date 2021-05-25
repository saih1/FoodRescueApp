package com.saihtoo.foodrescueapp.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class FoodItem {
    private int foodID, userID;
    private Bitmap image;
    private String title, description, date, time, quantity, location;

    public FoodItem(int foodID, int userID, Bitmap image, String title, String description, String date, String time, String quantity, String location) {
        this.foodID = foodID;
        this.userID = userID;
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.quantity = quantity;
        this.location = location;
    }

    public FoodItem(Bitmap image, String title, String description, String date, String time, String quantity, String location) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.quantity = quantity;
        this.location = location;
    }

    public FoodItem() { }

    public int getFoodID()
    {
        return foodID;
    }

    public void setFoodID(int foodID)
    {
        this.foodID = foodID;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public byte[] getImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.image.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
