package com.example.queuemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterBusinessActivity extends AppCompatActivity {
    private EditText businessNameEditText, emailEditText, passwordEditText, addressEditText, businessCodeEditText, phoneNumberEditText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private static final int MIN_LENGTH = 8;
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);

        // Retrieve references to the UI elements
        businessNameEditText = findViewById(R.id.businessNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        addressEditText = findViewById(R.id.addressEditText);
        businessCodeEditText = findViewById(R.id.businessCodeEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        // Get a reference to the Firebase Realtime Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("BusinessUser");
    }

    public void onRegisterClick(View view) {
        String businessName = businessNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String businessCodee = businessCodeEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        int isQueueStarted=0;
        int total=0;

        if (businessName.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || businessCodee.isEmpty()|| phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else if (!isPasswordSecure(password)) {
            Toast.makeText(this, "Password is not secure", Toast.LENGTH_SHORT).show();
            return;
        }else{
            int businessCode = Integer.parseInt(businessCodee);
        // Create a new Business object with the user input
        Business business = new Business(businessName, email, address, businessCode, phoneNumber, isQueueStarted, total);

        // Save the Business object to the Firebase Realtime Database

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

                                            // Save the Business object to the "businessUsers" node
                                            databaseReference.child(user.getUid()).setValue(business)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Registration successful
                                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(this, BusinessLoginActivity.class);
                                                        startActivity(intent);

                                                        // ...
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



        public void onSwitchToLoginClick(View view) {
            // Handle the "Already have an account" button click
            Intent intent = new Intent(this, BusinessLoginActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the current activity to prevent going back to it with the back button
        }


    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Email verification sent

                            Toast.makeText(this, "yoooooooooo", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email verification sending failed
                            Toast.makeText(this, "Failed to send email verification", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
