package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class showdata extends AppCompatActivity {

    //private static final String url = "https://adrenal-pairs.000webhostapp.com/fetch_all.php";
    private static final String url = "https://vivek.ninja/fetch_all.php";
    //private static final String DELETE_USER = "https://adrenal-pairs.000webhostapp.com/Admin/deluseradmin.php";
    private static final String DELETE_USER = "https://vivek.ninja/Admin/deluseradmin.php";
    RecyclerView recyclerView;
    List<Fetchall> datalist;
    public LoadingDialog loading;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);
        loading= new LoadingDialog(showdata.this);
        loading.startLoadingDialog();
        recyclerView = findViewById(R.id.recycleViewshow);

        mSwipeRefreshLayout = findViewById(R.id.AdmnManageMembersswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //movieList = new ArrayList<>();


        StringRequest sr = new StringRequest(POST, DELETE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismissDialog();
                Toast.makeText(showdata.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(showdata.this);
        rq.add(sr);

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                loading.dismissDialog();
            }
        }.start();


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
                recyclerView.setAdapter(new myadapter(getApplicationContext(), showdata.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(showdata.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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