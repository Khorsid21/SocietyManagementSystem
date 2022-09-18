package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class SecShowDeli extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_show_deli);

        tabLayout = findViewById(R.id.sec_deli_tab_layout);

        viewPager = findViewById(R.id.sec_deli_viewPager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final AdapterSecShowDeli mm= new AdapterSecShowDeli(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(mm);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}