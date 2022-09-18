package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class userprofile extends AppCompatActivity {

    TextView userName1, userName2, userEmail, userPhone, userCity, userSociety, userRole, userBlock;
    FrameLayout fmlay;
    SharedPreferences sharedPreferences;
    public static final String fileName = "USER_PROFILE";
    LinearLayout userBlockll, userRolell;
    private static final String user_profile = "https://vivek.ninja/userprofile/userprofile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        //Biunding
        userName1 = findViewById(R.id.userName1);
        userName2 = findViewById(R.id.userName2);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userCity = findViewById(R.id.userCity);
        userSociety = findViewById(R.id.userSoc);
        userRole = findViewById(R.id.userRole);
        userBlock = findViewById(R.id.userBlock);
        fmlay = findViewById(R.id.userBackButton);
        userBlockll = findViewById(R.id.userBlockll);
        userRolell = findViewById(R.id.userRolell);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        StringRequest sr = new StringRequest(POST, user_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject js = jsonArray.getJSONObject(0);
                            String res = js.getString("ACK");
                            if (!res.contains("no")) {
                                //userEmail= res;

                                Log.e("Response", js + " Hii");

                                userName1.setText(js.getString("name").toString());
                                userName2.setText(js.getString("name").toString());
                                userEmail.setText(js.getString("email").toString());
                                userPhone.setText(js.getString("phoneno").toString());
                                userSociety.setText(js.getString("society"));
                                //Log.e("Response", js.getString("s_name") + " Hii");
                                userCity.setText(js.getString("city"));

                                switch (js.getString("role")) {
                                    case "Admin":
                                    case "Society Member":
                                        Log.e("Status","General GOne");
                                        userRole.setText(js.getString("persue"));
                                        userBlock.setText(js.getString("block"));
                                        break;
                                    case "Security":
                                        Log.e("Status","security GOne");
                                        userBlockll.setVisibility(View.GONE);
                                        userRolell.setVisibility(View.GONE);
                                        break;
                                }

                                //userEmail= js.getString("email");
                                //userName  = js.getString("name");
                                //Log.e("DSD",username+"ddddd "+email);
                                //userPhoneNumber = "+91"+phonenumber.getText().toString();
                            } else if (res.contains("no")) {
                                //loading= new LoadingDialog(login.this);
                                Toast.makeText(userprofile.this, "Sorry !", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("JSON Parser", "Error parsing data " + error.toString());
                Toast.makeText(userprofile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                //String path = getPath(filepath);
                //map.put("namekey",name.getText().toString().trim());
                map.put("mobilekey", sharedPreferences.getString("phoneno", ""));
                //map.put("role",spinnerrole.getSelectedItem().toString());
                //map.put("image",encodeBitmapImage(bitmap));
                return map;
            }


        };
        RequestQueue rq = Volley.newRequestQueue(userprofile.this);
        rq.add(sr);


        fmlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(userprofile.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });

    }


}