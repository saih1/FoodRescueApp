package com.saihtoo.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.UserItem;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    TextView fullName, emailAddress, phoneNumber, address;
    int currentUserID;
    DBHelper db;

    UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account");
        fullName = findViewById(R.id.accountName);
        emailAddress = findViewById(R.id.accountEmail);
        phoneNumber = findViewById(R.id.accountPhoneNumber);
        address = findViewById(R.id.accountAddress);

        Intent intent = getIntent();
        currentUserID = intent.getIntExtra(MainActivity.CURRENT_USER, 0);

        db = new DBHelper(this);
        user = db.getUserByID(currentUserID);

        fullName.setText("FULL NAME: " + user.getFullName());
        emailAddress.setText("EMAIL: " + user.getEmail());
        phoneNumber.setText("PHONE NUMBER: " +user.getPhone());
        address.setText("ADDRESS: " + user.getAddress());
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
                Intent homeIntent = new Intent(AccountActivity.this, HomeActivity.class);
                homeIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(homeIntent);
                finish();
                break;

            case R.id.accountMenu:
                Intent accountIntent = new Intent(AccountActivity.this, AccountActivity.class);
                accountIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(accountIntent);
                finish();
                break;

            case R.id.myListMenu:
                Intent listIntent = new Intent(AccountActivity.this, MyListActivity.class);
                listIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(listIntent);
                finish();
                break;

            case R.id.myCartMenu:
                Intent cartIntent = new Intent(AccountActivity.this, CartActivity.class);
                cartIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(cartIntent);
                finish();
                break;
        } return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
        startActivity(intent);
    }
}