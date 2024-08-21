package com.example.queuemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CustomerQueue extends AppCompatActivity {

private int businesscode;
    private TextView currentqueue,myturn;

    private Button btnleave;

    DatabaseReference databaseReferenc;
    DatabaseReference databaseReferend;

    DatabaseReference databaseReferenceb;

    DatabaseReference dr;
    DatabaseReference drr;

    int a , b;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.customer_queue);

            currentqueue = findViewById(R.id.currentqueue);
            myturn = findViewById(R.id.myturn);
            btnleave = findViewById(R.id.btnleavee);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if (ContextCompat.checkSelfPermission(CustomerQueue.this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CustomerQueue.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                }

            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();

                // Reference the user's data in the database
                databaseReferenc = FirebaseDatabase.getInstance().getReference().child("CustomerUser").child(userId);
                databaseReferend = FirebaseDatabase.getInstance().getReference().child("CustomerUser").child(userId);
                dr= FirebaseDatabase.getInstance().getReference().child("CustomerUser").child(userId).child("hasRequest");
                drr= FirebaseDatabase.getInstance().getReference().child("CustomerUser").child(userId).child("in");
            }
            databaseReferenceb = FirebaseDatabase.getInstance().getReference().child("BusinessUser");

            btnleave.setOnClickListener(view ->{
                Intent intent = new Intent(this, CustomerLandingActivity.class);
                startActivity(intent);
                Toast.makeText(this, "You have left the queue and your request has been removed!", Toast.LENGTH_SHORT).show();

                databaseReferenc.child("request").setValue(null);
                databaseReferenc.child("requestToBusiness").setValue(null);
                databaseReferenc.child("hasRequest").setValue(0);
                databaseReferenc.child("in").setValue(0);

            });

                    if(dr!=null){
                        dr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int has = snapshot.getValue(int.class);
                                if(has==0){
                                    Intent intent = new Intent(CustomerQueue.this, CustomerLandingActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(CustomerQueue.this, "Your request is deleted!", Toast.LENGTH_SHORT).show();
                                    dr.removeEventListener(this);
                                }else{}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }




                    //these part is for going to customerLanding page when business ends the queue
                    if (databaseReferenc!= null) {
                        databaseReferenc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.exists()) {
                                    int businesscode = dataSnapshot1.child("requestToBusiness").getValue(int.class);


                                    if (databaseReferenceb != null) {

                                        Query query = databaseReferenceb.orderByChild("businessCode").equalTo(businesscode);
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // This code will be triggered whenever the data at the query location changes
                                                if (dataSnapshot.exists()) {
                                                    DataSnapshot businessSnapshot = dataSnapshot.getChildren().iterator().next();
                                                    Integer is = businessSnapshot.child("isQueueStarted").getValue(Integer.class);


                                                    if (is != null) {
                                                        int iss = is.intValue();
                                                        if (iss == 0) {
                                                            Intent intent = new Intent(CustomerQueue.this, CustomerLandingActivity.class);
                                                            startActivity(intent);
                                                            Toast.makeText(CustomerQueue.this, "Queue have been ended!", Toast.LENGTH_SHORT).show();

                                                            // Remove the ValueEventListener after request is sent
                                                            databaseReferenceb.removeEventListener(this);

                                                        } else {

                                                        }
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle any errors
                                                Toast.makeText(CustomerQueue.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(CustomerQueue.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    //nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn

            if (databaseReferend!= null) {
                databaseReferend.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()) {
                            int businesscode = dataSnapshot1.child("requestToBusiness").getValue(int.class);


                            if (databaseReferenceb != null) {

                                Query query = databaseReferenceb.orderByChild("businessCode").equalTo(businesscode);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        DataSnapshot businessSnapshot = dataSnapshot.getChildren().iterator().next();
                                        Integer in = businessSnapshot.child("in").getValue(Integer.class);


                                        if (in != null) {
                                            int innn = in.intValue();
                                            currentqueue.setText("Currently in:"+innn);
                                            b=innn;
                                            if(a==b&&b>0){
                                                makeNotification();
                                                showDialog();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle any errors
                                        Toast.makeText(CustomerQueue.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CustomerQueue.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });



            }



            //nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn


        }

    @Override
    public void onStart() {
        super.onStart();

        if (databaseReferenc != null) {

            databaseReferenc.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int in = snapshot.child("in").getValue(int.class);
                    a=in;
                    myturn.setText("My Turn:"+in);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if(drr!=null){
            drr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int inn = snapshot.getValue(int.class);
                    if(inn>0){
                        Toast.makeText(CustomerQueue.this, "Your request has been accepted!", Toast.LENGTH_SHORT).show();
                        drr.removeEventListener(this);
                    }else{}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





    }

    public void makeNotification(){
            String chanelID = "CHANNEL_ID_NOTIFICATION";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);

        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Alert!")
                .setContentText("Your turn has come")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), CustomerLandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("","");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);

            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Some Description", importance);

                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }

            notificationManager.notify(0,builder.build());


    }
    public void showDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder
                (CustomerQueue.this);
        builder.setTitle("Alert!");
        builder.setMessage("Your turn has come");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Okey",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReferenc.child("request").setValue(null);
                        databaseReferenc.child("requestToBusiness").setValue(null);
                        databaseReferenc.child("hasRequest").setValue(0);
                        databaseReferenc.child("in").setValue(0);
                        Intent intent = new Intent(CustomerQueue.this, CustomerLandingActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
