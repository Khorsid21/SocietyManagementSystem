package com.example.societymanagmentsystem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.content.Context;
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
import com.ramotion.foldingcell.FoldingCell;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class Workers_List extends AppCompatActivity {

    RecyclerView workerslist_recyclerview;
    String fwdtypeWorker,city;
    FoldingCell toggleBtn;
    List<workerdata> datalist;
    FoldingCell fc;
    String workerNum;
    int REQUEST_CALL = 1;
    private static final String url = "https://vivek.ninja/worker/worker_fetch.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers__list);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        fwdtypeWorker = intent.getStringExtra("workerType");
        city = intent.getStringExtra("city").toLowerCase();
        //Toast.makeText(this, fwdtypeWorker, Toast.LENGTH_SHORT).show();

        workerslist_recyclerview = findViewById(R.id.workers_list_recyclerview);

        workerslist_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        //workerslist_recyclerview.setAdapter(new myadapter(getApplicationContext(), Workers_List.this, datalist));\

        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //Toast.makeText(Workers_List.this, "Hiiiiiii"+response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<workerdata>>() {
                }.getType();
                ArrayList<workerdata> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                workerslist_recyclerview.setAdapter(new workeradapter(getApplicationContext(), Workers_List.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Workers_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("typeUser",fwdtypeWorker);
                map.put("city",city);
                return map;
            }
        };

         RequestQueue queue = Volley.newRequestQueue(this);
          queue.add(request);



    }


}