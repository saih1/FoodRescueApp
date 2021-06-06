package com.saihtoo.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AddNewFoodActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddNewFoodActivity";
    ImageButton addImageButton;
    Button saveFood;
    EditText foodTitle, foodDescription, foodTime, foodQuantity;
    CalendarView foodCalendar;
    DBHelper db;
    Bitmap uploadImage;

    int userID;
    public static final int IMAGE_UPLOAD_CODE = 1001;

    String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        Objects.requireNonNull(getSupportActionBar()).setTitle("New Food");
        addImageButton = findViewById(R.id.addImageButton);
        saveFood = findViewById(R.id.newFoodSaveButton);
        foodTitle = findViewById(R.id.foodTitle);
        foodDescription = findViewById(R.id.foodDescription);
        foodTime = findViewById(R.id.foodTime);
        foodQuantity = findViewById(R.id.foodQuantity);
        foodCalendar = findViewById(R.id.calenderView);

        db = new DBHelper(this);

        addImageButton.setOnClickListener(this);
        saveFood.setOnClickListener(this);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.PLACES_API_KEY));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Address");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                selectedLocation = place.getLatLng().toString();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_UPLOAD_CODE) {
            Uri image = null;
            if (data != null) {
                image = data.getData();
            }
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            uploadImage = BitmapFactory.decodeStream(stream);
            addImageButton.setImageBitmap(uploadImage);
        }
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
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = dateFormat.format(new Date(foodCalendar.getDate()));
                String time = foodTime.getText().toString();
                String quantity = foodQuantity.getText().toString();
                String location = selectedLocation;

                FoodItem food = new FoodItem();
                userID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);
                food.setUserID(String.valueOf((userID)));
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