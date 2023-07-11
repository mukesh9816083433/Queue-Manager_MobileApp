package com.example.queuemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_Business extends Activity {
    EditText edtbemail,edtbname,edtbpassword,edtbid,edtbaddress;
    Button btnbregister,btnblogin;

    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String role="Business";

    @Override
    //bundle class contains setting files /functions to run app
    protected void onCreate(Bundle b){
        //now we need to pass bundle to parent activity
        super.onCreate(b);
        setContentView(R.layout.register_business);//linking ui page to activity

        edtbemail=findViewById(R.id.edtbemail);
        edtbpassword=findViewById(R.id.edtbpassword);
        edtbname=findViewById(R.id.edtbname);
        edtbid=findViewById(R.id.edtbid);
        edtbaddress=findViewById(R.id.edtbaddress);

        btnbregister=findViewById(R.id.btnbregister);
        btnblogin=findViewById(R.id.btnblogin);

        btnbregister.setOnClickListener(view -> {

            String bemail=edtbemail.getText().toString();
            String bname=edtbname.getText().toString();
            String bbuid=edtbid.getText().toString();
            String baddress=edtbaddress.getText().toString();
            String password=edtbpassword.getText().toString();
            //making object of database to call db.register_business
//            Database db = new Database(getApplicationContext(),"queue_manager",null,1);

            databaseReference = firebaseDatabase.getInstance().getReference("Business");
            FAuth = FirebaseAuth.getInstance();

            if(bemail.length()==0 || password.length()==0 || bname.length()==0 || bbuid.length()==0 || baddress.length()==0){
                //Toast message it remains for just some sec
                Toast.makeText(getApplicationContext(),"Please fill all details", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(getApplicationContext(),"Registered successfully! Please verify your Email", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register_Business.this, Login_Business.class));

                FAuth.createUserWithEmailAndPassword(bemail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                            final HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("Role",role);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    HashMap<String , String> hashMap2 = new HashMap<>();
                                    hashMap2.put("Email",bemail);
                                    hashMap2.put("Password",password);
                                    hashMap2.put("Business_Name",bname);
                                    hashMap2.put("Business_Id",bbuid);
                                    hashMap2.put("Address",baddress);


                                    firebaseDatabase.getInstance().getReference("Business")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    FAuth.getCurrentUser().sendEmailVerification();

                                                }
                                            });
                                }
                            });
                        }

                    }
                });

//                int buid=Integer.parseInt(edtbid.getText().toString());
//                db.register_business(username,buname,buid,baddress,password);
//                Toast.makeText(getApplicationContext(),"Registered successfully", Toast.LENGTH_SHORT).show();
               //going to Login_Business activity when register is succesful and button is clicked

            }
        });

        btnblogin.setOnClickListener(view -> {
           //going to Login_busimess activity when Login button clicked
            startActivity(new Intent(Register_Business.this,Login_Business.class));
        });


    }
}
