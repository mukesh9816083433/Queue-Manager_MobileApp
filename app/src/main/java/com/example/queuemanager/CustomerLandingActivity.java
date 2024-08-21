package com.example.queuemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerLandingActivity extends AppCompatActivity {

    private EditText businessCodeEditText;
    private Button enterQueueButton;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencem;

    boolean isValueEventListenerAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_landing);

        businessCodeEditText = findViewById(R.id.businessCodeEditText);
        enterQueueButton = findViewById(R.id.enterQueueButton);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReferencem = FirebaseDatabase.getInstance().getReference().child("CustomerUser").child(userId);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("BusinessUser");

        enterQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = businessCodeEditText.getText().toString();
                if(a.isEmpty()){
                    Toast.makeText(CustomerLandingActivity.this, "Please Enter the Business code", Toast.LENGTH_SHORT).show();
                }else {
                    int enteredBusinessCode = Integer.parseInt(businessCodeEditText.getText().toString().trim());
                    validateBusinessCode(enteredBusinessCode);

                }

            }
        });
    }

    private void validateBusinessCode(int businessCodee) {
        if (databaseReference != null) {
            databaseReference.orderByChild("businessCode").equalTo(businessCodee).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DataSnapshot businessSnapshot = dataSnapshot.getChildren().iterator().next();
                        int businesscode = businessSnapshot.child("businessCode").getValue(int.class);
                        int isQueueStarted = businessSnapshot.child("isQueueStarted").getValue(int.class);

                        if (businessCodee == businesscode) {
                            if (isQueueStarted == 1 && !isValueEventListenerAdded) {
                                if (databaseReferencem != null) {
                                    // Add the ValueEventListener only once
                                    isValueEventListenerAdded = true;

                                    databaseReferencem.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                            if (dataSnapshot1.exists()) {
                                                String name = dataSnapshot1.child("name").getValue(String.class);
                                                String number = dataSnapshot1.child("number").getValue(String.class);
                                                int has = dataSnapshot1.child("hasRequest").getValue(int.class);
                                                if (has == 0) {
                                                    // Storing the request in the database
                                                    databaseReferencem.child("requestToBusiness").setValue(businessCodee);
                                                    databaseReferencem.child("request").setValue(name);
                                                    databaseReferencem.child("hasRequest").setValue(1);

                                                    Intent intent = new Intent(CustomerLandingActivity.this, CustomerQueue.class);
                                                    startActivity(intent);
                                                    Toast.makeText(CustomerLandingActivity.this, "Your request has been sent to :"+businesscode, Toast.LENGTH_SHORT).show();

                                                    // Remove the ValueEventListener after request is sent
                                                    databaseReferencem.removeEventListener(this);

                                                } else {
                                                    Toast.makeText(CustomerLandingActivity.this, "You already have a pending request!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(CustomerLandingActivity.this, CustomerQueue.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(CustomerLandingActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(CustomerLandingActivity.this, "There is no Queue for this business.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(CustomerLandingActivity.this, "Wrong business Code.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CustomerLandingActivity.this, "Invalid business code.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CustomerLandingActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
