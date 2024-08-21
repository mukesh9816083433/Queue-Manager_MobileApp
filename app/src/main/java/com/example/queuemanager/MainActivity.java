package com.example.queuemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBusinessLoginClick(View view) {
        Intent intent = new Intent(this, BusinessLoginActivity.class);
        startActivity(intent);
    }

    public void onCustomerLoginClick(View view) {
        Intent intent = new Intent(this, CustomerLoginActivity.class);
        startActivity(intent);
    }

}