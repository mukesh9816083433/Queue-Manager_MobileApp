package com.example.queuemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Choose_Login extends AppCompatActivity {
 public Button btncustomer,btnbusiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_login);

        btncustomer=findViewById(R.id.btncustomer);
        btnbusiness=findViewById(R.id.btnbusiness);

        btncustomer.setOnClickListener(view -> {
            //going from one activity to another
            startActivity(new Intent(Choose_Login.this,Login_Customer.class));
        });

        btnbusiness.setOnClickListener(view -> {
            //going from one activity to another
            startActivity(new Intent(Choose_Login.this,Login_Business.class));
        });

    }
}