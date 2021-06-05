package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.saihtoo.foodrescueapp.data.CartDBHelper;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.CartItem;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.util.Objects;

public class FoodActivity extends AppCompatActivity {
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    ImageView foodImage;
    TextView foodDescription, foodDate, foodTime, foodQuantity;
    Button addToCart;
    int currentUserID;
    int currentFoodID;
    DBHelper db;
    CartDBHelper cartDb;
    FoodItem food;

    @SuppressLint("SetTextI18n")
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

        cartDb = new CartDBHelper(this);

        db = new DBHelper(this);
        food = db.getFoodByID(currentFoodID);
        Objects.requireNonNull(getSupportActionBar()).setTitle(food.getTitle());

        foodImage.setImageBitmap(food.getImage());
        foodDescription.setText("Description: " + food.getDescription());
        foodDate.setText("Date: " + food.getDate());
        foodTime.setText("Pick up time: " + food.getTime());
        foodQuantity.setText("Quantity: " + food.getQuantity());

        Fragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        Double[] lat_lngArray = convertToLatLng(food.getLocation());
        double latitude = lat_lngArray[0];
        double longitude = lat_lngArray[1];
        bundle.putDouble(LAT, latitude);
        bundle.putDouble(LNG, longitude);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mapsFrameLayout, fragment).commit();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cart = new CartItem();
                cart.setFoodID(food.getFoodID());
                cart.setTitle(food.getTitle());
                cart.setDescription(food.getDescription());
                cart.setDate(food.getDate());
                cart.setQuantity(food.getQuantity());
                cart.setImage(food.getImage());
                long result = cartDb.insertCartItem(cart);
                if (result > 0) Toast.makeText(FoodActivity.this,"Added to cart!",
                            Toast.LENGTH_SHORT).show();
                else Toast.makeText(FoodActivity.this,"Unable to add to cart!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to convert location saved in String format to Double values
    protected Double[] convertToLatLng (String strLatLng) {
        String stringInBracket = strLatLng.substring(strLatLng.indexOf("(")+1, strLatLng.indexOf(")"));
        String[] stringByComma = stringInBracket.split(",");
        return new Double[]{Double.valueOf(stringByComma[0]), Double.valueOf(stringByComma[1])};
    }
}