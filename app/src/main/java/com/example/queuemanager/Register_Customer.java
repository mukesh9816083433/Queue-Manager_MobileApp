package com.example.queuemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Register_Customer extends Activity {
    EditText edtcemail,edtcnumber,edtcaddress,edtcpassword;
    Button btncregister,btnclogin;

    FirebaseAuth FAUth;
    DatabaseReference databaseReferencee;
    FirebaseDatabase firebaseDatabasee;

    String role="Customer";

    @Override

    protected void onCreate(Bundle b){

        super.onCreate(b);
        setContentView(R.layout.register_customer);


        edtcemail=findViewById(R.id.edtcemail);
        edtcpassword=findViewById(R.id.edtcpassword);
        edtcaddress=findViewById(R.id.edtcaddress);
        edtcnumber=findViewById(R.id.edtcnumber);

        btncregister=findViewById(R.id.btncregister);
        btnclogin=findViewById(R.id.btnclogin);

        databaseReferencee = firebaseDatabasee.getInstance().getReference("Customer");
        FAUth = FirebaseAuth.getInstance();

        btncregister.setOnClickListener(view -> {

            String cemail = edtcemail.getText().toString();
            String password = edtcpassword.getText().toString();
            String caddress = edtcaddress.getText().toString();
            String number =edtcaddress.getText().toString();

            //making object of database to call db.register_business
//            Database db = new Database(getApplicationContext(),"queue_manager",null,1);

            if(cemail.length()==0 || password.length()==0 || caddress.length()==0 ){
                //Toast message
                Toast.makeText(getApplicationContext(),"Please fill all details", Toast.LENGTH_SHORT).show();
            }else {
//                int nnumber=Integer.parseInt(edtcnumber.getText().toString());
//                db.register_customer(username,caddress,password,nnumber);

                FAUth.createUserWithEmailAndPassword(cemail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String useriddd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReferencee = FirebaseDatabase.getInstance().getReference("User").child(useriddd);
                            final HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("Role",role);

                            databaseReferencee.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    HashMap<String , String> hashMap1 = new HashMap<>();
                                    hashMap1.put("Email",cemail);
                                    hashMap1.put("Password",password);
                                    hashMap1.put("Address",caddress);
                                    hashMap1.put("Number",number);

                                    firebaseDatabasee.getInstance().getReference("Customer")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    FAUth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Toast.makeText(getApplicationContext(),"Registered successfully! Please verify your Email", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Register_Customer.this, Login_Customer.class));
                                                        }
                                                    });

                                                }
                                            });
                                }
                            });
                        }

                    }
                });


//                Toast.makeText(getApplicationContext(),"Registered successfully", Toast.LENGTH_SHORT).show();
                //jumping from register_customer to Login_Customer activity


            }
        });

        btnclogin.setOnClickListener(view -> {
            //jumping form one activity to another
            startActivity(new Intent(Register_Customer.this,Login_Customer.class));
        });


    }
}
