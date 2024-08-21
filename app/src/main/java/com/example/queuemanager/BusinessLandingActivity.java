package com.example.queuemanager;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BusinessLandingActivity extends AppCompatActivity implements FragmentHome.OnStartQueueListener  {

    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_landing);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);



        loadFragment(new FragmentHome());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    loadFragment(new FragmentHome());
                    return true;
                case R.id.menu_requests:
                    loadFragment(new FragmentRequest());
                    return true;
                case R.id.menu_queue:
                    loadFragment(new FragmentQueue());
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onStartQueue() {
        loadFragment(new FragmentQueue());
        // Optionally, you can set the "Queue" item as checked in the BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.menu_queue);
    }

}
