package com.example.queuemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BusinessLoginActivity extends AppCompatActivity {
    // ...

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void onLoginClick(View view) {
        // Retrieve the input values from the EditText fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the input values
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in with email and password using Firebase Authentication
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // User is logged in and email is verified
                            // Proceed to the next activity or perform necessary actions
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, BusinessLandingActivity.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Email not verified
                            Toast.makeText(this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Login failed
                        Toast.makeText(this, "Login failed. Please check your credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onForgotPasswordClick(View view) {
        Intent intent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(intent);
        // Handle "Forgot Password" button click
        // Implement your forgot password logic here
    }

    public void onRegisterClick(View view) {
        // Handle "Register" button click
        Intent intent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(intent);
    }
}


//public class BusinessLoginActivity extends AppCompatActivity {
//    private EditText emailEditText, passwordEditText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_business_login);
//
//        // Retrieve references to the UI elements
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//    }
//
//    public void onLoginClick(View view) {
//        // Handle login button click
//        // Implement your login logic here
//    }
//
//    public void onForgotPasswordClick(View view) {
//        // Handle "Forgot Password" button click
//        // Implement your forgot password logic here
//    }
//
//    public void onRegisterClick(View view) {
//        // Handle "Register" button click
//        Intent intent = new Intent(this, RegisterBusinessActivity.class);
//        startActivity(intent);
//    }
//
//}

