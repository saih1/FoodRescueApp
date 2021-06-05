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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saihtoo.foodrescueapp.adapter.RecyclerViewAdapter;
import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.util.List;
import java.util.Objects;

public class MyListActivity extends AppCompatActivity implements RecyclerViewAdapter.onFoodItemClickListener {
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
        Objects.requireNonNull(getSupportActionBar()).setTitle("My List");
        addButton = findViewById(R.id.myListAddButton);
        myListRecyclerView = findViewById(R.id.myListRecyclerView);
        currentUserID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);

        db = new DBHelper(this);

        foodItemList = db.getAllFoodByUser(currentUserID);
        adapter = new RecyclerViewAdapter(foodItemList, MyListActivity.this, this);
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
                Intent accountIntent = new Intent(MyListActivity.this, AccountActivity.class);
                accountIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(accountIntent);
                finish();
                break;

            case R.id.myListMenu:
                Intent mylistIntent = new Intent(MyListActivity.this, MyListActivity.class);
                mylistIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(mylistIntent);
                finish();
                break;

            case R.id.myCartMenu:
                Intent cartIntent = new Intent(MyListActivity.this, CartActivity.class);
                cartIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(cartIntent);
                finish();
                break;
        } return true;
    }

    @Override
    public void onRowClick(int position) {
        FoodItem selection = foodItemList.get(position);
        Toast.makeText(MyListActivity.this, selection.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClick(int position) {
        FoodItem selection = foodItemList.get(position);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareText =
                "Let’s start conserving resources!!\n" +
                        "Food : " + selection.getTitle() + "\n" +
                        "Description : " + selection.getDescription() + "\n";
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, null));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyListActivity.this, HomeActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
        startActivity(intent);
    }
}