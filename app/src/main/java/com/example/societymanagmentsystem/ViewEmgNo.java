package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ViewEmgNo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emg_no);

        FragmentViewEmgnNo sdsds=new FragmentViewEmgnNo();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.view_emgn_no_fragment_container,sdsds);
        //fragmentTransaction.add(R.id.view_emgn_no_fragment_container,sdsds);
        //fragmentTransaction.add(R.id.fragment_container,,"SDSD");
        fragmentTransaction.commit();
    }
}