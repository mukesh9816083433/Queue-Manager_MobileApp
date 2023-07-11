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
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Customer extends Activity {
    EditText emailcl,passwordcl;
    Button btnregister,btnlogin, btnforget;

    FirebaseAuth Fauth;
    @Override
    //bundle class contains setting files /functions to run app
    protected void onCreate(Bundle b){
        //now we need to pass nundle to parent activity
        super.onCreate(b);
        setContentView(R.layout.login_customer);//linking ui page to activity


        try {
            emailcl = findViewById(R.id.emailcl);
            passwordcl = findViewById(R.id.passwordcl);

            btnregister = findViewById(R.id.btnregister);
            btnlogin = findViewById(R.id.btnlogin);
            btnforget = findViewById(R.id.btnfp);

            Fauth = FirebaseAuth.getInstance();


            btnlogin.setOnClickListener(view -> {

                String emailid = emailcl.getText().toString();
                String pwd = passwordcl.getText().toString();

//            Database db = new Database(getApplicationContext(),"queue_manager",null,1);

                if (emailid.length() == 0 || pwd.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {


                    Fauth.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if (Fauth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login_Customer.this, Customer_Landing.class));
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
            });

            btnregister.setOnClickListener(view -> {
                startActivity(new Intent(Login_Customer.this, Register_Customer.class));
            });
        }catch (Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
    }
}
