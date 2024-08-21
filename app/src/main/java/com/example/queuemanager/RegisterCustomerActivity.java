package com.example.queuemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegisterCustomerActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, numberEditText, addressEditText, nameEditText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private static final int MIN_LENGTH = 8;
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        // Retrieve references to the UI elements
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        numberEditText = findViewById(R.id.numberEditText);
        addressEditText = findViewById(R.id.addressEditText);

        // Get a reference to the Firebase Realtime Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CustomerUser");
    }

    public void onRegisterClick(View view) {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        int hasRequest=0;
        int in = 0;

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || number.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else if (!isPasswordSecure(password)) {
            Toast.makeText(this, "Password is not secure", Toast.LENGTH_SHORT).show();
            return;
        }else{
            // Create a new Customer object with the user input
            Customer customer = new Customer(name, email, number, address, hasRequest,in);

            // Save the Customer object to the Firebase Realtime Database
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(emailVerificationTask -> {
                                            if (emailVerificationTask.isSuccessful()) {
                                                // Email verification sent
                                                Toast.makeText(this, "Email verification sent", Toast.LENGTH_SHORT).show();

                                                // Save the Customer object to the "customerUsers" node
                                                databaseReference.child(user.getUid()).setValue(customer)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Registration successful
                                                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(this, CustomerLoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Registration failed
                                                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                // Failed to send email verification
                                                Toast.makeText(this, "Failed to send email verification", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Registration failed
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void onAlreadyHaveAccountClick(View view) {
        // Handle the "Already have an account" button click
        Intent intent = new Intent(this, CustomerLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isPasswordSecure(String password) {
        if (password.length() < MIN_LENGTH) {
            return false;
        }

        // Check for upper and lower case characters
        if (password.equals(password.toLowerCase()) || password.equals(password.toUpperCase())) {
            return false;
        }

        // Check for digits and special characters
        if (!password.matches(".*\\d.*") || !SPECIAL_CHARACTER_PATTERN.matcher(password).find()) {
            return false;
        }

        // Check for repeated characters
        if (password.matches(".*(\\w)\\1{2,}.*")) {
            return false;
        }

        // Check for sequential characters
        if (password.matches("123|234|345|456|567|678|789|890")) {
            return false;
        }


        return true;
    }
}

