package com.example.societymanagmentsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tbuonomo.creativeviewpager.CreativeViewPager;

public class temp extends Fragment {

    CreativeViewPager creativePager;
    NatureCreativePagerAdapter nv;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_temp,container,false);

        creativePager = view.findViewById(R.id.creativeViewPagerView);
        creativePager.setCreativeViewPagerAdapter(new NatureCreativePagerAdapter(getActivity().getApplicationContext()));
        return view;
    }
}