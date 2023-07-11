package com.example.queuemanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Home_Fragment1 extends Fragment {


    FirebaseAuth FAUTh;

    DatabaseReference databaseReferenceee;

    FirebaseDatabase firebaseDatabaseee;

    Button btnstart;
    EditText edtqueuetitle, edtavgtime ;


 String data;
 String role="Business";

    @Override
    public View onCreateView(LayoutInflater in, ViewGroup grp, Bundle b){
        View view = in.inflate(R.layout.home_fragment1,grp,false);



        btnstart = view.findViewById(R.id.btnstart);
        edtqueuetitle = view.findViewById(R.id.edtqueuetitle);
        edtavgtime= view.findViewById(R.id.edtavgtime);


        databaseReferenceee = firebaseDatabaseee.getInstance().getReference("Customer");
        FAUTh = FirebaseAuth.getInstance();



        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String queuetitle = edtqueuetitle.getText().toString();
                String avgtime = edtavgtime.getText().toString();

                if(queuetitle.length()==0 || avgtime.length()==0 ){
                    getFragmentManager().beginTransaction().replace(R.id.fragment,new Home_Fragment1()).commit();
                    //Toast message
//                    Toast.makeText(getApplicationContext(),"Please fill all details", Toast.LENGTH_SHORT).show();
                }else {

                    String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReferenceee = FirebaseDatabase.getInstance().getReference("AboutQueue").child(useridd);
                    final HashMap<String, String> hashMap3 = new HashMap<>();
                    hashMap3.put("Role", role);
                    hashMap3.put("QueueTitle", queuetitle);
                    hashMap3.put("AverageTime", avgtime);

                    firebaseDatabaseee.getInstance().getReference("Business")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(hashMap3);
                    getFragmentManager().beginTransaction().replace(R.id.fragment,new Home_Fragment2()).commit();

                }




//                Intent in = new Intent(Home_Fragment1.this,Business_Landing.class);
//                in.putExtra("id", data);
//                startActivity(in);

//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment,homeFragment);
//                transaction.commit();
            }

        });



        return view;
    }
}
