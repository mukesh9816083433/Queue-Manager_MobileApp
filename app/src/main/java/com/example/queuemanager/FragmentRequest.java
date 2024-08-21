package com.example.queuemanager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentRequest extends Fragment implements RequestAdapter.RequestButtonClickListener {

    private ListView listView;
    private RequestAdapter adapter;
    private DatabaseReference databaseReferencer;
    private DatabaseReference databaseReferencec;


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
        databaseReferencer = null;
        databaseReferencec=null;
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_requests, container, false);
            getActivity().setTitle("Requests");
            listView = view.findViewById(R.id.listView);

            EditText searchIndexEditText = view.findViewById(R.id.searchIndexEditText);
            Button searchButton = view.findViewById(R.id.searchButton);

            // Adding a click listener to the search button
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchIndex = searchIndexEditText.getText().toString();
                    if (!searchIndex.isEmpty()) {
                        binarySearchRequest(Integer.parseInt(searchIndex)); // Calling the search method
                    }
                }
            });

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                databaseReferencer = FirebaseDatabase.getInstance().getReference().child("BusinessUser").child(userId);
            }
            databaseReferencec = FirebaseDatabase.getInstance().getReference().child("CustomerUser");

            // Initialize the adapter
            adapter = new RequestAdapter(getContext(), new ArrayList<>(), this);

            // Set the adapter for the ListView
            listView.setAdapter(adapter);


            //getting the business code of the business to abstract request from database stored in customerUser with businessCode of the business
            if (databaseReferencer != null) {
                databaseReferencer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Integer businesscode = dataSnapshot.child("businessCode").getValue(Integer.class);

                                Integer is = dataSnapshot.child("isQueueStarted").getValue(Integer.class);
                            if(is!=null) {
                                int iss = is.intValue();
                                if (iss == 1) {
                                    //for extracting data and request from customer field and making list of request
                                    onRequestSent(businesscode);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                        Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Handle list item clicks  here
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = adapter.getItem(position);

                    // Handle the item click (accept and delete)
                    // For this example, we'll show a toast message
                    Toast.makeText(getContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            });



            return view;
        }


    // When Accept button is clicked in request
        @Override
            public void onAcceptButtonClick(int position, String re, int c) {
                // Handle the "Accept" button click for the item at the specified position
                String reque = adapter.getItem(position);



    //for increasing the value of total customer by 1 every time a request is accepted{
            if (databaseReferencec != null) {
                databaseReferencer.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        // Retrieve the current value
                        Integer total = mutableData.child("total").getValue(Integer.class);

                        if (total == 0) {
                            // If total is null, set it to 1
                            mutableData.child("total").setValue(1);

                        } else {
                            // Increment the value by 1
                            mutableData.child("total").setValue(total + 1);
                            a=total+1;
                        }

                        // Set the data back to the database
                        databaseReferencer.setValue(mutableData.getValue());

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                        if (databaseError != null) {
                            // Handle error
                        } else {
                            // Transaction completed successfully
                        }
                    }
                });
            }




            //for changing value of different child of customeruser who have send request to business when ACCEPTED {
                    if (databaseReferencec != null) {
                         databaseReferencec.orderByChild("name").equalTo(re).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {

                                if (dataSnapshott.exists()) {
                                    for (DataSnapshot customerSnapshot : dataSnapshott.getChildren()) {
                                        String customerId = customerSnapshot.getKey(); // Get the customer's ID
                                        if (customerId != null) {
                                            DatabaseReference customerRef = databaseReferencec.child(customerId);

                                            customerRef.child("in").setValue(a);
                                            customerRef.child("request").setValue(null);
    //                                        customerRef.child("requestToBusiness").setValue(null);

                                            adapter.remove(reque);
                                            adapter.notifyDataSetChanged();

                                            if (isAttached) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(),  reque+" goes to "+a, Toast.LENGTH_SHORT).show();
                                                        // Trigger the interface method to switch to FragmentQueue
                                                    }
                                                });
                                            }

                                        } else {
                                        }
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





        //WHEN REQUEST IS DELETED................{
        @Override
            public void onDeleteButtonClick(int position,String request) {
                // Handle the "Delete" button click for the item at the specified position
                String requestt = adapter.getItem(position);

                // Remove the request and Notify the adapter that the dataset has changed
                adapter.remove(requestt);
                adapter.notifyDataSetChanged();



                        if (databaseReferencer != null) {
                            //getting the business code of the business to abstract request from database stored in customerUser with businessCode of the business
                            databaseReferencer.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Integer businesscode = dataSnapshot.child("businessCode").getValue(Integer.class); // Use Integer.class
                                    //CALLIMG DELETE METHOD WHICH deletes request and changing different fields of cutomeruser
                                        delete(businesscode,request);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors
                                    Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


            }




            public void onRequestSent(int businesscode) {
            //here we are abstracting request from customer database
            if (databaseReferencec != null) {
                databaseReferencec.orderByChild("requestToBusiness").equalTo(businesscode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<String> requests = new ArrayList<>();

                            for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                                String request = customerSnapshot.child("request").getValue(String.class);
                                if(request!=null) {
                                    requests.add(request);
                                }
                            }

                            // Update the adapter with the list of requests
                            adapter.addAll(requests);
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




            public void delete(int businesscode, String reqe){
                if (databaseReferencec != null) {
                    databaseReferencec.orderByChild("name").equalTo(reqe).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                            if (dataSnapshott.exists()) {
                                for (DataSnapshot customerSnapshot : dataSnapshott.getChildren()) {
                                    String customerId = customerSnapshot.getKey(); // Get the customer's ID
                                    if (customerId != null) {
                                        DatabaseReference customerRef = databaseReferencec.child(customerId);
                                       customerRef.child("request").setValue(null);
                                        customerRef.child("requestToBusiness").setValue(null);
                                        customerRef.child("hasRequest").setValue(0);
                                        if (isAttached) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), " Request deleted!", Toast.LENGTH_SHORT).show();
                                                    // Trigger the interface method to switch to FragmentQueue
                                                }
                                            });
                                        }

                                    } else {
                                    }
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
    private void binarySearchRequest(int index) {
        int low = 0;
        int high = adapter.getCount() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (mid == index - 1) {
                String selectedRequest = adapter.getItem(mid);
                if (selectedRequest != null) {
                    Toast.makeText(getContext(), "Request at index " + index + ": " + selectedRequest, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (mid < index - 1) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        Toast.makeText(getContext(), "Request not found at index " + index, Toast.LENGTH_SHORT).show();
    }

}
