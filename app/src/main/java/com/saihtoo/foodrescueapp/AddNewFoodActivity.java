package com.saihtoo.foodrescueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddNewFoodActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton addImageButton;
    Button saveFood;
    EditText foodTitle, foodDescription, foodTime, foodQuantity, foodLocation;
    CalendarView foodCalendar;

    int userID;
    DBHelper db;
    //toStoreImageFile
    Bitmap uploadImage;
    public static final int IMAGE_UPLOAD_CODE = 1001;

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

        db = new DBHelper(this);

        addImageButton.setOnClickListener(this);
        saveFood.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri image = data.getData();
        InputStream stream = null;
        try {
            stream = getContentResolver().openInputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        uploadImage = BitmapFactory.decodeStream(stream);
        addImageButton.setImageBitmap(uploadImage);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImageButton:
                Intent imageIntent = new Intent(Intent.ACTION_PICK);
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                imageIntent.setDataAndType(Uri.parse(file.getPath()), "image/*");
                startActivityForResult(imageIntent, IMAGE_UPLOAD_CODE);
                break;

            case R.id.newFoodSaveButton:
                String title = foodTitle.getText().toString();
                String description = foodDescription.getText().toString();
                long calendarDate = foodCalendar.getDate();
                String date = String.valueOf(calendarDate);
                String time = foodTime.getText().toString();
                String quantity = foodQuantity.getText().toString();
                String location = foodLocation.getText().toString();

                FoodItem food = new FoodItem();
                userID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);
                food.setUserID(String.valueOf(userID));
                System.out.println(userID + "****************");
                food.setTitle(title);
                food.setDescription(description);
                food.setDate(date);
                food.setTime(time);
                food.setQuantity(quantity);
                food.setLocation(location);
                food.setImage(uploadImage);

                db.insertFood(food);
                Intent intent = new Intent(AddNewFoodActivity.this, HomeActivity.class);
                intent.putExtra(MainActivity.CURRENT_USER, userID);
                startActivity(intent);
                break;
        }
    }
}