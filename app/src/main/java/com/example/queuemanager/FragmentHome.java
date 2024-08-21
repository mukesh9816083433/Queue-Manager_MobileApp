package com.example.queuemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends Fragment {

    public interface OnStartQueueListener {
        void onStartQueue();
    }

    private OnStartQueueListener onStartQueueListener;

    private EditText queueTitleEditText, averageTimeEditText;
    private Button startQueueButton;
    private DatabaseReference userReferenceee;
    private DatabaseReference databaseReferencecc;


    private boolean isAttached = false; // Add this variable to track attachment status

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isAttached = true; // Fragment is attached to an activity
        if (context instanceof OnStartQueueListener) {
            onStartQueueListener = (OnStartQueueListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnStartQueueListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false; // Fragment is detached from the activity
        userReferenceee = null;
        databaseReferencecc = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");
        setHasOptionsMenu(true);
        // Initialize views
        queueTitleEditText = view.findViewById(R.id.queueTitleEditText);
        averageTimeEditText = view.findViewById(R.id.averageTimeEditText);
        startQueueButton = view.findViewById(R.id.startQueueButton);

        // Get the currently logged in user's ID
        FirebaseUser currentUserr = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUserr != null) {
            String userId = currentUserr.getUid();

            // Reference the user's data in the database
            userReferenceee = FirebaseDatabase.getInstance().getReference().child("BusinessUser").child(userId);
        }
        databaseReferencecc = FirebaseDatabase.getInstance().getReference().child("CustomerUser");


        // Set click listener for the start queue button
        //here when user clicks the button it checks whether the isQueueStartedButton is 1 or 0 to know if there is already a running queue
        //and if its 0 then it starts a queue with the given data and accepts request for entering the queue
        //and if its 1 meaning there is already a queue running we just show a toast message informing the user and jump to QueueFragment
        startQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queueTitle = queueTitleEditText.getText().toString().trim();
                String averageTime = averageTimeEditText.getText().toString().trim();
                if (queueTitle.isEmpty() || averageTime.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userReferenceee != null) {
                        userReferenceee.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Integer iss = dataSnapshot.child("isQueueStarted").getValue(Integer.class); // Use Integer instead of int
                                    if (iss != null) {
                                        int is = iss.intValue();

                                        if (is == 1) {
                                            // Queue has already been started
                                            if (isAttached) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "Queue has already been Started!", Toast.LENGTH_SHORT).show();
                                                        // Trigger the interface method to switch to FragmentQueue
                                                        onStartQueueListener.onStartQueue();
                                                    }
                                                });
                                            }
                                        } else if (is == 0) {
                                            // Update the queue title and average time in the user's data
                                            userReferenceee.child("queueTitle").setValue(queueTitle);
                                            userReferenceee.child("averageTime").setValue(averageTime);
                                            userReferenceee.child("isQueueStarted").setValue(1);

                                            userReferenceee.child("in").setValue(0);

                                            if (isAttached) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "Queue Started!", Toast.LENGTH_SHORT).show();
                                                        // Trigger the interface method to switch to FragmentQueue
                                                        onStartQueueListener.onStartQueue();
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        if (isAttached) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), "noooooooo!", Toast.LENGTH_SHORT).show();
                                                    // Trigger the interface method to switch to FragmentQueue
                                                    onStartQueueListener.onStartQueue();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    // User data doesn't exist, handle it accordingly
                                    // You can create the initial data structure if needed
                                    // In this case, you might want to set isQueueStarted to 0 initially.
                                    // userReference.child("isQueueStarted").setValue(0);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors
                                Toast.makeText(getContext(), "An error occurred, please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

//for deleting the data when queueends by monitoring the isQueueStarted field and if it changes to 0 it calls a function delete which deletes the requests{
//
//        if (userReferencee != null) {
//            userReferencee.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        int is = dataSnapshot.child("isQueueStarted").getValue(Integer.class); // Use Integer instead of int
//                        int businesscode = dataSnapshot.child("businessCode").getValue(Integer.class);
//                        if (is == 0) {
//                            delete(businesscode);
//
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle any errors
//                    Toast.makeText(getContext(), "An error occurred, please try again!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
        //}

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idd = item.getItemId();
        if (idd == R.id.logout) {
            Logout();
            return true;
        }
        if (idd == R.id.aboutus) {
            Aboutus();
            return true;
        }
        if (idd == R.id.summary) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
//            Summery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void Aboutus() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
