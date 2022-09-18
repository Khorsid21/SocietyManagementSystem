package com.example.societymanagmentsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class ManageSoc extends AppCompatActivity {

    RecyclerView manage_soc__recyclerview;
    String typeWorker;
    List<SocData> datalist;
    FoldingCell fc;
    String city,society;
    String workerNum;
    int REQUEST_CALL = 1;
    FloatingActionButton Add_Soc_btn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sharedPreferences;
    //Button Logout;
    public LoadingDialog loading;
    public static final String fileName = "USER_PROFILE";
    private static final String url = "https://vivek.ninja/ManageSociety/fetch_all_soc.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/ManageSociety/add_soc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_soc);

        manage_soc__recyclerview = findViewById(R.id.MS_list_recyclerview);
        Add_Soc_btn = findViewById(R.id.AddSoc);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.MSswipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        society = sharedPreferences.getString("society", "");
        city = sharedPreferences.getString("city", "");



        manage_soc__recyclerview.setLayoutManager(new LinearLayoutManager(this));



        StringRequest request = new StringRequest(POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(showdata.this, response, Toast.LENGTH_SHORT).show();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                datalist = new ArrayList<>();
                Type userListType = new TypeToken<ArrayList<SocData>>() {
                }.getType();
                ArrayList<SocData> datalist = gson.fromJson(response, userListType);

                //Fetchall []data=gson.fromJson(response,Fetchall[].class);

                /*for(Fetchall user : data) {
                    Log.e(user.t);
                }*/
                manage_soc__recyclerview.setAdapter(new AdapterManageSoc(getApplicationContext(), ManageSoc.this, datalist,society,city));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManageSoc.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("society",society);
                map.put("city",city);


                return  map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        Add_Soc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater layoutInflaterMSG = LayoutInflater.from(ManageSoc.this);
                View ViewMSG = layoutInflaterMSG.inflate(R.layout.fragment_edit_soc_details, null);
                AlertDialog.Builder MSGalertDialogBuilderUserInput = new AlertDialog.Builder(ManageSoc.this);
                MSGalertDialogBuilderUserInput.setView(ViewMSG);
                //
                final EditText editCity = ViewMSG.findViewById(R.id.editcity);
                final TextView Title = ViewMSG.findViewById(R.id.HeadingTitle);
                final EditText editSoc = ViewMSG.findViewById(R.id.editsoc);
                final EditText editBlock = ViewMSG.findViewById(R.id.editblock);
                final CardView DelDone = ViewMSG.findViewById(R.id.EditDone);
                final CardView DelCancel = ViewMSG.findViewById(R.id.EditCancel);
                //final Button selTime = mViewi.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialogi = MSGalertDialogBuilderUserInput.create();

                Title.setText("Add New Block / Wing ");

                DelDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*if(editBlock.getText().toString().isEmpty() ||
                        editCity.getText().toString().isEmpty() ||
                        editSoc.getText().toString().isEmpty()){
                            Toast.makeText(ManageSoc.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        if(editBlock.getText().toString().isEmpty()){
                            editBlock.setError("Block/Wing Name is Required !");
                            return;
                            //Toast.makeText(ManageSoc.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                        }
                        /*if(editCity.getText().toString().isEmpty()){
                            editCity.setError("City Name is Required !");
                            return;
                            //Toast.makeText(ManageSoc.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                        }
                        if(editSoc.getText().toString().isEmpty()){
                            editSoc.setError("Society Name is Required !");
                            return;
                            //Toast.makeText(ManageSoc.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                        }*/


                        StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                                            JSONObject js = jsonArray.getJSONObject(0);
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;
                                                dialogi.dismiss();
                                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                                queue.add(request);
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(ManageSoc.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ManageSoc.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Block / Wing Added Successfully !");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                                final AlertDialog dialogii = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialogii.dismiss();
                                                    }
                                                });

                                                dialogii.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialogii.setCanceledOnTouchOutside(false);
                                                dialogii.show();

                                                //Toast.makeText(ManageSoc.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(res.contains("no")){
                                                //loading= new LoadingDialog(login.this);
                                                //loading.dismissDialog();
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(ManageSoc.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ManageSoc.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                                final AlertDialog dialogii = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialogii.dismiss();
                                                    }
                                                });

                                                dialogii.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialogii.setCanceledOnTouchOutside(false);
                                                dialogii.show();
                                                //Toast.makeText(ManageSoc.this, "NOooooooo", Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                            }

                                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            //loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(ManageSoc.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ManageSoc.this);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText(e.getMessage());
                                            reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                    getResources().getDimension(R.dimen.result_font));
                                            reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                            final AlertDialog dialogii = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialogii.dismiss();
                                                }
                                            });

                                            dialogii.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialogii.setCanceledOnTouchOutside(false);
                                            dialogii.show();
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //loading.dismissDialog();
                                Log.e("JSON Parser", "Error parsing data " + error.toString());
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(ManageSoc.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ManageSoc.this);
                                alertDialogBuilderUserInput.setView(mViewi);
                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                reasonTv.setText(error.getMessage());
                                reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                        getResources().getDimension(R.dimen.result_font));
                                reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                reasonBtn.setText("OK");
                                reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                final AlertDialog dialogii = alertDialogBuilderUserInput.create();

                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogii.dismiss();
                                    }
                                });

                                dialogii.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialogii.setCanceledOnTouchOutside(false);
                                dialogii.show();
                                //Toast.makeText(ManageSoc.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();

                                map.put("sockey",society);
                                //Log.e("Soc",editSoc.getText().toString());
                                map.put("blkkey",editBlock.getText().toString());
                                map.put("citykey",city);


                                return  map;
                            }


                        };

                        RequestQueue rq = Volley.newRequestQueue(ManageSoc.this);
                        rq.add(sr);


                    }
                });

                DelCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogi.dismiss();

                    }
                });



                dialogi.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogi.setCanceledOnTouchOutside(false);
                dialogi.show();

            }
        });

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