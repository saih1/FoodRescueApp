package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.UserItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CURRENT_USER = "current_user";
    private EditText usernameText, passwordText;
    private Button loginBtn, signupBtn;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameText = findViewById(R.id.mainUsernameEditText);
        passwordText = findViewById(R.id.mainPasswordEditText);
        loginBtn = findViewById(R.id.mainLoginBtn);
        signupBtn = findViewById(R.id.mainSignupBtn);

        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

        db = new DBHelper(this);
        UserItem userItem;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        usernameText.setText(intent.getStringExtra("username"));
        passwordText.setText(intent.getStringExtra("password"));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainLoginBtn:
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your username and password",
                            Toast.LENGTH_SHORT).show();
                } else {
                    int resultGetUser = db.getUser(username, password);
                    int userID = db.getUserID(username, password);
                    if (resultGetUser > -1) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra(CURRENT_USER, userID);
                        System.out.println(userID);
                        startActivity(intent);
                    } else
                        Toast.makeText(MainActivity.this, "User not found! Please Sign up.",
                                Toast.LENGTH_SHORT).show();
                } break;

            case R.id.mainSignupBtn:
                Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupIntent);
                break;
        }
    }

    //Quit the application on BackPress from Login Screen.
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}