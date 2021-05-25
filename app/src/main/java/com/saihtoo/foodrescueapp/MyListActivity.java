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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saihtoo.foodrescueapp.adapter.RecyclerViewAdapter;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.util.List;

public class MyListActivity extends AppCompatActivity
{
    FloatingActionButton addButton;
    RecyclerViewAdapter adapter;
    List<FoodItem> foodItemList;
    RecyclerView myListRecyclerView;
    DBHelper db;
    int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        addButton = findViewById(R.id.myListAddButton);
        myListRecyclerView = findViewById(R.id.myListRecyclerView);
        currentUserID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);

        db = new DBHelper(this);

        foodItemList = db.getAllFoodByUser(currentUserID);
        adapter = new RecyclerViewAdapter(foodItemList, MyListActivity.this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myListRecyclerView.setLayoutManager(manager);
        myListRecyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListActivity.this, AddNewFoodActivity.class);
                intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(intent);
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
                Intent homeIntent = new Intent(MyListActivity.this, HomeActivity.class);
                homeIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(homeIntent);
                finish();
                break;

            case R.id.accountMenu:

                break;
            case R.id.myListMenu:
                Intent mylistIntent = new Intent(MyListActivity.this, MyListActivity.class);
                mylistIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(mylistIntent);
                finish();
                break;
        }
        return true;
    }
}