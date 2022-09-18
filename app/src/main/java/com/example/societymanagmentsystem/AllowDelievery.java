package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class AllowDelievery extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_delievery);


        tabLayout = findViewById(R.id.deli_tab_layout);

        viewPager = findViewById(R.id.deli_viewPager);
        //bt1 = findViewById(R.id.cab_once_btn);
        //bt2 = findViewById(R.id.cab_freq_btn);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final AdapterDeli mm= new AdapterDeli(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(mm);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}