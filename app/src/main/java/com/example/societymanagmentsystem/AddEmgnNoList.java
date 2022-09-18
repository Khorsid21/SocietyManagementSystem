package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddEmgnNoList extends AppCompatActivity {

    RecyclerView emgnno_list_recyclerview;
    String typeWorker;
    FoldingCell toggleBtn;
    List<Emgnnodata> datalist;
    FoldingCell fc;
    String workerNum;
    int REQUEST_CALL = 1;
    FloatingActionButton Add_EmgnNo_btn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String url = "https://vivek.ninja/emgn/admn_emgnno_fetch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emgn_no_list);

        emgnno_list_recyclerview = findViewById(R.id.Add_EmgnNO_list_recyclerview);
        Add_EmgnNo_btn = findViewById(R.id.addEmgnNO);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.EmgnNoswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);
        Add_EmgnNo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AddEmgnNoList.this,AddEmgnNo.class);
                startActivity(i);

                //Toast.makeText(AddEmgnNoList.this, "Add EmgnNo Activated", Toast.LENGTH_SHORT).show();
            }
        });

        emgnno_list_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<Emgnnodata>>() {
                }.getType();
                ArrayList<Emgnnodata> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                emgnno_list_recyclerview.setAdapter(new addemgnnoadapter(getApplicationContext(), AddEmgnNoList.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddEmgnNoList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}