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

public class CustomerLoginActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void onLoginClick(View view) {
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
                        if (user != null) {
                            // User is logged in
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            // ...
                            Intent intent = new Intent(this, CustomerLandingActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // Login failed
                        Toast.makeText(this, "Login failed. Please check your credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void onRegisterClickk(View view) {
        Intent intent = new Intent(this, RegisterCustomerActivity.class);
        startActivity(intent);
    }

    public void onForgetPasswordClickk(View view) {
        // Handle "Forgot Password" button click
    }

}

