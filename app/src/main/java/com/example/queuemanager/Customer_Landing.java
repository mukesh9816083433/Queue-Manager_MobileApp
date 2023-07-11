package com.example.queuemanager;

import android.app.Activity;
import android.os.Bundle;

public class Customer_Landing extends Activity {

    @Override
    //bundle class contains setting files /functions to run app
    protected void onCreate(Bundle b){
        //now we need to pass nundle to parent activity
        super.onCreate(b);
        setContentView(R.layout.customer_landing);//linking ui page to activity
    }
}
