package com.saihtoo.foodrescueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.saihtoo.foodrescueapp.data.CartDBHelper;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.CartItem;
import com.saihtoo.foodrescueapp.model.FoodItem;
import com.saihtoo.foodrescueapp.util.PaymentsUtil;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;

public class FoodActivity extends AppCompatActivity {
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    ImageView foodImage;
    TextView foodDescription, foodDate, foodTime, foodQuantity, foodPrice;
    Button addToCart;
    RelativeLayout googlePayButton;

    int currentUserID;
    int currentFoodID;

    DBHelper db;
    CartDBHelper cartDb;
    FoodItem food;

    final int priceOfEachItem = 3;

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
        foodPrice = findViewById(R.id.foodItemPrice);
        addToCart = findViewById(R.id.foodItemAddToCart);
        googlePayButton = findViewById(R.id.foodGooglePayButton);

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
        foodPrice.setText("Price: $ " + priceOfEachItem);

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
                cart.setFoodID(currentFoodID);
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

        googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentsUtil.REQUEST_PAYMENT) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(FoodActivity.this, "Payment successful.", Toast.LENGTH_SHORT).show();

                    //Delete purchased items from the Database tables
                    cartDb.deleteFoodByID(currentFoodID);
                    db.deleteFoodByID(String.valueOf(currentFoodID));

                    Intent intent = new Intent(FoodActivity.this, HomeActivity.class);
                    intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                    startActivity(intent);
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(FoodActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    Toast.makeText(this, AutoResolveHelper.RESULT_ERROR, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + resultCode);
            }
        }
    }

    //RequestPayment
    protected void requestPayment() {
        PaymentsClient paymentsClient = PaymentsUtil.createPaymentsClient(FoodActivity.this);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceOfEachItem);
        if (!paymentDataRequestJson.isPresent()) { return; }
        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),
                FoodActivity.this, PaymentsUtil.REQUEST_PAYMENT);
    }

    //Method to convert location saved in String format to Double values
    protected Double[] convertToLatLng (String strLatLng) {
        String stringInBracket = strLatLng.substring(strLatLng.indexOf("(")+1, strLatLng.indexOf(")"));
        String[] stringByComma = stringInBracket.split(",");
        return new Double[]{Double.valueOf(stringByComma[0]), Double.valueOf(stringByComma[1])};
    }
}