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

public class ManageChildExit extends AppCompatActivity {

    RecyclerView ChildExitRview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String city,society;

    List<AllowChildExitData> datalist;
    private static final String url = "https://vivek.ninja/childexit/fetch_child_exit.php";

    SharedPreferences sharedPreferences;
    //Button Logout;
    public static final String fileName = "USER_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_child_exit);

        ChildExitRview = findViewById(R.id.ChildExit_list_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.ChildExitswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        society = sharedPreferences.getString("society", "");
        city = sharedPreferences.getString("city", "");

        ChildExitRview.setLayoutManager(new LinearLayoutManager(ManageChildExit.this));

        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<AllowChildExitData>>() {
                }.getType();
                ArrayList<AllowChildExitData> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                ChildExitRview.setAdapter(new AdapterChildExit(getApplicationContext(), ManageChildExit.this, datalist));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManageChildExit.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                //String path = getPath(filepath);
                //map.put("namekey",name.getText().toString().trim());
                map.put("city", city);
                map.put("soc", society);
                //map.put("role",rb.getText().toString());
                //map.put("image",encodeBitmapImage(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ManageChildExit.this);
        queue.add(request);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(ManageChildExit.this);
                queue.add(request);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}