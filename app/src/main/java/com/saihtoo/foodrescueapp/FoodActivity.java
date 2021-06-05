package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.FoodItem;

public class FoodActivity extends AppCompatActivity {
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    ImageView foodImage;
    TextView foodDescription, foodDate, foodTime, foodQuantity;
    Button addToCart;
    int currentUserID;
    int currentFoodID;
    DBHelper db;

    FoodItem food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodImage = findViewById(R.id.foodItemImageView);
        foodDescription = findViewById(R.id.foodItemDescription);
        foodDate = findViewById(R.id.foodItemDate);
        foodQuantity = findViewById(R.id.foodItemQuantity);
        foodTime = findViewById(R.id.foodItemTime);
        addToCart = findViewById(R.id.foodItemAddToCart);

        Intent intent = getIntent();
        currentUserID = intent.getIntExtra(MainActivity.CURRENT_USER, 0);
        currentFoodID = intent.getIntExtra(HomeActivity.CURRENT_FOOD_ID, 0);

        db = new DBHelper(this);
        food = db.getFoodByID(currentFoodID);

        foodImage.setImageBitmap(food.getImage());
        foodDescription.setText("Description: " + food.getDescription());
        foodDate.setText("Date: " + food.getDate());
        foodTime.setText("Pick up time: " + food.getTime());
        foodQuantity.setText("Quantity: " + food.getQuantity());

        Fragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        Double[] latlngArray = convertToLatLng(food.getLocation());
        double latitude = latlngArray[0];
        double longitude = latlngArray[1];
        bundle.putDouble(LAT, latitude);
        bundle.putDouble(LNG, longitude);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mapsFrameLayout, fragment).commit();
    }

    //Method to convert location saved in String format to Double values
    protected Double[] convertToLatLng (String strLatLng) {
        String stringInBracket = strLatLng.substring(strLatLng.indexOf("(")+1, strLatLng.indexOf(")"));
        String[] stringByComma = stringInBracket.split(",");
        return new Double[]{Double.valueOf(stringByComma[0]), Double.valueOf(stringByComma[1])};
    }
}