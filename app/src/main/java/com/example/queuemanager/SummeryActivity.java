package com.example.queuemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class SummeryActivity extends AppCompatActivity {

    private EditText data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);

        data = findViewById(R.id.queue);


    }
}