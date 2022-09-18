package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class AllowCab extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Button bt1,bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_cab);

        tabLayout = findViewById(R.id.cab_tab_layout);

        viewPager = findViewById(R.id.cab_viewPager);
        //bt1 = findViewById(R.id.cab_once_btn);
        //bt2 = findViewById(R.id.cab_freq_btn);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       /* bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AllowCab.this, "Frequently Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        final AdapterC mm= new AdapterC(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(mm);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



    }
}