package com.example.societymanagmentsystem;

import android.content.Context;
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
import java.util.List;

public class fragment_MM_Accepted extends Fragment {

    RecyclerView mmAcceptRview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Fetchall> datalist;
    private static final String url = "https://vivek.ninja/fetch_all_A_30-3.php";
    SharedPreferences sharedPreferences;
    //Button Logout;
    public LoadingDialog loading;
    public static final String fileName = "USER_PROFILE";
    String city,society;
    Context context;

    public fragment_MM_Accepted(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_mm_accepted, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mmAcceptRview = view.findViewById(R.id.mmaccept_list_recyclerview);
        mSwipeRefreshLayout = view.findViewById(R.id.mmacceptswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        society = sharedPreferences.getString("society", "");
        city = sharedPreferences.getString("city", "");

        mmAcceptRview.setLayoutManager(new LinearLayoutManager(getContext()));

        /////
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<Fetchall>>() {
                }.getType();
                ArrayList<Fetchall> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                mmAcceptRview.setAdapter(new Adaptermmaccepted(getActivity().getApplicationContext(), getActivity(), datalist,city,society));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        /////

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
