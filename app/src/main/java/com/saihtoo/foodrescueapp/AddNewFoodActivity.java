package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddNewFoodActivity extends AppCompatActivity {
    ImageButton addImageButton;
    Button saveFood;
    EditText foodTitle, foodDescription, foodTime, foodQuantity, foodLocation;
    CalendarView foodCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        addImageButton = findViewById(R.id.addImageButton);
        saveFood = findViewById(R.id.newFoodSaveButton);
        foodTitle = findViewById(R.id.foodTitle);
        foodDescription = findViewById(R.id.foodDescription);
        foodTime = findViewById(R.id.foodTime);
        foodQuantity = findViewById(R.id.foodQuantity);
        foodLocation = findViewById(R.id.foodLocation);
        foodCalendar = findViewById(R.id.calenderView);


    }
}