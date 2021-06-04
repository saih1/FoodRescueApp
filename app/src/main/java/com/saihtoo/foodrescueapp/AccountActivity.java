package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.UserItem;

public class AccountActivity extends AppCompatActivity {
    TextView fullName, emailAddress, phoneNumber, address;
    int currentUserID;
    DBHelper db;

    UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
    public void onBackPressed() {
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
        startActivity(intent);
    }
}