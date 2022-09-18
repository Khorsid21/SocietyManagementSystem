package com.example.societymanagmentsystem;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterManageMember extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Pending", "Accepted","Rejected"};

    public  AdapterManageMember(FragmentManager fm , Context context, int totalTabs){
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
                fragment_MM_Pending fo = new fragment_MM_Pending(context);
                return fo;
            case 1:
                fragment_MM_Accepted ff = new fragment_MM_Accepted(context);
                return ff;
            case 2:
                fragment_MM_Rejected fp = new fragment_MM_Rejected();
                return fp;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
