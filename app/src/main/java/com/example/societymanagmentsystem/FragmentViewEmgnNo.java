package com.example.societymanagmentsystem;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbuonomo.creativeviewpager.CreativeViewPager;

public class FragmentViewEmgnNo extends Fragment {

    CreativeViewPager creativePager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_view_emgn_no,container,false);

        creativePager = view.findViewById(R.id.emgn_no_creativeViewPagerView);

        creativePager.setCreativeViewPagerAdapter(new NatureCreativePagerEmgnNoAdapter(getActivity().getApplicationContext()));

        return view;
    }
}