package com.example.queuemanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Business_Landing extends Activity {
    private Button btnhome, btnrequest, btnqueue;
    private FragmentManager fragmentManager;

    @Override
    //bundle class contains setting files /functions to run app
    protected void onCreate(Bundle b){
        //now we need to pass nundle to parent activity
        super.onCreate(b);
        setContentView(R.layout.business_landing);//linking ui page to activity
        getFragmentManager().beginTransaction().replace(R.id.fragment,new Home_Fragment1()).commit();

        Intent in = getIntent();
        String data= in.getStringExtra("id");

        btnhome = findViewById(R.id.btnhome);
        btnrequest = findViewById(R.id.btnrequest);
        btnqueue = findViewById(R.id.btnqueue);


        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getFragmentManager().beginTransaction().replace(R.id.fragment,new Home_Fragment1()).commit();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment,homeFragment);
//                transaction.commit();
            }
        });

        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.fragment,new Request_Fragment1()).commit();
            }
        });

        btnqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.fragment,new Queue_Fragment1()).commit();
            }
        });

        // Load the initial fragment
//        loadFragment(homeFragment);
    }


}
