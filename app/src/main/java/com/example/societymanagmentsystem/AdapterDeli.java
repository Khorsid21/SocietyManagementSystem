package com.example.societymanagmentsystem;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterDeli extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Once", "Frequently"};

    public  AdapterDeli(FragmentManager fm , Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                fragment_deli_once fo = new fragment_deli_once();
                return fo;
            case 1:
                fragment_deli_freq ff = new fragment_deli_freq();
                return ff;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
