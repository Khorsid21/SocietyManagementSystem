package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ManageMembers extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_members);

        tabLayout = findViewById(R.id.manage_member_tab_layout);

        viewPager = findViewById(R.id.manage_member_viewPager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final AdapterManageMember mm= new AdapterManageMember(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(mm);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}