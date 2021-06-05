package com.saihtoo.foodrescueapp;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.saihtoo.foodrescueapp.adapter.CartViewAdapter;
import com.saihtoo.foodrescueapp.data.CartDBHelper;
import com.saihtoo.foodrescueapp.model.CartItem;

import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    CartViewAdapter adapter;
    List<CartItem> cartItemList;
    RecyclerView cartRecyclerView;
    CartDBHelper cartDb;
    Button clearCart;
    int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Cart");

        cartRecyclerView = findViewById(R.id.myCartRecyclerView);
        clearCart = findViewById(R.id.clearCart);

        currentUserID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);

        cartDb = new CartDBHelper(this);
        cartItemList = cartDb.getAllCartItems();

        adapter = new CartViewAdapter(cartItemList, CartActivity.this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(manager);
        cartRecyclerView.setAdapter(adapter);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDb.eraseTable();
                if (cartDb.dbIsEmpty()) {
                    Toast.makeText(CartActivity.this, "Your cart has been cleared", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
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