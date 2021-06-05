package com.saihtoo.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.saihtoo.foodrescueapp.adapter.CartViewAdapter;
import com.saihtoo.foodrescueapp.data.CartDBHelper;
import com.saihtoo.foodrescueapp.model.CartItem;
import com.saihtoo.foodrescueapp.util.PaymentsUtil;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CartActivity extends AppCompatActivity {
    CartViewAdapter adapter;
    List<CartItem> cartItemList;
    RecyclerView cartRecyclerView;
    CartDBHelper cartDb;
    Button clearCart;
    Button googlePayButton;

    TextView cartTotal;
    int currentUserID;
    int totalPrice;
    int priceOfEachItem = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Cart");

        cartRecyclerView = findViewById(R.id.myCartRecyclerView);
        clearCart = findViewById(R.id.clearCart);
        cartTotal = findViewById(R.id.cartTotalText);
        googlePayButton = findViewById(R.id.googlePayButton);

        currentUserID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);

        cartDb = new CartDBHelper(this);
        cartItemList = cartDb.getAllCartItems();

        adapter = new CartViewAdapter(cartItemList, CartActivity.this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(manager);
        cartRecyclerView.setAdapter(adapter);

        totalPrice = cartItemList.size() * priceOfEachItem;
        cartTotal.setText("TOTAL: $ " + totalPrice);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDb.eraseTable();
                if (cartDb.dbIsEmpty()) {
                    Toast.makeText(CartActivity.this, "Your cart has been cleared", Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(CartActivity.this, "ERROR: cart cannot be cleared", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(intent);
            }
        });

        googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });
    }

    //RequestPayment
    protected void requestPayment() {
        PaymentsClient paymentsClient = PaymentsUtil.createPaymentsClient(CartActivity.this);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(totalPrice);
        if (!paymentDataRequestJson.isPresent()) { return; }
        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),
                CartActivity.this, PaymentsUtil.REQUEST_PAYMENT);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentsUtil.REQUEST_PAYMENT) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(CartActivity.this, "Payment Successful.", Toast.LENGTH_SHORT).show();
                    cartDb.eraseTable();
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                    startActivity(intent);
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(CartActivity.this, "Payment cancelled.", Toast.LENGTH_SHORT).show();
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    Toast.makeText(this, AutoResolveHelper.RESULT_ERROR, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + resultCode);
            }
        }
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeMenu:
                Intent homeIntent = new Intent(CartActivity.this, HomeActivity.class);
                homeIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(homeIntent);
                finish();
                break;

            case R.id.accountMenu:
                Intent accountIntent = new Intent(CartActivity.this, AccountActivity.class);
                accountIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(accountIntent);
                finish();
                break;

            case R.id.myListMenu:
                Intent mylistIntent = new Intent(CartActivity.this, MyListActivity.class);
                mylistIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(mylistIntent);
                finish();
                break;

            case R.id.myCartMenu:
                Intent cartIntent = new Intent(CartActivity.this, CartActivity.class);
                cartIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(cartIntent);
                finish();
                break;
        } return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
        startActivity(intent);
    }
}