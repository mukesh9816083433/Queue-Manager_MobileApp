package com.example.queuemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Business extends Activity {
    Button btnregister,btnlogin;
    EditText edtbemail,edtbpwd;

    FirebaseAuth FAUth;

    @Override
    //bundle class contains setting files /functions to run app
    protected void onCreate(Bundle b){
        //now we need to pass nundle to parent activity
        super.onCreate(b);
        setContentView(R.layout.login_business);//linking ui page to activity

        edtbemail=findViewById(R.id.edtbemail);
        edtbpwd=findViewById(R.id.edtbpwd);

        btnregister=findViewById(R.id.btnregister);
        btnlogin=findViewById(R.id.btnlogin);

        FAUth = FirebaseAuth.getInstance();

        btnregister.setOnClickListener(view -> {

            startActivity(new Intent(Login_Business.this,Register_Business.class));
        });

        btnlogin.setOnClickListener(view -> {

            String bemailid = edtbemail.getText().toString();
            String bpwd = edtbpwd.getText().toString();

//            Database db = new Database(getApplicationContext(),"queue_manager",null,1);

            if (bemailid.length() == 0 || bpwd.length() == 0) {

                Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {


                FAUth.signInWithEmailAndPassword(bemailid, bpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if (FAUth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login_Business.this, Business_Landing.class));
                            }else{
                                Toast.makeText(getApplicationContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                //checking if username and password is valid or not by calling login function we have created in Database.java
//                if (db.clogin(email, password) == 1) {
//                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//
//                    //to save username or our data with key and value to use it in other activity
//                    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("username", email);
//                    editor.apply();


            }









//            String username=edtuname.getText().toString();
//            String password=edtpassword.getText().toString();
//            //object of Database to call db.login
//            Database db = new Database(getApplicationContext(),"queue_manager",null,1);
//
//            if(username.length()==0 || password.length()==0){
//                Toast.makeText(getApplicationContext(),"Please fill all details", Toast.LENGTH_SHORT).show();
//               } else {
//                //checking if username and password is valid or not by calling login function we have created in Database.java
//                    if(db.login(username,password)==1){
//                      Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
//
//                    //to save username or our data with key and value to use it in other activity
////                      SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
////                      SharedPreferences.Editor editor = sharedPreferences.edit();
////                      editor.putString("username",username);
////                      editor.apply();
//
//                      startActivity( new Intent(Login_Business.this, Business_Landing.class));
//                    }else {
//                      Toast.makeText(getApplicationContext(),"Invalid Username and Password", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
        });

    }
}
