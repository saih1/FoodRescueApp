package com.saihtoo.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saihtoo.foodrescueapp.data.DBHelper;
import com.saihtoo.foodrescueapp.model.UserItem;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    EditText fullName, email, phoneNumber, address, password, confirmPassword;
    Button signUpButton;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up");
        fullName = findViewById(R.id.sFullName);
        email = findViewById(R.id.sEmail);
        phoneNumber = findViewById(R.id.sPhone);
        address = findViewById(R.id.sAddress);
        password = findViewById(R.id.sPassword);
        confirmPassword = findViewById(R.id.sConfirmPassword);
        signUpButton = findViewById(R.id.sSignUpButton);

        db = new DBHelper(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = fullName.getText().toString();
                String e = email.getText().toString();
                String p = phoneNumber.getText().toString();
                String a = address.getText().toString();
                String pa = password.getText().toString();
                String cp = confirmPassword.getText().toString();
                if (pa.equals(cp)) {
                    long result = db.insertUser(new UserItem(n, e, p, a, pa));
                    if (result > 0)
                        Toast.makeText(SignupActivity.this, "You have been registered!",
                                Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(SignupActivity.this, "ERROR: Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
}