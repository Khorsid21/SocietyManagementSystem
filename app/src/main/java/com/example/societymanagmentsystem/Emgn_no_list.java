package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Emgn_no_list extends AppCompatActivity {

    RecyclerView emgnnolist_recyclerview;
    String fwdtypeWorker,city;
    FoldingCell toggleBtn;
    List<Emgnnodata> datalist;
    FoldingCell fc;
    String workerNum;
    int REQUEST_CALL = 1;
    private static final String url = "https://vivek.ninja/emgn/emgn_fetch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emgn_no_list);


        Intent intent = getIntent();
        fwdtypeWorker = intent.getStringExtra("Type");
        city = intent.getStringExtra("city").toLowerCase();
        Log.e("AD",fwdtypeWorker);
        Toast.makeText(this, fwdtypeWorker, Toast.LENGTH_SHORT).show();

        emgnnolist_recyclerview = findViewById(R.id.emgn_no_list_recyclerview);

        emgnnolist_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //Toast.makeText(Workers_List.this, "Hiiiiiii"+response, Toast.LENGTH_SHORT).show();
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
                emgnnolist_recyclerview.setAdapter(new emgnadapter(getApplicationContext(), Emgn_no_list.this, datalist,fwdtypeWorker));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Emgn_no_list.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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