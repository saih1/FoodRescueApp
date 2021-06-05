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
import com.saihtoo.foodrescueapp.model.UserItem;

import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements RecyclerViewAdapter.onFoodItemClickListener {
    public static final String CURRENT_FOOD_ID = "current_food_id";
    FloatingActionButton addButton;
    RecyclerViewAdapter adapter;
    List<FoodItem> foodItemList;
    RecyclerView homeRecyclerView;
    DBHelper db;
    int currentUserID;
    UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        addButton = findViewById(R.id.homeAddButton);
        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        currentUserID = getIntent().getIntExtra(MainActivity.CURRENT_USER, 0);

        db = new DBHelper(this);

        foodItemList = db.getAllFood();
        adapter = new RecyclerViewAdapter(foodItemList, HomeActivity.this, this);
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        homeRecyclerView.setLayoutManager(manager);
        homeRecyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddNewFoodActivity.class);
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
                Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                homeIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(homeIntent);
                finish();
                break;

            case R.id.accountMenu:
                Intent accountIntent = new Intent(HomeActivity.this, AccountActivity.class);
                accountIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(accountIntent);
                finish();
                break;

            case R.id.myListMenu:
                Intent listIntent = new Intent(HomeActivity.this, MyListActivity.class);
                listIntent.putExtra(MainActivity.CURRENT_USER, currentUserID);
                startActivity(listIntent);
                finish();
                break;
        } return true;
    }

    @Override
    public void onRowClick(int position) {
        FoodItem selection = foodItemList.get(position);
        Toast.makeText(HomeActivity.this, selection.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, FoodActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUserID);
        intent.putExtra(HomeActivity.CURRENT_FOOD_ID, selection.getFoodID());

        startActivity(intent);
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
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        user = db.getUserByID(currentUserID);
        intent.putExtra("username", user.getEmail());
        intent.putExtra("password", user.getPassword());
        startActivity(intent);
    }
}