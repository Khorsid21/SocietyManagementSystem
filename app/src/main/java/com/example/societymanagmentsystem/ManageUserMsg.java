package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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

public class ManageUserMsg extends AppCompatActivity {

    RecyclerView UserMsgRview;
    String society,city;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<UserMsgData> datalist;
    private static final String url = "https://vivek.ninja/SecMsg/fetch_usermsg.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_msg);

        Intent intent = getIntent();

        society = intent.getStringExtra("society");
        city = intent.getStringExtra("city");

       // Toast.makeText(this, society+"EE"+block, Toast.LENGTH_SHORT).show();

        UserMsgRview = findViewById(R.id.MUM_list_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.MUMswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        UserMsgRview.setLayoutManager(new LinearLayoutManager(ManageUserMsg.this));

        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<UserMsgData>>() {
                }.getType();
                ArrayList<UserMsgData> datalist = gson.fromJson(response, userListType);

                UserMsgRview.setAdapter(new AdapterUserMsg(getApplicationContext(), ManageUserMsg.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManageUserMsg.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("sockey", society);
                map.put("citykey", city);
                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(ManageUserMsg.this);
        queue.add(request);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(ManageUserMsg.this);
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}