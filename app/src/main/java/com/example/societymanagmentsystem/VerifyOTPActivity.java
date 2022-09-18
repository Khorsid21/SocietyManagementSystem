package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VerifyOTPActivity extends AppCompatActivity  {

    //private static final String URL_PRODUCTS= "http://vivekdabhi.atwebpages.com/json_fetch.php";
    private static final String URL_PRODUCTS= "https://vivek.ninja/json_fetch.php";
    private ProductAdapter.RecyclerViewClickListener listener;
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Theme_SocietyManagmentSystem);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_otp);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerView);
        setOnClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();
    }

    private void setOnClickListener() {
        listener = new ProductAdapter.RecyclerViewClickListener() {
            @Override
            public void onButtonYesClick(View view, int position) {

            }

            @Override
            public void onClick(View v, int position) {
                Intent i =new Intent(getApplicationContext(),Profile.class);
                i.putExtra("phoneno",productList.get(position).getPhoneno());
                startActivity(i);
            }

            @Override
            public void onButtonNoClick(View view, int position) {

            }
        };
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new Product(
                                        product.getString("name"),
                                        product.getString("email"),
                                        product.getString("phoneno")


                                ));
                            }
                            Activity activity;

                            //creating adapter object and setting it to recyclerview
                            ProductAdapter adapter = new ProductAdapter(getApplicationContext(), productList,VerifyOTPActivity.this,listener);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
    }


