package com.example.societymanagmentsystem;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tbuonomo.creativeviewpager.CreativeViewPager;

public class BlankFragment extends Fragment {
    CreativeViewPager creativePager;
    NatureCreativePagerAdapter nv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_blank,container,false);

        creativePager = view.findViewById(R.id.creativeViewPagerView);

        creativePager.setCreativeViewPagerAdapter(new NatureCreativePagerAdapter(getActivity().getApplicationContext()));
        return view;
    }
}