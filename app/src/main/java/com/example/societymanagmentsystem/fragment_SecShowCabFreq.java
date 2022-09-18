package com.example.societymanagmentsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class fragment_SecShowCabFreq extends Fragment {

    RecyclerView SecShowCabFreqRview;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<SecShowFreqData> datalist;
    private static final String url = "https://vivek.ninja/AllowCab/fetch_cab_freq.php";
    SharedPreferences sharedPreferences;
    //Button Logout;
    Context context;
    String city,society;
    public static final String fileName = "USER_PROFILE";

    public fragment_SecShowCabFreq(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_sec_show_cab_freq, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SecShowCabFreqRview = view.findViewById(R.id.SecShowCabFreq_list_recyclerview);
        mSwipeRefreshLayout = view.findViewById(R.id.SecFswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        society = sharedPreferences.getString("society", "");
        city = sharedPreferences.getString("city", "");

        SecShowCabFreqRview.setLayoutManager(new LinearLayoutManager(getContext()));



        ///

        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<SecShowFreqData>>() {
                }.getType();
                ArrayList<SecShowFreqData> datalist = gson.fromJson(response, userListType);


                SecShowCabFreqRview.setAdapter(new AdapterSecCabFreq(getActivity().getApplicationContext(), getActivity(), datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("city", city);
                map.put("society", society);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);


        ////

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });





    }
}
