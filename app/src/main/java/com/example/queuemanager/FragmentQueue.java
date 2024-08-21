package com.example.queuemanager;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentQueue extends Fragment {

    private String textInView;
    private TextView queueTitleTextView, averageTimeTextView, next, totall;
    private Button endQueueButton, finish;
    private DatabaseReference userReference;
    private DatabaseReference databaseReferencecc;
//    private DatabaseReference customerRef;
  int a=1;
    private boolean isAttached = false; // Add this variable to track attachment status

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isAttached = true; // Fragment is attached to an activity
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false; // Fragment is detached from the activity
        userReference = null;
        databaseReferencecc = null;
//        customerRef = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue, container, false);
        getActivity().setTitle("Queue");

        // Initialize views
        queueTitleTextView = view.findViewById(R.id.queueTitleTextView);
        averageTimeTextView = view.findViewById(R.id.averageTimeTextView);
        endQueueButton = view.findViewById(R.id.endQueueButton);
        next=view.findViewById(R.id.next);
        finish=view.findViewById(R.id.finish);
        totall=view.findViewById(R.id.total);


        if (savedInstanceState != null) {
            textInView = savedInstanceState.getString("textInView");
            next.setText(textInView);
        }


        // Get the currently logged in user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference the user's data in the database
            userReference = FirebaseDatabase.getInstance().getReference().child("BusinessUser").child(userId);
        }

        databaseReferencecc = FirebaseDatabase.getInstance().getReference().child("CustomerUser");

        // Set click listener for the end queue button
        endQueueButton.setOnClickListener(view1 -> {
                // Clear the queue title and average time in the user's data
                if (userReference != null) {
                    userReference.child("queueTitle").setValue(null);
                    userReference.child("averageTime").setValue(null);
                    userReference.child("isQueueStarted").setValue(0);
                    userReference.child("total").setValue(0);
                    userReference.child("in").setValue(0);
                    // Toast a message to confirm the queue has ended
                    Toast.makeText(getActivity(), "Queue Ended!", Toast.LENGTH_SHORT).show();

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int is = dataSnapshot.child("isQueueStarted").getValue(Integer.class); // Use Integer instead of int
                                int businesscode = dataSnapshot.child("businessCode").getValue(Integer.class);
                                if (is == 0) {
                                    delete(businesscode);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                            Toast.makeText(getContext(), "An error occurred, please try again!", Toast.LENGTH_SHORT).show();
                        }

                    });

                }

        });


        //Trying to do the work

            finish.setOnClickListener(view1 -> {

                if (userReference != null) {

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int businesscode = dataSnapshot.child("businessCode").getValue(Integer.class);
                                int total = dataSnapshot.child("total").getValue(Integer.class);
                                int inn = dataSnapshot.child("in").getValue(Integer.class);
                                    show(businesscode,total,inn);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors
                            Toast.makeText(getContext(), "An error occurred, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            });










        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textInView", textInView);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (userReference != null) {
            // Listen for changes in the user's data
            userReference.addValueEventListener(queueValueEventListener);
        }
    }


    private ValueEventListener queueValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                // Retrieve the user's data
                String queueTitle = dataSnapshot.child("queueTitle").getValue(String.class);
                String averageTime = dataSnapshot.child("averageTime").getValue(String.class);
                Integer tota=dataSnapshot.child("total").getValue(Integer.class);
                // Update the views with the retrieved data
                queueTitleTextView.setText("QueueTitle: "+queueTitle);
                averageTimeTextView.setText("AverageTime/Client: "+averageTime);
                totall.setText("Total no: "+tota);
                // Retrieve the user's data


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Handle any errors
            Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
    };


    public void delete(int businesscode){
        if (databaseReferencecc != null) {
            databaseReferencecc.orderByChild("requestToBusiness").equalTo(businesscode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                            String customerId = customerSnapshot.getKey(); // Get the customer's ID
                            if (customerId != null) {
                                DatabaseReference customerRef = databaseReferencecc.child(customerId);

                                // Set the 'request' field to null for this customer
                                customerRef.child("request").setValue(null);
                                customerRef.child("requestToBusiness").setValue(null);
                                customerRef.child("hasRequest").setValue(0);
                                customerRef.child("in").setValue(0);
                                next.setText("No queue");
                                if (isAttached) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "All pending request deleted!", Toast.LENGTH_SHORT).show();
                                            // Trigger the interface method to switch to FragmentQueue
                                        }
                                    });
                                }
                                databaseReferencecc.removeEventListener(this);

                            }else{}
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "An Error occurred. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


    public void show(int businesscodee, int total, int innn){
        if (databaseReferencecc != null) {
            databaseReferencecc.orderByChild("requestToBusiness").equalTo(businesscodee).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {



                        if (databaseReferencecc != null) {
                            databaseReferencecc.orderByChild("in").equalTo(a).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                                            String customerId = customerSnapshot.getKey(); // Get the customer's ID
                                            if (customerId != null) {
                                                DatabaseReference customerRef = databaseReferencecc.child(customerId);
                                                String nam = customerSnapshot.child("name").getValue(String.class);
                                                String num = customerSnapshot.child("number").getValue(String.class);
                                                int in = customerSnapshot.child("in").getValue(Integer.class);
                                                userReference.child("in").setValue(in);

                                                if(in==a) {
                                                    next.setText("current no: "+in+"\n"+nam+"\n"+num);


                                                    databaseReferencecc.removeEventListener(this);



                                                }
                                                a=a+1;

                                            } else {}
                                        }



                                    }else{
                                        if(a>total) {
                                            if (isAttached) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "No one in the queue " , Toast.LENGTH_SHORT).show();
                                                        // Trigger the interface method to switch to FragmentQueue
                                                    }
                                                });
                                            }
                                            databaseReferencecc.removeEventListener(this);
                                        }else {
//                                            for(int i=a;i<=innn;i++){
//
//
//                                                if (databaseReferencecc != null) {
//                                                    databaseReferencecc.orderByChild("in").equalTo(i).addValueEventListener(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                            if (dataSnapshot.exists()) {
//                                                                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
//                                                                    String customerId = customerSnapshot.getKey(); // Get the customer's ID
//                                                                    if (customerId != null) {
//                                                                        DatabaseReference customerRef = databaseReferencecc.child(customerId);
//                                                                        String nam = customerSnapshot.child("name").getValue(String.class);
//                                                                        String num = customerSnapshot.child("number").getValue(String.class);
//                                                                        int in = customerSnapshot.child("in").getValue(Integer.class);
////                                                                        userReference.child("in").setValue(in);
//
//                                                                            next.setText("current no: "+in+"\n"+nam+"\n"+num);
//
////                                                                            if (isAttached) {
////                                                                                getActivity().runOnUiThread(new Runnable() {
////                                                                                    @Override
////                                                                                    public void run() {
////                                                                                        Toast.makeText(getContext(), "mk" , Toast.LENGTH_SHORT).show();
////                                                                                        // Trigger the interface method to switch to FragmentQueue
////                                                                                    }
////                                                                                });
////                                                                            }
//                                                                            databaseReferencecc.removeEventListener(this);
//
//
//
////                                                                        a=a+1;
//
//                                                                    } else {}
//                                                                }
//
//
//
//                                                            }else{
//
//                                                                    a = a + 1;
//                                                                    databaseReferencecc.removeEventListener(this);
//
//                                                                }
//                                                            }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                            // Handle any errors
//                                                            getActivity().runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    Toast.makeText(getContext(), "An Error Occurred. Please try again!", Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            });
//                                                        }
//                                                    });
//                                                }
//
//
//
//
//
//
//                                            }

                                            if (isAttached) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "nooo one at " + a, Toast.LENGTH_SHORT).show();

                                                        // Trigger the interface method to switch to FragmentQueue
                                                    }
                                                });
                                            }
                                            a=a+1;
                                            databaseReferencecc.removeEventListener(this);


                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "An Error Occurred. Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }






                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "An Error Occurred. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
