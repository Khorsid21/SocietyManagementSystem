package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

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

public class SecShowMembers extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    //Button Logout;

    public static final String fileName = "USER_PROFILE";
    String society , city;
    RecyclerView SecShowMemberRview;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<Fetchall> datalist;
    private static final String url = "https://vivek.ninja/MemberFetch/fetch_SecShowMembers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_show_members);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        society = sharedPreferences.getString("society", "");

        city = sharedPreferences.getString("city", "");

        SecShowMemberRview = findViewById(R.id.SecShowMember_list_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.SecShowMemberswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        SecShowMemberRview.setLayoutManager(new LinearLayoutManager(SecShowMembers.this));


        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
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
                SecShowMemberRview.setAdapter(new AdapterShowMembers(getApplicationContext(), SecShowMembers.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SecShowMembers.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                //map.put("childnamekey",parentName.getText().toString());
                // map.put("timeper",selTime.getText().toString());
                map.put("sockey", society);
                map.put("citykey", city);
                return map;
            }
        } ;

        RequestQueue queue = Volley.newRequestQueue(SecShowMembers.this);
        queue.add(request);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(SecShowMembers.this);
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}