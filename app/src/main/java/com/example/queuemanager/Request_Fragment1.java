package com.example.queuemanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Request_Fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater in, ViewGroup grp, Bundle b){
        View view = in.inflate(R.layout.request_fragment,grp,false);
        return view;
    }
}