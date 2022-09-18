package com.example.societymanagmentsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class ShowSocietyNotice extends AppCompatActivity {

    RecyclerView SocNoticeRview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<SocNoticeData> datalist;
    String userrole;
    String url = "https://vivek.ninja/Soc_Notice/fetch_society_notice.php";
    String url_final;
    String url1 = "https://vivek.ninja/Soc_Notice/fetch_society_notice_member.php";
    String url2 = "https://vivek.ninja/Soc_Notice/fetch_society_notice_sec.php";

    SharedPreferences sharedPreferences;
    //Button Logout;
    String city,society;
    public static final String fileName = "USER_PROFILE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_society_notice);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        society = sharedPreferences.getString("society", "");
        city = sharedPreferences.getString("city", "");

        Intent i = getIntent();
        userrole = i.getStringExtra("role");

        if(userrole.equalsIgnoreCase("admin")){
            url_final=url;
        }
        else if(userrole.equalsIgnoreCase("Society Member")){
            url_final=url1;
        }
        else if(userrole.equalsIgnoreCase("security")){
            url_final=url2;
        }

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_NETWORK_STATE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        SocNoticeRview = findViewById(R.id.shownotice_list_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.shownoticeswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        SocNoticeRview.setLayoutManager(new LinearLayoutManager(ShowSocietyNotice.this));

        /////
        StringRequest request = new StringRequest(POST,url_final, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<SocNoticeData>>() {
                }.getType();
                ArrayList<SocNoticeData> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                SocNoticeRview.setAdapter(new AdapterSocNotice(ShowSocietyNotice.this, ShowSocietyNotice.this,userrole, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowSocietyNotice.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                //String path = getPath(filepath);
                //map.put("namekey",name.getText().toString().trim());
                map.put("city", city);
                map.put("society", society);
                //map.put("role",rb.getText().toString());
                //map.put("image",encodeBitmapImage(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ShowSocietyNotice.this);
        queue.add(request);
        /////

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(ShowSocietyNotice.this);
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        AdapterSocNotice adp = new AdapterSocNotice();
        //if(adp !=null){
            adp.onActivityResult(requestCode,resultCode,data);
        //}
        super.onActivityResult(requestCode, resultCode, data);
    }

}