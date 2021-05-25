package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameText, passwordText;
    private Button loginBtn, signupBtn;

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
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainLoginBtn:
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                //Check if Username and Password exist


                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;

            case R.id.mainSignupBtn:
                Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupIntent);
                break;
        }
    }
}