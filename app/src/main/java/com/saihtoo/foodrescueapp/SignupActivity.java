package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText fullName, email, phoneNumber, address, password, confirmPassword;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fullName = findViewById(R.id.sFullName);
        email = findViewById(R.id.sEmail);
        phoneNumber = findViewById(R.id.sPhone);
        address = findViewById(R.id.sAddress);
        password = findViewById(R.id.sPassword);
        confirmPassword = findViewById(R.id.sConfirmPassword);
        signUpButton = findViewById(R.id.sSignUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = fullName.getText().toString();
                String e = email.getText().toString();
                String p = phoneNumber.getText().toString();
                String a = address.getText().toString();
                String pa = password.getText().toString();
                String cp = confirmPassword.getText().toString();

                if (pa.equals(cp))
                {
                    //Register User
                    //Add data to the User Database
                }
                else
                    Toast.makeText(SignupActivity.this, "ERROR: Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
}